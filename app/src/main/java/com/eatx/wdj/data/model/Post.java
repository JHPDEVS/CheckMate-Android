package com.eatx.wdj.data.model;

public class Post {
    private String id, password,subject,content,writer,wdate , type ;
    private int bno;

    public Post() {}


    public Post (String type,String id, String password, String subject, String content, String writer , String wdate, int bno) {

    this.id = id;
    this.type = type;
    this.password = password;
    this.subject = subject;
    this.content = content;
    this.writer = writer;
    this.wdate = wdate;
    this.bno = bno;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getWdate() {
        return wdate;
    }

    public void setWdate(String wdate) {
        this.wdate = wdate;
    }

    public int getBno() {
        return bno;
    }

    public void setBno(int bno) {
        this.bno = bno;
    }
}