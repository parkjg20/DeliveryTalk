package com.dataflow.deliverytalk.Models;

public class Person {
    public Person(){}
    public Person(String name, String time) {
        this.name = name;
        this.time = time;
    }

    String name;
    String time;

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
