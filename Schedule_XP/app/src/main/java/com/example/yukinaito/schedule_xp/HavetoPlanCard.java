package com.example.yukinaito.schedule_xp;

import java.io.Serializable;
import java.util.Calendar;

public class HavetoPlanCard implements Serializable {
    String name;
    boolean haveto;
    Calendar start;
    Calendar limit;
    int forcast;
    String place;

    public void setInfo(String name, boolean haveto, Calendar start, Calendar limit, int forcast, String place){
        this.name = name;
        this.haveto = haveto;
        this.start = start;
        this.limit = limit;
        this.forcast = forcast;
        this.place = place;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setWant(boolean want){
        this.haveto = want;
    }

    public String getName(){return this.name;}

    public boolean getHaveto(){return this.haveto;}

    public Calendar getStart(){return this.start;}

    public Calendar getLimit(){return this.limit;}

    public int getForcast(){return this.forcast;}

    public String getPlace(){return this.place;}

    public void setUpdate(HavetoPlanCard card){
        this.name = card.getName();
        this.haveto = card.getHaveto();
        this.start = card.getStart();
        this.limit = card.getLimit();
        this.forcast = card.getForcast();
    }
}