package com.glovo.challenge.location.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.glovo.challenge.R;
import com.glovo.challenge.data.dtos.City;
import com.glovo.challenge.location.holders.CityViewHolder;
import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityViewHolder> {

    private final CityClickListener mListener;

    private List<City> mData = new ArrayList<>();

    public CityAdapter(final CityClickListener listener) {
        mListener = listener;
    }

    public void setData(@NonNull final List<City> cities) {
        mData = cities;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        return new CityViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(i, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CityViewHolder cityViewHolder, final int i) {
        cityViewHolder.onBind(mData.get(i));

        cityViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mListener.onCitySelected(mData.get(cityViewHolder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.holder_city;
    }

    public interface CityClickListener {

        void onCitySelected(@NonNull City city);
    }
}