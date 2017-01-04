package com.example.yukinaito.schedule_xp;

import java.io.Serializable;
import java.util.Calendar;

public class Card implements Serializable {
    private long calendar;
    private int lentime;
    private String place;
    private String content;
    private String memo;

    public void setInfo(long calendar, int lentime, String content, String place){
        this.calendar = calendar;
        this.lentime = lentime;
        this.content = content;
        this.place = place;
    }

    public void setMemo(String memo){
        this.memo = memo;
    }

    public long getCalendar(){return this.calendar;}

    public int getLentime(){return this.lentime;}

    public String getPlace(){return this.place;}

    public String getContent(){return this.content;}

    public String getMemo(){return this.memo;}

    public Card getCard(){
        return this;
    }

    public void setUpdate(Card card){
        this.calendar = card.getCalendar();
        this.lentime = card.getLentime();
        this.content = card.getContent();
        this.place = card.getPlace();
    }
}