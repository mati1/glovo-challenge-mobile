package com.glovo.challenge.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import com.glovo.challenge.R;
import com.glovo.challenge.core.BaseActivity;
import com.glovo.challenge.data.dtos.City;
import com.glovo.challenge.data.dtos.CityDetail;
import com.glovo.challenge.location.LocationActivity;
import com.glovo.challenge.location.LocationModel;
import com.glovo.challenge.map.widgets.DetailView;
import com.glovo.challenge.map.widgets.LocationDialog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends BaseActivity<MapPresenter>
    implements MapContract.View, OnMapReadyCallback, LocationDialog.Callback, GoogleMap.OnCameraIdleListener,
    GoogleMap.OnMarkerClickListener {

    private static final int LOCATION_UPDATES = 1;

    private SupportMapFragment mMapFragment;
    private DetailView mDetailView;
    private GoogleMap mMap;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;

    private final HashMap<City, Polygon> mCityPolygons = new HashMap<>();
    private final HashMap<City, Marker> mCityMarkers = new HashMap<>();
    private final HashMap<Marker, City> mMarkerCities = new HashMap<>();

    private int mCityPolygonColor;

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(final LocationResult locationResult) {
            super.onLocationResult(locationResult);
            final Location location = locationResult.getLastLocation();

            getPresenter().onUserLocated(location.getLatitude(), location.getLongitude());
        }
    };

    @Override
    protected MapPresenter createPresenter() {
        return new MapPresenter(new LocationModel());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mCityPolygonColor = ContextCompat.getColor(this, R.color.colorMapWorkingArea);

        mDetailView = findViewById(R.id.detailView);
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        final boolean granted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED;

        getPresenter().checkPermission(granted);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LocationActivity.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                City city = data.getParcelableExtra(LocationActivity.EXTRA_CITY);
                getPresenter().onUserSelectLocation(city);
            }
        }
    }

    @Override
    public void onPermissionGranted() {
        getPresenter().onRequestPermission(true);
    }

    @Override
    public void onPermissionNotGranted() {
        getPresenter().onRequestPermission(false);
    }

    @Override
    public void showRequestPermission() {
        LocationDialog.newInstance().show(getSupportFragmentManager(), LocationDialog.TAG);
    }

    @Override
    public void showManualLocation() {
        startActivityForResult(new Intent(this, LocationActivity.class), LocationActivity.REQUEST_CODE);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void locateUser() {
        if (mLocationRequest == null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setNumUpdates(LOCATION_UPDATES);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    @Override
    public void showMapCities(final List<City> cities) {
        for (City city : cities) {
            if (city.getWorkingArea() != null) {
                ArrayList<PolygonOptions> polygons = new ArrayList<>();

                for (final String polygonEncoded : city.getWorkingArea()) {
                    final PolygonOptions polygonOptions = new PolygonOptions();

                    polygonOptions.addAll(PolyUtil.decode(polygonEncoded));
                    polygons.add(polygonOptions);
                }

                final Polygon polygon = mMap.addPolygon(MapUtils.convexHull(polygons));
                polygon.setFillColor(mCityPolygonColor);
                polygon.setStrokeWidth(0f);
                polygon.setVisible(false);

                final MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(MapUtils.getCenter(polygon));
                markerOptions.title(city.getName());
                markerOptions.visible(false);

                final Marker marker = mMap.addMarker(markerOptions);

                mCityPolygons.put(city, polygon);
                mCityMarkers.put(city, marker);
                mMarkerCities.put(marker, city);
            }
        }
    }

    @Override
    public void showMapAt(final double lat, final double lng, float zoom) {
        mMap.setOnCameraIdleListener(this);
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));
    }

    @Override
    public LatLng getCityLocation(final City city) {
        return mCityMarkers.get(city).getPosition();
    }

    @Override
    public void showError() {
        new AlertDialog.Builder(this)
            .setMessage(R.string.generic_error)
            .setPositiveButton(android.R.string.ok, null)
            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(final DialogInterface dialogInterface) {
                    finish();
                }
            }).show();
    }

    @Override
    public City getCurrentCity() {
        for (Map.Entry<City, Polygon> map : mCityPolygons.entrySet()) {
            if (PolyUtil.containsLocation(mMap.getCameraPosition().target, map.getValue().getPoints(), false)) {
                return map.getKey();
            }
        }
        return null;
    }

    @Override
    public void setCityPinsVisibility(final boolean visible) {
        for (Map.Entry<City, Polygon> entry : mCityPolygons.entrySet()) {
            final Polygon polygon = entry.getValue();
            polygon.setVisible(visible);
        }
    }

    @Override
    public void setCityWorkingAreasVisibility(final boolean visible) {
        for (Map.Entry<City, Marker> entry : mCityMarkers.entrySet()) {
            final Marker marker = entry.getValue();
            marker.setVisible(visible);
        }
    }

    @Override
    public void showCityDetail(final CityDetail cityDetail) {
        mDetailView.setCityDetail(cityDetail);
    }

    @Override
    public void showLoadingCityDetail() {
        mDetailView.setLoading(true);
    }

    @Override
    public void showCityDetailUnknown() {
        mDetailView.showUnknown();
    }

    @Override
    public void onCameraIdle() {
        mMap.getUiSettings().setAllGesturesEnabled(true);

        final LatLng latLng = mMap.getCameraPosition().target;
        getPresenter().onLocationChange(latLng.latitude, latLng.longitude, mMap.getCameraPosition().zoom);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        final Polygon polygon = mCityPolygons.get(mMarkerCities.get(marker));
        final LatLngBounds latLngBounds = MapUtils.getLatLngBounds(polygon);

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
        return false;
    }
}
