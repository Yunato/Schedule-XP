package com.example.yukinaito.schedule_xp;

import java.io.Serializable;

public class WantPlanCard implements Serializable {
    String id;
    String content;
    boolean active;
    int ratio;
    String place;
    int which;
    int sum;

    public WantPlanCard(String content, boolean active, int ratio, String place, int which){
        this.content = content;
        this.active = active;
        this.ratio = ratio;
        this.place = place;
        this.which = which;
        this.sum = 0;
    }

    public WantPlanCard(String id, String content, boolean active, int ratio, String place, int which, int sum){
        this.id = id;
        this.content = content;
        this.active = active;
        this.ratio = ratio;
        this.place = place;
        this.which = which;
        this.sum = sum;
    }

    public WantPlanCard setId(long id){
        this.id = Long.toString(id);
        return this;
    }

    public void setContent(String name){
        this.content = name;
    }

    public void setActive(boolean want){
        this.active = want;
    }

    public void setRatio(int ratio){
        this.ratio = ratio;
    }

    public void addSum(int count){
        this.sum += count;
    }

    public String getId(){
        return this.id;
    }

    public String getContent(){return this.content;}

    public boolean getActive(){return this.active;}

    public int getRatio(){return this.ratio;}

    public String getPlace(){return this.place;}

    public int getWhich(){
        return this.which;
    }

    public int getSum(){
        return this.sum;
    }
}

