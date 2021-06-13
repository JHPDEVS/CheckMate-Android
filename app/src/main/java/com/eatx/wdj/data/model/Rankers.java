package com.eatx.wdj.data.model;

public class Rankers {
    private String name , classValue;
    private int sid, totalrun, countlate;

    public Rankers() {

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

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getTotalrun() {
        return totalrun;
    }

    public void setTotalrun(int totalrun) {
        this.totalrun = totalrun;
    }

    public int getCountlate() {
        return countlate;
    }

    public void setCountlate(int countlate) {
        this.countlate = countlate;
    }
}