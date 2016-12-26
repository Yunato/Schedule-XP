package com.example.yukinaito.schedule_xp;

import android.app.Application;

import java.util.ArrayList;

public class SchedlueApplication extends Application {
    private ArrayList<ModelSchedule> model;

    @Override
    public void onCreate(){
    }

    public void setModelSchedule(ArrayList<ModelSchedule> model){
        this.model = model;
    }

    public ArrayList<ModelSchedule> getModelSchedule(){
        return this.model;
    }
}
