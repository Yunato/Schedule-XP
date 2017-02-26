package com.example.yukinaito.schedule_xp;

import java.io.Serializable;

public class MustPlanCard implements Serializable {
    String content;
    boolean active;
    int limitDate;
    int limitTime;
    String place;
    String memo;

    public MustPlanCard(String content, boolean active, int limitDate, int limitTime, String place){
        this.content = content;
        this.active = active;
        this.limitDate = limitDate;
        this.limitTime = limitTime;
        this.place = place;
    }

    public void setName(String name){
        this.content = name;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public void setMemo(String memo){
        this.memo = memo;
    }

    public String getContent(){return this.content;}

    public boolean getActive(){return this.active;}

    public int getLimitDate(){return this.limitDate;}

    public int getLimitTime(){return this.limitTime;}

    public String getPlace(){return this.place;}

    public String getMemo(){
        return this.memo;
    }
}