package com.eatx.wdj.data.model;

public class mainModel {

    public String mTitle , BoardTitle;
    public String mText;
    public String mID;
    public String name;
    public int mSID;
    public boolean check;
    public String mSimpleTitle;

    public String getBoardTitle() {
        return BoardTitle;
    }

    public void setBoardTitle(String boardTitle) {
        BoardTitle = boardTitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String mSimpleText;
    private int viewType;
    public mainModel(String name, int viewType, boolean check) {
        this.name = name;
        this.viewType = viewType;
        this.check = check;
    }
    public mainModel(String name, int viewType, boolean check , boolean check2) {
        this.name = name;
        this.viewType = viewType;
    }
    public mainModel(String BoardTitle, int viewType) {
        this.BoardTitle = BoardTitle;
        this.viewType = viewType;
    }

    public mainModel(String mTitle, String mText, String mSimpleTitle, String mSimpleText, int viewType) {
        this.mTitle = mTitle;
        this.mText = mText;
        this.mSimpleTitle = mSimpleTitle;
        this.mSimpleText = mSimpleText;
        this.viewType = viewType;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public int getmSID() {
        return mSID;
    }

    public void setmSID(int mSID) {
        this.mSID = mSID;
    }

    public String getmSimpleTitle() {
        return mSimpleTitle;
    }

    public void setmSimpleTitle(String mSimpleTitle) {
        this.mSimpleTitle = mSimpleTitle;
    }

    public String getmSimpleText() {
        return mSimpleText;
    }

    public void setmSimpleText(String mSimpleText) {
        this.mSimpleText = mSimpleText;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}