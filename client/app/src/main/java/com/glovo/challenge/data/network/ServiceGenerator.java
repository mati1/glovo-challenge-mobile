package com.glovo.challenge.data.network;

import com.glovo.challenge.BuildConfig;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static final GsonConverterFactory GSON_CONVERTER_FACTORY = GsonConverterFactory.create(new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create());

    private static final Retrofit RETROFIT = new Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GSON_CONVERTER_FACTORY)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();

    public static ApiService createService() {
        return RETROFIT.create(ApiService.class);
    }
}