package com.example.yukinaito.schedule_xp;

import android.app.Application;
import android.util.Log;
import android.util.LongSparseArray;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class SchedlueApplication extends Application {
    private ArrayList<ModelSchedule> model;
    private ArrayList<Card> plancards;
    private ArrayList<WantPlanCard> wantplancards;
    private ArrayList<HavetoPlanCard> havetoplancards;
    private ArrayList<EventCard> eventcards;
    private ArrayList<ModelSchedule> eventmodel;

    @Override
    public void onCreate() {
    }

    public void setModelSchedule(ArrayList<ModelSchedule> model) {
        this.model = model;
    }

    public ArrayList<ModelSchedule> getModelSchedule() {
        return this.model;
    }

    public ArrayList<Card> getPlanCard() {
        return this.plancards;
    }

    public ArrayList<WantPlanCard> getWantplancards() {
        return this.wantplancards;
    }

    public ArrayList<HavetoPlanCard> getHavetoplancards() {
        return this.havetoplancards;
    }

    public ArrayList<EventCard> getEventplancards() {
        return this.eventcards;
    }

    public void readFile() {
        readModelFile();
        readPlanFile();
        readWantPlanFile();
        readHavetoPlanFile();
        readEventPlanFile();
    }

    public void readModelFile() {
        model = new ArrayList<ModelSchedule>();
        String[] buffer = new String[4];
        String tmp;
        char c;
        try {
            FileInputStream in = openFileInput("default.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            while ((tmp = reader.readLine()) != null) {
                Arrays.fill(buffer, "");
                for (int i = 0, j = 0; i < tmp.length(); i++) {
                    c = tmp.charAt(i);
                    if (c == ' ') {
                        j++;
                        continue;
                    }
                    buffer[j] += c;
                }
                ModelSchedule modelSch = new ModelSchedule();
                modelSch.setName(buffer[2]);
                int count = Integer.parseInt(buffer[1]);
                for (int i = 0; i < count; i++) {
                    tmp = reader.readLine();
                    Arrays.fill(buffer, "");
                    for (int j = 0, k = 0; j < tmp.length(); j++) {
                        c = tmp.charAt(j);
                        if (c == ' ') {
                            k++;
                            continue;
                        }
                        buffer[k] += c;
                    }
                    modelSch.setCardproperty(Long.parseLong(buffer[0]),
                            Integer.parseInt(buffer[1]),
                            buffer[2], buffer[3]);
                }
                model.add(modelSch);
            }
            setModelSchedule(model);
        } catch (IOException e) {
        }
    }

    public void readPlanFile() {
        plancards = new ArrayList<Card>();
        String[] buffer = new String[5];
        String tmp;
        char c;
        try {
            FileInputStream in = openFileInput("plan.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            while ((tmp = reader.readLine()) != null) {
                Arrays.fill(buffer, "");
                for (int i = 0, j = 0; i < tmp.length(); i++) {
                    c = tmp.charAt(i);
                    if (c == ' ') {
                        j++;
                        continue;
                    }
                    buffer[j] += c;
                }
                Card card = new Card();
                card.setInfo(Long.parseLong(buffer[0]), Integer.parseInt(buffer[1]), buffer[2], buffer[3]);
                if(buffer[4].length() != 0)
                    card.setMemo(buffer[4]);
                plancards.add(card);
            }
        } catch (IOException e) {
        }
    }

    public void readWantPlanFile() {
        wantplancards = new ArrayList<WantPlanCard>();
        String[] buffer = new String[4];
        String tmp;
        char c;
        try {
            FileInputStream in = openFileInput("wantplan.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            while ((tmp = reader.readLine()) != null) {
                Arrays.fill(buffer, "");
                for (int i = 0, j = 0; i < tmp.length(); i++) {
                    c = tmp.charAt(i);
                    if (c == ' ') {
                        j++;
                        continue;
                    }
                    buffer[j] += c;
                }
                WantPlanCard card = new WantPlanCard();
                card.setInfo(buffer[0], Boolean.parseBoolean(buffer[1]), Integer.parseInt(buffer[2]), buffer[3]);
                wantplancards.add(card);
            }
        } catch (IOException e) {
        }
    }

    public void readHavetoPlanFile() {
        havetoplancards = new ArrayList<HavetoPlanCard>();
        String[] buffer = new String[6];
        String tmp;
        char c;
        try {
            FileInputStream in = openFileInput("havetoplan.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            while ((tmp = reader.readLine()) != null) {
                Arrays.fill(buffer, "");
                for (int i = 0, j = 0; i < tmp.length(); i++) {
                    c = tmp.charAt(i);
                    if (c == ' ') {
                        j++;
                        continue;
                    }
                    buffer[j] += c;
                }
                HavetoPlanCard card = new HavetoPlanCard();
                card.setInfo(buffer[0],
                        Boolean.parseBoolean(buffer[1]),
                        Long.parseLong(buffer[2]),
                        Long.parseLong(buffer[3]),
                        Integer.parseInt(buffer[4]),
                        buffer[5]);
                havetoplancards.add(card);
            }
        } catch (IOException e) {
        }
    }

    public void readEventPlanFile() {
        eventcards = new ArrayList<EventCard>();
        String[] buffer = new String[3];
        String[] buf = new String[7];
        String tmp;
        char c;
        try {
            FileInputStream in = openFileInput("eventplan.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            while ((tmp = reader.readLine()) != null) {
                Arrays.fill(buffer, "");
                for (int i = 0, j = 0; i < tmp.length(); i++) {
                    c = tmp.charAt(i);
                    if (c == ' ') {
                        j++;
                        continue;
                    }
                    buffer[j] += c;
                }
                EventCard eventCard = new EventCard();
                ArrayList<EventModelCard> eventModelCards = new ArrayList<EventModelCard>();
                eventCard.setInfo(Integer.parseInt(buffer[0]), Integer.parseInt(buffer[1]));
                int count = Integer.parseInt(buffer[2]);
                for(int i = 0; i < count ; i++) {
                    EventModelCard eventModelCard = new EventModelCard();
                    tmp = reader.readLine();
                    Arrays.fill(buf, "");
                    for(int j = 0, k = 0; j < tmp.length(); j++){
                        c = tmp.charAt(j);
                        if (c == ' '){
                            k++;
                            continue;
                        }
                        buf[k] += c;
                    }
                    if(buf[0].equals("true"))
                        eventModelCard.setmodelInfo(true, Integer.parseInt(buf[1]));
                    else {
                        Card card = new Card();
                        card.setInfo(Long.parseLong(buf[2]), Integer.parseInt(buf[3]), buf[4], buf[5]);
                        if(buf[6].length() != 0)
                            card.setMemo(buf[6]);
                        eventModelCard.setmodelInfo(false, Integer.parseInt(buf[1]), card);
                    }
                    eventModelCards.add(eventModelCard);
                }
                eventCard.setContent(eventModelCards);
                eventcards.add(eventCard);
            }
        } catch (IOException e) {
        }
    }

    public void writeModelFile() {
        String str = "";
        String buf = new String();
        this.deleteFile("default.txt");
        Format f = new DecimalFormat("0000");
        for (int i = 0; i < model.size(); i++) {
            buf = Integer.toString(i)
                    + " " + Integer.toString(model.get(i).getCards().size())
                    + " " + model.get(i).getName() + "\n";
            str += buf;
            for (int j = 0; j < model.get(i).getCards().size(); j++) {
                buf = f.format(model.get(i).getCards().get(j).getCalendar())
                        + " " + f.format(model.get(i).getCards().get(j).getLentime())
                        + " " + model.get(i).getCards().get(j).getContent()
                        + " " + model.get(i).getCards().get(j).getPlace() + "\n";
                str += buf;
            }
        }
        try {
            FileOutputStream out = this.openFileOutput("default.txt", this.MODE_APPEND | this.MODE_WORLD_READABLE);
            out.write(str.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writePlanFile() {
        String str = "";
        String buf = new String();
        this.deleteFile("plan.txt");
        Format f1 = new DecimalFormat("000000000000");
        Format f2 = new DecimalFormat("0000");
        for (int i = 0; i < plancards.size(); i++) {
            buf = f1.format(plancards.get(i).getCalendar())
                    + " " + f2.format(plancards.get(i).getLentime())
                    + " " + plancards.get(i).getContent()
                    + " " + plancards.get(i).getPlace();
            str += buf;
            if(plancards.get(i).getMemo() != null && plancards.get(i).getMemo().length() != 0) {
                buf = " " + plancards.get(i).getMemo();
                str += buf;
            }
            str += "\n";
        }

        try {
            FileOutputStream out = this.openFileOutput("plan.txt", this.MODE_APPEND | this.MODE_WORLD_READABLE);
            out.write(str.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeWantPlanFile() {
        String str = "";
        String buf = new String();
        this.deleteFile("wantplan.txt");
        for (int i = 0; i < wantplancards.size(); i++) {
            buf = wantplancards.get(i).getName()
                    + " " + Boolean.toString(wantplancards.get(i).getWant())
                    + " " + Integer.toString(wantplancards.get(i).getHow())
                    + " " + wantplancards.get(i).getPlace() + "\n";
            str += buf;
        }
        try {
            FileOutputStream out = this.openFileOutput("wantplan.txt", this.MODE_APPEND | this.MODE_WORLD_READABLE);
            out.write(str.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeHavetoPlanFile() {
        String str = "";
        String buf = new String();
        this.deleteFile("havetoplan.txt");
        Format f1 = new DecimalFormat("000000000000");
        Format f2 = new DecimalFormat("0000");
        Collections.sort(havetoplancards, new CardComparator1());
        for (int i = 0; i < havetoplancards.size(); i++) {
            buf = havetoplancards.get(i).getName()
                    + " " + Boolean.toString(havetoplancards.get(i).getHaveto())
                    + " " + f1.format(havetoplancards.get(i).getStart())
                    + " " + f1.format(havetoplancards.get(i).getLimit())
                    + " " + f2.format(havetoplancards.get(i).getForcast())
                    + " " + havetoplancards.get(i).getPlace() + "\n";
            str += buf;
        }
        try {
            FileOutputStream out = this.openFileOutput("havetoplan.txt", this.MODE_APPEND | this.MODE_WORLD_READABLE);
            out.write(str.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeEventPlanFile() {
        String str = "";
        String buf = new String();
        this.deleteFile("eventplan.txt");
        Format f = new DecimalFormat("00000000");
        Collections.sort(eventcards, new CardComparator2());
        for (int i = 0; i < eventcards.size(); i++) {
            buf = f.format(eventcards.get(i).getDate())
                    + " " + eventcards.get(i).getIndex();
            str += buf;
            if(eventcards.get(i).getCards() != null) {
                buf = " " + eventcards.get(i).getCards().size() + "\n";
            }else{
                buf = " 0\n";
            }
            str += buf;
            if(eventcards.get(i).getCards() != null) {
                int count = eventcards.get(i).getCards().size();
                for (int j = 0; j < count; j++) {
                    buf = Boolean.toString(eventcards.get(i).getCards().get(j).getUpdate())
                            + " " + Integer.toString(eventcards.get(i).getCards().get(j).getIndex());
                    str += buf;
                    if(eventcards.get(i).getCards().get(j).getCard() != null) {
                        buf = " " + Long.toString(eventcards.get(i).getCards().get(j).getCardCalendar())
                                + " " + Integer.toString(eventcards.get(i).getCards().get(j).getCardLentime())
                                + " " + eventcards.get(i).getCards().get(j).getCardPlace()
                                + " " + eventcards.get(i).getCards().get(j).getCardContent();
                        str += buf;
                    }
                    if (eventcards.get(i).getCards().get(j).getCardMemo() != null && eventcards.get(i).getCards().get(j).getCardMemo().length() != 0) {
                        buf = " " + eventcards.get(i).getCards().get(j).getCardMemo();
                        str += buf;
                    }
                    str += "\n";
                }
            }
        }
        try {
            FileOutputStream out = this.openFileOutput("eventplan.txt", this.MODE_APPEND | this.MODE_WORLD_READABLE);
            out.write(str.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class CardComparator1 implements Comparator<HavetoPlanCard> {
        public int compare(HavetoPlanCard s, HavetoPlanCard t) {
            long calendar1 = s.getLimit();
            long calendar2 = t.getLimit();
            if (calendar1 > calendar2)
                return 1;
            else if (calendar1 == calendar2)
                return 0;
            else
                return -1;
        }
    }

    public class CardComparator2 implements Comparator<EventCard> {
        public int compare(EventCard s, EventCard t) {
            long calendar1 = s.getDate();
            long calendar2 = t.getDate();
            if (calendar1 > calendar2)
                return 1;
            else if (calendar1 == calendar2)
                return 0;
            else
                return -1;
        }
    }
}
