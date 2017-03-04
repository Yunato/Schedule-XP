package com.example.yukinaito.schedule_xp;

import java.io.Serializable;

public class EventPlanCard implements Serializable , Cloneable{
    private String id;
    private int date;
    private String title;
    private int modelIndex;

    public EventPlanCard(int date, String title, int modelIndex){
        this.date = date;
        this.title = title;
        this.modelIndex = modelIndex;
    }

    public EventPlanCard(String id, int date, String title, int modelIndex){
        this.id = id;
        this.date = date;
        this.title = title;
        this.modelIndex = modelIndex;
    }

    public EventPlanCard setId(long id){
        this.id = Long.toString(id);
        return this;
    }

    public String getId(){
        return this.id;
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
