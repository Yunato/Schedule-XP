package com.example.yukinaito.schedule_xp;

import android.graphics.PorterDuff;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class ModelSchedule implements Serializable{
    private String name;
    private ArrayList<Card> cards;

    public class Card implements Serializable{
        public Calendar calendar;
        public int lentime;
        public String place;
        public String content;
    }

    public ModelSchedule(){
        cards = new ArrayList<Card>();
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){return this.name;}

    public void setCardproperty(int time, int lentime, String place, String content){
        Card card = new Card();
        card.calendar = Calendar.getInstance();
        card.calendar.set(Calendar.HOUR_OF_DAY, time/100);
        card.calendar.set(Calendar.MINUTE, time%100);
        card.lentime = lentime;
        card.place = place;
        card.content = content;
        this.cards.add(card);
    }

    public ArrayList<Card> getCards(){return this.cards;}
}
