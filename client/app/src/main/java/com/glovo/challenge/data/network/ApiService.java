package com.glovo.challenge.data.network;

import com.glovo.challenge.data.dtos.City;
import com.glovo.challenge.data.dtos.CityDetail;
import com.glovo.challenge.data.dtos.Country;
import io.reactivex.Observable;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("countries")
    Observable<List<Country>> getCountries();

    @GET("cities")
    Observable<List<City>> getCities();

    @GET("cities/{code}")
    Observable<CityDetail> getCity(@Path("code") String code);
}
