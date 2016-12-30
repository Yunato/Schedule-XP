package com.example.yukinaito.schedule_xp;

import java.io.Serializable;
import java.util.Calendar;

public class EventCard implements Serializable {
    Calendar date;
    int index;

    public void setInfo(Calendar date, int index){
        this.date = date;
        this.index = index;
    }

    public Calendar getDate(){
        return this.date;
    }

    public int getIndex(){
        return this.index;
    }

    public void setUpdate(EventCard card){
        this.date = card.getDate();
        this.index = card.getIndex();
    }
}
