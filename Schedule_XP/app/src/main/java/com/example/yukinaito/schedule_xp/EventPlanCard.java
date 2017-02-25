package com.example.yukinaito.schedule_xp;

import java.io.Serializable;

public class EventPlanCard implements Serializable , Cloneable{
    private int date;
    private String title;
    private int modelIndex;

    public EventPlanCard(int date, String title, int modelIndex){
        this.date = date;
        this.title = title;
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
