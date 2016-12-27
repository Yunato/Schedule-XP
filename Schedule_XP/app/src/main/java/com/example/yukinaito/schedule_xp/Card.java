package com.example.yukinaito.schedule_xp;

import java.io.Serializable;
import java.util.Calendar;

public class Card implements Serializable {
    private Calendar calendar;
    private int lentime;
    private String place;
    private String content;

    public void setInfo(int time, int lentime, String content, String place){
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, time/100);
        calendar.set(Calendar.MINUTE, time%100);
        this.lentime = lentime;
        this.content = content;
        this.place = place;
    }

    public void setInfo(Calendar calendar, int lentime, String content, String place){
        this.calendar = calendar;
        this.lentime = lentime;
        this.content = content;
        this.place = place;
    }

    public Calendar getCalendar(){return this.calendar;}

    public int getLentime(){return this.lentime;}

    public String getPlace(){return this.place;}

    public String getContent(){return this.content;}

    public void setUpdate(Card card){
        this.calendar = card.getCalendar();
        this.lentime = card.getLentime();
        this.content = card.getContent();
        this.place = card.getPlace();
    }
}