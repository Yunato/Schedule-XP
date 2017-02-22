package com.example.yukinaito.schedule_xp;

import java.io.Serializable;

public class EventPlanCard implements Serializable , Cloneable{
    private int date;
    private int modelIndex;
    private String title;

    public void setInfo(int date, int modelIndex){
        this.date = date;
        this.modelIndex = modelIndex;
    }

    public void setIndex(int index){
        this.modelIndex = index;
    }

    public int getDate(){
        return this.date;
    }

    public int getIndex(){
        return this.modelIndex;
    }

    public String getTitle(){
        return this.title;
    }
}
