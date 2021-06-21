package com.eatx.wdj.data.model;

public class noticeBoardModel {
    private String href , author , title , date , num;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public noticeBoardModel() {}

    public noticeBoardModel(String href, String author, String title, String date ,String num) {
        this.href = href;
        this.author = author;
        this.title = title;
        this.date = date;
        this.num = num;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
