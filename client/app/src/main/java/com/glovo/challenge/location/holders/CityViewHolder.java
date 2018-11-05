package com.glovo.challenge.location.holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.glovo.challenge.R;
import com.glovo.challenge.data.dtos.City;

public class CityViewHolder extends RecyclerView.ViewHolder {

    private final TextView titleView;

    public CityViewHolder(final View itemView) {
        super(itemView);

        titleView = itemView.findViewById(R.id.title);
    }

    public void onBind(@NonNull final City city) {
        titleView.setText(city.getName());
    }
}