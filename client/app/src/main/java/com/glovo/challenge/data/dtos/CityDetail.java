package com.glovo.challenge.data.dtos;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class CityDetail extends City implements Parcelable {

    private final String languageCode;
    private final String currency;
    private final String timeZone;
    private final boolean enabled;
    private final boolean busy;

    public CityDetail(final String code, final String name, final String countryCode, final ArrayList<String> workingArea,
        final String languageCode, final String currency, final String timeZone, final boolean enabled,
        final boolean busy) {
        super(code, name, countryCode, workingArea);
        this.languageCode = languageCode;
        this.currency = currency;
        this.timeZone = timeZone;
        this.enabled = enabled;
        this.busy = busy;
    }

    protected CityDetail(Parcel in) {
        super(in);
        languageCode = in.readString();
        currency = in.readString();
        timeZone = in.readString();
        enabled = in.readByte() != 0;
        busy = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(languageCode);
        dest.writeString(currency);
        dest.writeString(timeZone);
        dest.writeByte((byte) (enabled ? 1 : 0));
        dest.writeByte((byte) (busy ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CityDetail> CREATOR = new Creator<CityDetail>() {
        @Override
        public CityDetail createFromParcel(Parcel in) {
            return new CityDetail(in);
        }

        @Override
        public CityDetail[] newArray(int size) {
            return new CityDetail[size];
        }
    };

    public String getLanguageCode() {
        return languageCode;
    }

    public String getCurrency() {
        return currency;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isBusy() {
        return busy;
    }

    @Override
    public String toString() {
        return "CityDetail{" +
            "languageCode='" + languageCode + '\'' +
            ", currency='" + currency + '\'' +
            ", timeZone='" + timeZone + '\'' +
            ", enabled=" + enabled +
            ", busy=" + busy +
            "} " + super.toString();
    }
}
