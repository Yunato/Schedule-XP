package com.example.yukinaito.schedule_xp;

import android.graphics.PorterDuff;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class ModelSchedule implements Serializable{
    private String name;
    private ArrayList<Card> cards;

    public ModelSchedule(){
        cards = new ArrayList<Card>();
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){return this.name;}

    public void setCardproperty(int time, int lentime, String content, String place){
        Card card = new Card();
        card.setInfo(time, lentime, content, place);
        this.cards.add(card);
    }

    public ArrayList<Card> getCards(){return this.cards;}
}
