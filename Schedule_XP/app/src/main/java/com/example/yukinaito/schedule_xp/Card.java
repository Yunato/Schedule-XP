package com.example.yukinaito.schedule_xp;

import java.io.Serializable;

public class Card implements Serializable {
    private long calendar;
    private int lenTime;
    private String place;
    private String content;
    private String memo;

    public void setInfo(long calendar, int lenTime, String content, String place){
        this.calendar = calendar;
        this.lenTime = lenTime;
        this.content = content;
        this.place = place;
    }

    public void setMemo(String memo){
        this.memo = memo;
    }

    public long getCalendar(){return this.calendar;}

    public int getLenTime(){return this.lenTime;}

    public String getPlace(){return this.place;}

    public String getContent(){return this.content;}

    public String getMemo(){return this.memo;}

    public Card getCard(){
        return this;
    }

    public boolean equals(Card card){
        if(this.calendar == card.getCalendar() &&
                this.lenTime == card.getLenTime() &&
                this.place.equals(card.getPlace()) &&
                this.content.equals(card.getContent()))
            return true;
        return false;
    }
}