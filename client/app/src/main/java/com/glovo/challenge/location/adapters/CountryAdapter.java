package com.glovo.challenge.location.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.glovo.challenge.R;
import com.glovo.challenge.data.dtos.Country;
import com.glovo.challenge.location.holders.CountryViewHolder;
import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryViewHolder> {

    final List<Country> mData;
    final CountryClickListener mListener;

    public CountryAdapter(final List<Country> countries, final CountryClickListener listener) {
        mData = countries;
        mListener = listener;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        return new CountryViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(i, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CountryViewHolder countryViewHolder, final int i) {
        countryViewHolder.onBind(mData.get(i));

        countryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mListener.onCountrySelected(mData.get(countryViewHolder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.holder_country;
    }

    public interface CountryClickListener {

        void onCountrySelected(@NonNull Country country);
    }
}
