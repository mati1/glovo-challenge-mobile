package com.glovo.challenge.map;

import com.glovo.challenge.core.BaseObserver;
import com.glovo.challenge.core.BaseView;
import com.glovo.challenge.data.dtos.City;
import com.glovo.challenge.data.dtos.CityDetail;
import com.glovo.challenge.location.LocationModel;
import com.google.android.gms.maps.model.LatLng;
import java.util.List;

public class MapPresenter extends MapContract.Presenter {

    private static final float ZOOM_SHOW_PINS = 10f;
    private static final float ZOOM_ON_LOCATE_USER = 15f;

    private final LocationModel mLocationModel;

    private boolean mFirstTimeLocated = true;

    public MapPresenter(final LocationModel locationModel) {
        mLocationModel = locationModel;
    }

    @Override
    protected void onViewAttached(final BaseView view) {

    }

    @Override
    protected void onViewDetached(final BaseView view) {

    }

    @Override
    void checkPermission(final boolean granted) {
        if (granted) {
            mLocationModel.getCities(new BaseObserver<List<City>>() {
                @Override
                public void onSuccess(final List<City> response) {
                    getView().showMapCities(response);
                    getView().locateUser();
                }

                @Override
                public void onError() {
                    getView().showError();
                }
            });
        } else {
            getView().showRequestPermission();
        }
    }

    @Override
    void onRequestPermission(final boolean granted) {
        if (granted) {
            getView().locateUser();
        } else {
            mLocationModel.getCities(new BaseObserver<List<City>>() {
                @Override
                public void onSuccess(final List<City> response) {
                    getView().showMapCities(response);
                    getView().showManualLocation();
                }

                @Override
                public void onError() {
                    getView().showError();
                }
            });
        }
    }

    @Override
    void onUserLocated(final double lat, final double lng) {
        getView().showMapAt(lat, lng, ZOOM_ON_LOCATE_USER);
    }

    @Override
    void onLocationChange(final double lat, final double lng, final float zoom) {
        boolean showPins = zoom > ZOOM_SHOW_PINS;

        getView().setCityPinsVisibility(showPins);
        getView().setCityWorkingAreasVisibility(!showPins);

        final City city = getView().getCurrentCity();

        loadCityDetail(city);
    }

    @Override
    void onUserSelectLocation(final City city) {
        final LatLng latLng = getView().getCityLocation(city);

        getView().showMapAt(latLng.latitude, latLng.longitude, ZOOM_ON_LOCATE_USER);
    }

    void loadCityDetail(City city) {
        getView().showLoadingCityDetail();

        if (mFirstTimeLocated) {
            mFirstTimeLocated = false;
            if (city == null) {
                getView().showManualLocation();
            }
        }

        if (city == null) {
            getView().showCityDetailUnknown();
        } else {
            mLocationModel.getCityDetail(city.getCode(), new BaseObserver<CityDetail>() {
                @Override
                public void onSuccess(final CityDetail response) {
                    getView().showCityDetail(response);
                }

                @Override
                public void onError() {
                    getView().showCityDetailUnknown();
                }
            });
        }
    }
}
