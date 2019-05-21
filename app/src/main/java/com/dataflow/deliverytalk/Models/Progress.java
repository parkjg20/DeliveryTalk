package com.dataflow.deliverytalk.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Progress implements Parcelable {
    String time;
    String location;
    State status;
    String description;

    public Progress(){}

    protected Progress(Parcel in){
        time = in.readString();
        location = in.readString();
        status = in.readParcelable(State.class.getClassLoader());
        description = in.readString();
    }

    public static final Creator<Progress> CREATOR = new Creator<Progress>() {
        @Override
        public Progress createFromParcel(Parcel in) {
            return new Progress(in);
        }

        @Override
        public Progress[] newArray(int size) {
            return new Progress[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel in, int flags) {
        in.writeString(time);
        in.writeString(location);
        in.writeParcelable(status, flags);
        in.writeString(description);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public State getStatus() {
        return status;
    }

    public void setStatus(State status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
