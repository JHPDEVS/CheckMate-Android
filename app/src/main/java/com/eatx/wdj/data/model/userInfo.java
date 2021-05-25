package com.eatx.wdj.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class userInfo implements Serializable {
    private int sid;
    private String name;
    private String id;


    public userInfo() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
