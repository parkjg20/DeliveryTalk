package com.dataflow.deliverytalk.Models;

public class TrackDetail {
    private String date;
    private String time;
    private Progress  progress;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "TrackDetail{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", progress=" + progress +
                '}';
    }
}
