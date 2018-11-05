package com.glovo.challenge.location.holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.glovo.challenge.R;
import com.glovo.challenge.data.dtos.Country;

public class CountryViewHolder extends RecyclerView.ViewHolder {

    private final TextView titleView;

    public CountryViewHolder(final View itemView) {
        super(itemView);

        titleView = itemView.findViewById(R.id.title);
    }

    public void onBind(@NonNull final Country country) {
        titleView.setText(country.getName());
    }
}
