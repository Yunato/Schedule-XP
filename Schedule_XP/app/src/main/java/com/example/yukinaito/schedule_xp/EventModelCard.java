package com.example.yukinaito.schedule_xp;

import java.io.Serializable;

public class EventModelCard implements Serializable {
    boolean visible;
    int index;
    Card card;

    EventModelCard(){
        this.card = new Card();
    }

    public void setModelInfo(boolean visible, int index, Card card){
        this.visible = visible;
        this.index = index;
        this.card = card;
    }

    public void setVisible(boolean update){
        this.visible = update;
    }

    public void setCard(Card card){
        this.card = card;
    }

    public boolean getVisible(){
        return this.visible;
    }

    public int getIndex(){
        return this.index;
    }

    public Card getCard(){
        return this.card;
    }

    public long getCardCalendar(){return card.getCalendar();}

    public int getCardLenTime(){return card.getLenTime();}

    public String getCardPlace(){return card.getPlace();}

    public String getCardContent(){return card.getContent();}

    public String getCardMemo(){return card.getMemo();}

    public boolean equalsCard(Card card){
        return this.card.equals(card);
    }
}
