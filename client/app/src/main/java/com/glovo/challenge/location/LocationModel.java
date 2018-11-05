package com.glovo.challenge.location;

import com.glovo.challenge.core.BaseObserver;
import com.glovo.challenge.data.dtos.City;
import com.glovo.challenge.data.dtos.CityDetail;
import com.glovo.challenge.data.dtos.Country;
import com.glovo.challenge.data.network.ApiService;
import com.glovo.challenge.data.network.ServiceGenerator;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class LocationModel implements LocationContract.Model {

    private static final ApiService apiService = ServiceGenerator.createService();

    @Override
    public void getCountries(final BaseObserver<List<Country>> observer) {
        apiService.getCountries()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer);
    }

    @Override
    public void getCities(final BaseObserver<List<City>> observer) {
        apiService.getCities()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer);
    }

    @Override
    public void getCities(final String countryCode, final BaseObserver<List<City>> observer) {
        apiService.getCities()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap(new Function<List<City>, ObservableSource<City>>() {
                @Override
                public ObservableSource<City> apply(List<City> tickets) throws Exception {
                    return Observable.fromIterable(tickets);
                }
            })
            .filter(new Predicate<City>() {
                @Override
                public boolean test(final City city) throws Exception {
                    return city.getCountryCode() != null && countryCode.equals(city.getCountryCode());
                }
            })
            .toList()
            .toObservable()
            .subscribe(observer);
    }

    @Override
    public void getCityDetail(final String cityCode, final BaseObserver<CityDetail> observer) {
        apiService.getCity(cityCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer);
    }
}
