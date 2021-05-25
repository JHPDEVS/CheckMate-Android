package com.eatx.wdj.data.model;

public class TimeTableModel {
    private String classTitle, classPlace , professorName , classValue;
    private int day , startTime , endTime ,hour , minute;

    public TimeTableModel() {

    }
    public TimeTableModel(String classTitle, String classPlace, String professorName, int day, int startTime, int endTime, int hour, int minute, String classValue) {
        this.classTitle = classTitle;
        this.classPlace = classPlace;
        this.professorName = professorName;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hour = hour;
        this.minute = minute;
        this.classValue = classValue;
    }

    public String getClassTitle() {
        return classTitle;
    }

    public void setClassTitle(String classTitle) {
        this.classTitle = classTitle;
    }

    public String getClassPlace() {
        return classPlace;
    }

    public void setClassPlace(String classPlace) {
        this.classPlace = classPlace;
    }

    public String getProfessorName() {
        return professorName;
    }

    public String getClassValue() {
        return classValue;
    }

    public void setClassValue(String classValue) {
        this.classValue = classValue;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
