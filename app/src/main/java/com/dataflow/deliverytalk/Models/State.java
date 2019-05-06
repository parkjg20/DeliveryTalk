package com.dataflow.deliverytalk.Models;

public class State {

    public State(String id, String text) {
        this.id = id;
        this.text = text;
    }

    String id;
    String text;

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
