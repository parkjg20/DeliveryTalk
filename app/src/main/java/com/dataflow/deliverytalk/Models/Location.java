package com.dataflow.deliverytalk.Models;


import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {
    String name;


    public Location(){}

    public Location(String name) {
        this.name = name;
    }

    protected Location(Parcel in){
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
    }
    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
