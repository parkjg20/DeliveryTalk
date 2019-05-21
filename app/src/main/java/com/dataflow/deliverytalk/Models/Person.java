package com.dataflow.deliverytalk.Models;


import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {
    String name;
    String time;


    public Person(){}

    public Person(String name, String time) {
        this.name = name;
        this.time = time;
    }

    protected Person(Parcel in){
        name = in.readString();
        time = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(time);
    }
    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
