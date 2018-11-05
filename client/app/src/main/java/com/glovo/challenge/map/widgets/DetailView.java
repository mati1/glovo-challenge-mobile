package com.glovo.challenge.map.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.glovo.challenge.R;
import com.glovo.challenge.data.dtos.CityDetail;

public class DetailView extends FrameLayout {

    private ImageView mImageView;
    private TextView mCityText;
    private TextView mLanguageText;
    private TextView mCurrencyText;
    private ProgressBar mProgress;

    private Drawable mLocationOk;
    private Drawable mLocationError;

    public DetailView(@NonNull final Context context) {
        super(context);
        init();
    }

    public DetailView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_detail, this);

        mLocationOk = ContextCompat.getDrawable(getContext(), R.drawable.ic_placeholder_ok);
        mLocationError = ContextCompat.getDrawable(getContext(), R.drawable.ic_placeholder_error);

        mImageView = findViewById(R.id.imageView);
        mCityText = findViewById(R.id.city);
        mLanguageText = findViewById(R.id.language);
        mCurrencyText = findViewById(R.id.currency);
        mProgress = findViewById(R.id.progressBar);

        setLoading(true);
    }

    public void setLoading(boolean loading) {
        mProgress.setVisibility(loading ? VISIBLE : GONE);
        mImageView.setVisibility(loading ? INVISIBLE : VISIBLE);
        mLanguageText.setVisibility(loading ? INVISIBLE : VISIBLE);
        mCityText.setVisibility(loading ? INVISIBLE : VISIBLE);
        mCurrencyText.setVisibility(loading ? INVISIBLE : VISIBLE);
    }

    public void setCityDetail(CityDetail cityDetail) {
        setLoading(false);
        populateDetail(true, cityDetail.getName(), cityDetail.getLanguageCode(), cityDetail.getCurrency());
    }

    public void showUnknown() {
        setLoading(false);
        populateDetail(false, "???", "???", "???");
    }

    private void populateDetail(boolean enabled, String city, String language, String currency) {
        mImageView.setImageDrawable(enabled ? mLocationOk : mLocationError);
        mCityText.setText(getResources().getString(R.string.detail_city, city));
        mLanguageText.setText(getResources().getString(R.string.detail_language, language));
        mCurrencyText.setText(getResources().getString(R.string.detail_currency, currency));
    }
}
