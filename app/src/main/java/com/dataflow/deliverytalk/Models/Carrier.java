package com.dataflow.deliverytalk.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Carrier implements Parcelable {
    String id;
    String name;
    String tel;
    String logo;
    String homepage;

    public Carrier(){}

    public Carrier(String id, String name, String tel, String logo, String homepage) {
        this.id = id;
        this.name = name;
        this.tel = tel;
        this.logo = logo;
        this.homepage = homepage;
    }

    protected Carrier(Parcel in){
        id = in.readString();
        name = in.readString();
        tel = in.readString();
        logo = in.readString();
        homepage = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(name);
        out.writeString(tel);
        out.writeString(logo);
        out.writeString(homepage);
    }
    public static final Creator<Carrier> CREATOR = new Creator<Carrier>() {
        @Override
        public Carrier createFromParcel(Parcel in) {
            return new Carrier(in);
        }

        @Override
        public Carrier[] newArray(int size) {
            return new Carrier[size];
        }
    };

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

}
