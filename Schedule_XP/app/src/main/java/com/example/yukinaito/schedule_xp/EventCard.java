package com.example.yukinaito.schedule_xp;

import java.io.Serializable;
import java.util.ArrayList;

public class EventCard implements Serializable , Cloneable{
    int date;
    int index;
    ArrayList<EventModelCard> cards;

    EventCard(){
        cards = new ArrayList<>();
    }

    public void setInfo(int date, int index){
        this.date = date;
        this.index = index;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public int getDate(){
        return this.date;
    }

    public int getIndex(){
        return this.index;
    }

    public ArrayList<EventModelCard> getCards(){
        return this.cards;
    }

    public void setContent(ArrayList<EventModelCard> cards){
        this.cards = cards;
    }

    public void deepCopy(ArrayList<EventModelCard> copy){
        for(int i = 0; i < cards.size() ; i++){
            cards.add(i, copy.get(i));
        }
    }
}
