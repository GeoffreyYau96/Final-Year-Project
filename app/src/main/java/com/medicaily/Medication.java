package com.medicaily;

public class Medication {
    private String name;
    private String type;
    private String dosage;
    private String interval;
    private String time;
    private String instruction;
    private String start;
    private String end;
    private String remain;
    private String userId;


    public Medication() {
        //empty constructor
    }

    public Medication(String name, String type, String dosage, String interval, String time,
                      String instruction, String start_from, String end, String remain,
                      String userId) {

        this.name = name;
        this.type = type;
        this.dosage = dosage;
        this.interval = interval;
        this.time = time;
        this.instruction = instruction;
        this.start = start_from;
        this.end = end;
        this.remain = remain;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDosage() {
        return dosage;
    }

    public String getInterval() {
        return interval;
    }

    public String getTime() {
        return time;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getRemain() {
        return remain;
    }

    public String getUserId() {
        return userId;
    }

}

