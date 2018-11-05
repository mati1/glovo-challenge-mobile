package com.glovo.challenge.data.dtos;

import android.os.Parcel;
import android.os.Parcelable;

public class Country implements Parcelable {

    private final String code;
    private final String name;

    public Country(final String code, final String name) {
        this.code = code;
        this.name = name;
    }

    protected Country(Parcel in) {
        code = in.readString();
        name = in.readString();
    }

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Country{" +
            "code='" + code + '\'' +
            ", name='" + name + '\'' +
            '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeString(code);
        parcel.writeString(name);
    }
}
