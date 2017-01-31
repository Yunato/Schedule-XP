package com.example.yukinaito.schedule_xp;

import java.io.Serializable;
import java.util.ArrayList;

public class ModelSchedule implements Serializable{
    private String name;
    private ArrayList<Card> cards;

    public ModelSchedule(){
        cards = new ArrayList<>();
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){return this.name;}

    public void setCardProperty(long calendar, int lentTime, String content, String place){
        Card card = new Card();
        card.setInfo(calendar, lentTime, content, place);
        this.cards.add(card);
    }

    public ArrayList<Card> getCards(){return this.cards;}
}
