package com.example.yukinaito.schedule_xp;

import java.io.Serializable;

public class WantPlanCard implements Serializable {
    String content;
    boolean active;
    int ratio;
    String place;

    public WantPlanCard(String content, boolean active, int ratio, String place){
        this.content = content;
        this.active = active;
        this.ratio = ratio;
        this.place = place;
    }

    public void setContent(String name){
        this.content = name;
    }

    public void setActive(boolean want){
        this.active = want;
    }

    public String getContent(){return this.content;}

    public boolean getActive(){return this.active;}

    public int getRatio(){return this.ratio;}

    public String getPlace(){return this.place;}
}

