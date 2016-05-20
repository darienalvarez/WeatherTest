package com.flomio.test.networking.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Darien
 * on 5/19/16.
 * <p>
 * Represent a location
 */
public class Location implements Parcelable {

    private String mState;
    private String mCity;

    public Location(String state, String city) {
        this.mState = state;
        this.mCity = city;
    }

    public String getState() {
        return mState;
    }

    public String getCity() {
        return mCity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mState);
        dest.writeString(this.mCity);
    }

    protected Location(Parcel in) {
        this.mState = in.readString();
        this.mCity = in.readString();
    }

    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
