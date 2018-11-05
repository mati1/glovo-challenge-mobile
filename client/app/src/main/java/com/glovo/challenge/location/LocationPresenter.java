package com.glovo.challenge.location;

import com.glovo.challenge.core.BaseObserver;
import com.glovo.challenge.core.BaseView;
import com.glovo.challenge.data.dtos.City;
import com.glovo.challenge.data.dtos.Country;
import java.util.List;

public class LocationPresenter extends LocationContract.Presenter {

    private final LocationModel model;

    public LocationPresenter(final LocationModel model) {
        this.model = model;
    }

    @Override
    protected void onViewAttached(final BaseView view) {
        loadCountries();
    }

    @Override
    protected void onViewDetached(final BaseView view) {

    }

    @Override
    public void loadCountries() {
        getView().setLoadingIndicator(true);
        model.getCountries(new BaseObserver<List<Country>>() {
            @Override
            public void onSuccess(final List<Country> response) {
                getView().showCountries(response);
                getView().setLoadingIndicator(false);
            }

            @Override
            public void onError() {
                getView().showError();
            }
        });
    }

    @Override
    public void loadCities(final String countryCode) {
        getView().setLoadingIndicator(true);
        model.getCities(countryCode, new BaseObserver<List<City>>() {
            @Override
            public void onSuccess(final List<City> response) {
                getView().showCities(response);
                getView().setLoadingIndicator(false);
            }

            @Override
            public void onError() {
                getView().showError();
            }
        });
    }

}
