package com.medicaily;


public class Appointment {
    private String title;
    private String date;
    private String time;
    private String location;
    private String contact_number;
    private String reminder;
    private String note;
    private String userId;


    public Appointment (){
        //empty constructor
    }

    public Appointment (String title, String date,String time, String location,
                        String contact_number, String reminder ,String note, String userId) {

        this.title = title;
        this.date = date;
        this.time = time;
        this.location = location;
        this.contact_number = contact_number;
        this.reminder = reminder;
        this.note = note;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getContact_number() {
        return contact_number;
    }

    public String getReminder() {
        return reminder;
    }

    public String getNote() {
        return note;
    }

    public String getUserId() {
        return userId;
    }
}
