package com.example.yukinaito.schedule_xp;

import java.io.Serializable;

public class MustPlanCard implements Serializable {
    String name;
    boolean must;
    long start;
    long limit;
    int forCast;
    String place;

    public void setInfo(String name, boolean must, long start, long limit, int forCast, String place){
        this.name = name;
        this.must = must;
        this.start = start;
        this.limit = limit;
        this.forCast = forCast;
        this.place = place;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setWant(boolean want){
        this.must = want;
    }

    public String getName(){return this.name;}

    public boolean getMust(){return this.must;}

    public long getStart(){return this.start;}

    public long getLimit(){return this.limit;}

    public int getForCast(){return this.forCast;}

    public String getPlace(){return this.place;}
}