package com.eatx.wdj.data.model;

public class Checkers {

    private String name , classValue , timestamp , serverTime , serverDate ,date;
    private int sid , run;

    public int getRun() {
        return run;
    }

    public void setRun(int run) {
        this.run = run;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public String getServerDate() {
        return serverDate;
    }

    public void setServerDate(String serverDate) {
        this.serverDate = serverDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Checkers() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassValue() {
        return classValue;
    }

    public void setClassValue(String classValue) {
        this.classValue = classValue;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }
}
