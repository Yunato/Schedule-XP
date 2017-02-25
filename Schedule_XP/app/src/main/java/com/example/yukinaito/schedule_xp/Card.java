package com.example.yukinaito.schedule_xp;

import java.io.Serializable;

public class Card implements Serializable {
    private int date;
    private int startTime;
    private int overTime;
    private boolean connect;
    private String content;
    private String place;
    private String memo;

    public Card(int date, int startTime, int overTime, boolean connect, String content, String place){
        this.date = date;
        this.startTime = startTime;
        this.overTime = overTime;
        this.connect = connect;
        this.content = content;
        this.place = place;
        this.memo = null;
    }

    public void setConnect(boolean connect){
        this.connect = connect;
    }

    public void setMemo(String memo){
        this.memo = memo;
    }

    public int getDate(){return this.date;}

    public int getStartTime(){return this.startTime;}

    public int getOverTime(){return this.overTime;}

    public boolean getConnect(){return this.connect;}

    public String getPlace(){return this.place;}

    public String getContent(){return this.content;}

    public String getMemo(){return this.memo;}

    public Card getCard(){
        return this;
    }

    public boolean equals(Card card){
        if(this.date == card.getDate() &&
                this.startTime == card.getStartTime() &&
                this.overTime == card.getOverTime() &&
                this.connect == card.getConnect() &&
                this.place.equals(card.getPlace()) &&
                this.content.equals(card.getContent()))
            return true;
        return false;
    }
}