package com.example.robocuttercontroller;

import android.widget.EditText;

public class Operator {

    int id;
    String name;
    String time;
    String date;

    public Operator(int id, String name, String time, String date) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }
}
