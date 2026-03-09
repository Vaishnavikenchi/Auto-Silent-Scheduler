package com.example.autosilentscheduler;

public class ScheduleModel {
    private String dateTime;
    private int id;

    public ScheduleModel(int id, String dateTime) {
        this.id = id;
        this.dateTime = dateTime;
    }
    public String getDateTime() { return dateTime; }
    public int getId() { return id; }
}
