package com.example.yukinaito.schedule_xp;

import java.io.Serializable;

public class EventModelCard implements Serializable {
    boolean update;
    int index;
    Card card;

    EventModelCard(){
        this.card = new Card();
    }

    public void setmodelInfo(boolean update, int index){
        this.update = update;
        this.index = index;
    }

    public void setmodelInfo(boolean update, int index, Card card){
        this.update = update;
        this.index = index;
        this.card = card;
    }

    public void setUpdate(boolean update){
        this.update = update;
    }

    public void setCard(Card card){
        this.card = card;
    }

    public boolean getUpdate(){
        return this.update;
    }

    public int getIndex(){
        return this.index;
    }

    public Card getCard(){
        return this.card;
    }

    public long getCardCalendar(){return card.getCalendar();}

    public int getCardLentime(){return card.getLentime();}

    public String getCardPlace(){return card.getPlace();}

    public String getCardContent(){return card.getContent();}

    public String getCardMemo(){return card.getMemo();}
}
