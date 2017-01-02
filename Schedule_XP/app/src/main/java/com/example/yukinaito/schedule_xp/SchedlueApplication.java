package com.example.yukinaito.schedule_xp;

import android.app.Application;
import android.util.Log;

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
                    modelSch.setCardproperty(Integer.parseInt(buffer[0]),
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
        String[] buffer = new String[4];
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
                card.setInfo(convertCalendar(buffer[0]), Integer.parseInt(buffer[1]), buffer[2], buffer[3]);
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
                        convertCalendar(buffer[2]),
                        convertCalendar(buffer[3]),
                        Integer.parseInt(buffer[4]),
                        buffer[5]);
                havetoplancards.add(card);
            }
        } catch (IOException e) {
        }
    }

    public void readEventPlanFile() {
        eventcards = new ArrayList<EventCard>();
        String[] buffer = new String[2];
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
                EventCard card = new EventCard();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, Integer.parseInt(buffer[0].substring(0, 4)));
                calendar.set(Calendar.MONTH, Integer.parseInt(buffer[0].substring(4, 6)) - 1);
                calendar.set(Calendar.DATE, Integer.parseInt(buffer[0].substring(6, 8)));
                card.setInfo(calendar, Integer.parseInt(buffer[1]));
                eventcards.add(card);
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
            //Collections.sort(model.get(i).getCards(), new CardComparator1());
            for (int j = 0; j < model.get(i).getCards().size(); j++) {
                buf = (new SimpleDateFormat("HHmm")).format(model.get(i).getCards().get(j).getCalendar().getTime())
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
        Format f = new DecimalFormat("0000");
        //Collections.sort(plancards, new CardComparator1());
        for (int i = 0; i < plancards.size(); i++) {
            buf = (new SimpleDateFormat("yyyyMMddHHmm")).format(plancards.get(i).getCalendar().getTime())
                    + " " + f.format(plancards.get(i).getLentime())
                    + " " + plancards.get(i).getContent()
                    + " " + plancards.get(i).getPlace() + "\n";
            str += buf;
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
        Format f = new DecimalFormat("0000");
        Collections.sort(havetoplancards, new CardComparator2());
        for (int i = 0; i < havetoplancards.size(); i++) {
            buf = havetoplancards.get(i).getName()
                    + " " + Boolean.toString(havetoplancards.get(i).getHaveto())
                    + " " + (new SimpleDateFormat("yyyyMMddHHmm")).format(havetoplancards.get(i).getStart().getTime())
                    + " " + (new SimpleDateFormat("yyyyMMddHHmm")).format(havetoplancards.get(i).getLimit().getTime())
                    + " " + f.format(havetoplancards.get(i).getForcast())
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
        Format f = new DecimalFormat("0000");
        Collections.sort(eventcards, new CardComparator3());
        for (int i = 0; i < eventcards.size(); i++) {
            buf = (new SimpleDateFormat("yyyyMMdd")).format(eventcards.get(i).getDate().getTime())
                    + " " + eventcards.get(i).getIndex() + "\n";
            str += buf;
        }

        try {
            FileOutputStream out = this.openFileOutput("eventplan.txt", this.MODE_APPEND | this.MODE_WORLD_READABLE);
            out.write(str.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Calendar convertCalendar(String buffer) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(buffer.substring(0, 4)));
        calendar.set(Calendar.MONTH, Integer.parseInt(buffer.substring(4, 6)) - 1);
        calendar.set(Calendar.DATE, Integer.parseInt(buffer.substring(6, 8)));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(buffer.substring(8, 10)));
        calendar.set(Calendar.MINUTE, Integer.parseInt(buffer.substring(10, 12)));
        return calendar;
    }

    public class CardComparator1 implements Comparator<Card> {
        public int compare(Card s, Card t) {
            Calendar calendar1 = s.getCalendar();
            Calendar calendar2 = t.getCalendar();
            int diff = calendar1.compareTo(calendar2);
            if (diff > 0)
                return 1;
            else if (diff == 0)
                return 0;
            else
                return -1;
        }
    }

    public class CardComparator2 implements Comparator<HavetoPlanCard> {
        public int compare(HavetoPlanCard s, HavetoPlanCard t) {
            Calendar calendar1 = s.getLimit();
            Calendar calendar2 = t.getLimit();
            int diff = calendar1.compareTo(calendar2);
            if (diff > 0)
                return 1;
            else if (diff == 0)
                return 0;
            else
                return -1;
        }
    }

    public class CardComparator3 implements Comparator<EventCard> {
        public int compare(EventCard s, EventCard t) {
            Calendar calendar1 = s.getDate();
            Calendar calendar2 = t.getDate();
            int diff = calendar1.compareTo(calendar2);
            if (diff > 0)
                return 1;
            else if (diff == 0)
                return 0;
            else
                return -1;
        }
    }
}
