package com.example.yukinaito.schedule_xp;

import android.app.usage.UsageEvents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class EventCard implements Serializable , Cloneable{
    int date;
    int index;
    ArrayList<EventModelCard> cards;

    EventCard(){
        cards = null;
    }

    public void setInfo(int date, int index){
        this.date = date;
        this.index = index;
    }

    public int getDate(){
        return this.date;
    }

    public int getIndex(){
        return this.index;
    }

    public ArrayList<EventModelCard> getCards(){
        return this.cards;
    }

    public void setContent(ArrayList<EventModelCard> cards){
        this.cards = cards;
    }
}
