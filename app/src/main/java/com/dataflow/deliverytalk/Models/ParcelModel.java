package com.dataflow.deliverytalk.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ParcelModel implements Parcelable {

    private String parcelKey;
    private String title;
    private String waybill;
    private Person from;
    private Person to;
    private State state;
    private List<Progress> progresses;
    private Carrier carrier;
    private boolean alarm;

    public ParcelModel(){}
    protected ParcelModel(Parcel in){
        parcelKey = in.readString();
        title = in.readString();
        waybill = in.readString();
        from = in.readParcelable(Person.class.getClassLoader());
        to = in.readParcelable(Person.class.getClassLoader());
        state = in.readParcelable(State.class.getClassLoader());
        progresses = new ArrayList<>();
        in.readList(progresses, Progress.class.getClassLoader());
        carrier = in.readParcelable(Carrier.class.getClassLoader());
        alarm = in.readByte()==1;
    }

    public static final Creator<ParcelModel> CREATOR = new Creator<ParcelModel>() {
        @Override
        public ParcelModel createFromParcel(Parcel in) {
            return new ParcelModel(in);
        }

        @Override
        public ParcelModel[] newArray(int size) {
            return new ParcelModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(parcelKey);
        out.writeString(title);
        out.writeString(waybill);
        out.writeParcelable(from, flags);
        out.writeParcelable(to, flags);
        out.writeParcelable(state, flags);
        out.writeList(progresses);
        out.writeParcelable(carrier, flags);
        out.writeByte((byte)((alarm)?1:0));
    }

    public String getParcelKey() {
        return parcelKey;
    }

    public void setParcelKey(String parcelKey) {
        this.parcelKey = parcelKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Person getFrom() {
        return from;
    }

    public void setFrom(Person from) {
        this.from = from;
    }

    public Person getTo() {
        return to;
    }

    public void setTo(Person to) {
        this.to = to;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<Progress> getProgresses() {
        return progresses;
    }

    public void setProgresses(List<Progress> progresses) {
        this.progresses = progresses;
    }

    public Carrier getCarrier() {
        return carrier;
    }

    public void setCarrier(Carrier carrier) {
        this.carrier = carrier;
    }

    public String getWaybill() {
        return waybill;
    }

    public void setWaybill(String waybill) {
        this.waybill = waybill;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

}
