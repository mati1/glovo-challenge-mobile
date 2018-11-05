package com.glovo.challenge.data.dtos;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class City implements Parcelable {

    private final String code;
    private final String name;
    private final String countryCode;
    private final ArrayList<String> workingArea;

    public City(final String code, final String name, final String countryCode, final ArrayList<String> workingArea) {
        this.code = code;
        this.name = name;
        this.countryCode = countryCode;
        this.workingArea = workingArea;
    }

    protected City(Parcel in) {
        code = in.readString();
        name = in.readString();
        countryCode = in.readString();
        workingArea = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(name);
        dest.writeString(countryCode);
        dest.writeStringList(workingArea);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public ArrayList<String> getWorkingArea() {
        return workingArea;
    }

    @Override
    public String toString() {
        return "City{" +
            "code='" + code + '\'' +
            ", name='" + name + '\'' +
            ", countryCode='" + countryCode + '\'' +
            ", workingArea='" + workingArea + '\'' +
            '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final City city = (City) o;

        if (code != null ? !code.equals(city.code) : city.code != null) {
            return false;
        }
        if (name != null ? !name.equals(city.name) : city.name != null) {
            return false;
        }
        if (countryCode != null ? !countryCode.equals(city.countryCode) : city.countryCode != null) {
            return false;
        }
        return workingArea != null ? workingArea.equals(city.workingArea) : city.workingArea == null;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (countryCode != null ? countryCode.hashCode() : 0);
        result = 31 * result + (workingArea != null ? workingArea.hashCode() : 0);
        return result;
    }
}
