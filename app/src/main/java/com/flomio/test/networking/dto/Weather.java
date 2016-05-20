package com.flomio.test.networking.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Darien
 * on 5/20/16.
 * <p/>
 * Represent a weather condition
 */
public class Weather implements Parcelable {

    private Location mLocation;
    private List<Forecast> mForecastList;

    public Weather(Location mLocation, List<Forecast> mForecastList) {
        this.mLocation = mLocation;
        this.mForecastList = mForecastList;
    }

    public Location getLocation() {
        return mLocation;
    }

    public List<Forecast> getForecastList() {
        return mForecastList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mLocation, 0);
        dest.writeTypedList(mForecastList);
    }

    protected Weather(Parcel in) {
        this.mLocation = in.readParcelable(Location.class.getClassLoader());
        this.mForecastList = in.createTypedArrayList(Forecast.CREATOR);
    }

    public static final Parcelable.Creator<Weather> CREATOR = new Parcelable.Creator<Weather>() {
        public Weather createFromParcel(Parcel source) {
            return new Weather(source);
        }

        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };
}
