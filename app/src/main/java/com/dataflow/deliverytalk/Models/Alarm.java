package com.dataflow.deliverytalk.Models;

public class Alarm {
    private int id;
    private short type;
    private String content;
    private String wdate;
    private String parcelId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWdate() {
        return wdate;
    }

    public void setWdate(String wdate) {
        this.wdate = wdate;
    }

    public String getParcelId() {
        return parcelId;
    }

    public void setParcelId(String parcelId) {
        this.parcelId = parcelId;
    }
}
