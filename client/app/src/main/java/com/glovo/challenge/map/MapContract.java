package com.glovo.challenge.map;

import com.glovo.challenge.core.BasePresenter;
import com.glovo.challenge.core.BaseView;
import com.glovo.challenge.data.dtos.City;
import com.glovo.challenge.data.dtos.CityDetail;
import com.google.android.gms.maps.model.LatLng;
import java.util.List;

public class MapContract {

    interface View extends BaseView {

        void showRequestPermission();

        void showManualLocation();

        void showLoadingCityDetail();

        void showCityDetail(CityDetail cityDetail);

        void showCityDetailUnknown();

        void showMapCities(List<City> cities);

        void showMapAt(double lat, double lng, float zoom);

        void showError();

        void setCityPinsVisibility(boolean visible);

        void setCityWorkingAreasVisibility(boolean visible);

        void locateUser();

        LatLng getCityLocation(City city);

        City getCurrentCity();

    }

    abstract static class Presenter extends BasePresenter<View> {

        abstract void checkPermission(boolean granted);

        abstract void onRequestPermission(boolean granted);

        abstract void onUserLocated(double lat, double lng);

        abstract void onLocationChange(double lat, double lng, float zoom);

        abstract void onUserSelectLocation(City city);

    }
}
