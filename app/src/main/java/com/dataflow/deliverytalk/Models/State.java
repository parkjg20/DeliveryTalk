package com.dataflow.deliverytalk.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class State implements Parcelable {

    String id;
    String text;


    public State(){}

    public State(String id, String text) {
        this.id = id;
        this.text = text;
    }
    protected State(Parcel in) {
        id = in.readString();
        text = in.readString();
    }

    public static final Creator<State> CREATOR = new Creator<State>() {
        @Override
        public State createFromParcel(Parcel in) {
            return new State(in);
        }

        @Override
        public State[] newArray(int size) {
            return new State[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(text);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
