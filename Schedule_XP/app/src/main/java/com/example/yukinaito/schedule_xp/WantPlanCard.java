package com.example.yukinaito.schedule_xp;

import java.io.Serializable;

public class WantPlanCard implements Serializable {
    String name;
    boolean want;
    int how;
    String place;

    public void setInfo(String name, boolean want, int how, String place){
        this.name = name;
        this.want = want;
        this.how = how;
        this.place = place;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setWant(boolean want){
        this.want = want;
    }

    public String getName(){return this.name;}

    public boolean getWant(){return this.want;}

    public int getHow(){return this.how;}

    public String getPlace(){return this.place;}
}

