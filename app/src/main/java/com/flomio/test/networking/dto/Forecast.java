package com.flomio.test.networking.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Darien
 * on 5/19/16.
 *
 * Represent a forecast
 */
public class Forecast implements Parcelable {

    private String mDay;
    private String mForecast;

    public Forecast(String day, String forecast) {
        this.mDay = day;
        this.mForecast = forecast;
    }

    public String getDay() {
        return mDay;
    }

    public String getForecast() {
        return mForecast;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mDay);
        dest.writeString(this.mForecast);
    }

    protected Forecast(Parcel in) {
        this.mDay = in.readString();
        this.mForecast = in.readString();
    }

    public static final Parcelable.Creator<Forecast> CREATOR = new Parcelable.Creator<Forecast>() {
        public Forecast createFromParcel(Parcel source) {
            return new Forecast(source);
        }

        public Forecast[] newArray(int size) {
            return new Forecast[size];
        }
    };
}
