package com.glovo.challenge.location;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ViewFlipper;
import com.glovo.challenge.R;
import com.glovo.challenge.core.BaseActivity;
import com.glovo.challenge.data.dtos.City;
import com.glovo.challenge.data.dtos.Country;
import com.glovo.challenge.location.adapters.CityAdapter;
import com.glovo.challenge.location.adapters.CountryAdapter;
import java.util.List;

public class LocationActivity extends BaseActivity<LocationPresenter> implements LocationContract.View,
    CountryAdapter.CountryClickListener, CityAdapter.CityClickListener {

    public static final int REQUEST_CODE = 546;
    public static final String EXTRA_CITY = "CITY";

    private ProgressBar mProgressBar;
    private ViewFlipper mViewFlipper;
    private RecyclerView mRecyclerCountries;
    private RecyclerView mRecyclerCities;

    private CountryAdapter mCountryAdapter;
    private CityAdapter mCitiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mProgressBar = findViewById(R.id.progressBar);
        mViewFlipper = findViewById(R.id.flipper);
        mRecyclerCountries = findViewById(R.id.countries);
        mRecyclerCities = findViewById(R.id.cities);

        mRecyclerCountries.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerCities.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected LocationPresenter createPresenter() {
        return new LocationPresenter(new LocationModel());
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        mProgressBar.setVisibility(active ? View.VISIBLE : View.GONE);
        mViewFlipper.setVisibility(active ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showCountries(final List<Country> countries) {
        mCountryAdapter = new CountryAdapter(countries, this);
        mRecyclerCountries.setAdapter(mCountryAdapter);
    }

    @Override
    public void showCities(final List<City> cities) {
        if (mCitiesAdapter == null) {
            mCitiesAdapter = new CityAdapter(this);
            mRecyclerCities.setAdapter(mCitiesAdapter);
        }
        mCitiesAdapter.setData(cities);
        mViewFlipper.showNext();
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
            })
            .show();
    }

    @Override
    public void onCountrySelected(@NonNull final Country country) {
        getPresenter().loadCities(country.getCode());
    }

    @Override
    public void onCitySelected(@NonNull final City city) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CITY, city);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (mViewFlipper.getDisplayedChild() > 0) {
            mViewFlipper.showPrevious();
        } else {
            super.onBackPressed();
        }
    }
}
