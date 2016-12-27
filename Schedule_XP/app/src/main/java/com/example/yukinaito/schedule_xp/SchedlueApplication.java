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

public class SchedlueApplication extends Application {
    private ArrayList<ModelSchedule> model;
    private ArrayList<Card> plancards;
    static public final String DATE_PATTERN = "HHmm";

    @Override
    public void onCreate(){
    }

    public void setModelSchedule(ArrayList<ModelSchedule> model){
        this.model = model;
    }

    public ArrayList<ModelSchedule> getModelSchedule(){
        return this.model;
    }

    public void readFile(){
        readModelFile();
        readPlanFile();
        for(int i = 0; i < plancards.size(); i++)
            Log.d("test",(new SimpleDateFormat("yyyyMMddHHmm")).format(plancards.get(i).getCalendar().getTime()) + " " + Integer.toString(plancards.get(i).getLentime()) + " " + plancards.get(i).getContent() + " " + plancards.get(i).getPlace());
    }

    public void readModelFile(){
        model = new ArrayList<ModelSchedule>();
        String[] buffer = new String[4];
        String tmp;
        char c;
        try{
            FileInputStream in = openFileInput("default.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            while((tmp = reader.readLine())!=null) {
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
                    for(int j = 0, k = 0; j < tmp.length(); j++){
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
        }catch(IOException e){
        }
    }

    public void readPlanFile(){
        plancards = new ArrayList<Card>();
        String[] buffer = new String[4];
        String tmp;
        char c;
        try{
            FileInputStream in = openFileInput("plan.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            while((tmp = reader.readLine())!=null) {
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
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, Integer.parseInt(buffer[0].substring(0,4)));
                calendar.set(Calendar.MONTH, Integer.parseInt(buffer[0].substring(4,6)));
                calendar.set(Calendar.DATE, Integer.parseInt(buffer[0].substring(6,8)));
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(buffer[0].substring(8,10)));
                calendar.set(Calendar.MINUTE, Integer.parseInt(buffer[0].substring(10,12)));
                card.setInfo(calendar, Integer.parseInt(buffer[1]), buffer[2], buffer[3]);
                plancards.add(card);
            }
        }catch(IOException e){
        }
    }

    public void writeModelFile(){
        String str = "";
        String buf = new String();
        this.deleteFile("default.txt");
        Format f = new DecimalFormat("0000");
        for(int i = 0; i < model.size(); i++) {
            buf = Integer.toString(i)
                    + " " + Integer.toString(model.get(i).getCards().size())
                    + " " + model.get(i).getName() + "\n";
            str += buf;
            for(int j = 0; j < model.get(i).getCards().size(); j++){
                buf = String.format(convertDate2String(model.get(i).getCards().get(j).getCalendar().getTime())
                        + " " + f.format(model.get(i).getCards().get(j).getLentime()))
                        + " " + model.get(i).getCards().get(j).getContent()
                        + " " + model.get(i).getCards().get(j).getPlace() + "\n";
                str += buf;
            }
        }
        try{
            Log.d("test",str);
            FileOutputStream out = this.openFileOutput("default.txt",this.MODE_APPEND|this.MODE_WORLD_READABLE);
            out.write(str.getBytes());
        }catch(IOException e){
            Log.d("test","BAD");
            e.printStackTrace();
        }
    }

    public String convertDate2String(java.util.Date date) {
        return (new SimpleDateFormat(DATE_PATTERN)).format(date);
    }
}
