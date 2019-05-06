package com.dataflow.deliverytalk.Models;

import java.util.List;

public class ParcelModel {

    private Person from;
    private Person to;
    private State state;
    private List<Progress> progresses;
    private Carrier carrier;

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
}
