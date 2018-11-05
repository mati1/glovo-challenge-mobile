package com.glovo.challenge.location;

import com.glovo.challenge.core.BaseModel;
import com.glovo.challenge.core.BaseObserver;
import com.glovo.challenge.core.BasePresenter;
import com.glovo.challenge.core.BaseView;
import com.glovo.challenge.data.dtos.City;
import com.glovo.challenge.data.dtos.CityDetail;
import com.glovo.challenge.data.dtos.Country;
import java.util.List;

public class LocationContract {

    public interface Model extends BaseModel {

        void getCountries(BaseObserver<List<Country>> observer);

        void getCities(BaseObserver<List<City>> observer);

        void getCities(String countryCode, BaseObserver<List<City>> observer);

        void getCityDetail(String cityCode, BaseObserver<CityDetail> observer);
    }

    public interface View extends BaseView {

        void setLoadingIndicator(boolean active);

        void showCountries(List<Country> countries);

        void showCities(List<City> cities);

        void showError();
    }

    abstract static class Presenter extends BasePresenter<LocationContract.View> {

        public abstract void loadCountries();

        public abstract void loadCities(final String countryCode);
    }
}
