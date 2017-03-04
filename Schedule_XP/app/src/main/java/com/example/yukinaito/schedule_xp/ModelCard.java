package com.example.yukinaito.schedule_xp;

public class ModelCard {
    private String id;
    private String title;
    private boolean saved;

    public ModelCard(String id, String title, boolean saved){
        this.id = id;
        this.title = title;
        this.saved = saved;
    }

    public ModelCard setId(Long id){
        this.id = Long.toString(id);
        return this;
    }

    public void setSaved(boolean saved){
        this.saved = saved;
    }

    public String getId(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }

    public boolean getSaved(){
        return  this.saved;
    }
}
