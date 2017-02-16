package com.example.yukinaito.schedule_xp;

import android.app.Application;
import android.database.Cursor;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class ScheduleApplication extends Application {
    static DBAdapter dbAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
        dbAdapter = new DBAdapter(this);
    }

    public void saveCard(Card card){
        dbAdapter.open();
        dbAdapter.saveWord(card);
        dbAdapter.close();
    }

    public void updateCard(String wordId, Card card){
        dbAdapter.open();
        dbAdapter.updateWord(wordId, card);
        dbAdapter.close();
    }

    //テスト(単語情報をDBから削除)
    public void deleteCard(String id){
        dbAdapter.open();
        dbAdapter.deleteWord(id);
        dbAdapter.close();
    }

    //テスト用(分類のみ出力) 成功
    public void Test_getCards(String getSearch, String[] field){
        dbAdapter.open();
        Cursor cursor = dbAdapter.getPlanInfo(getSearch, field);
        Log.d("SQLApp getWordClass()","Size " + Integer.toString(cursor.getCount()));

        Card[] data = new Card[cursor.getCount()];
        int i = 0;
        while(cursor.moveToNext()){
            Log.d("SQLApp 出力結果ですよ", cursor.getString(cursor.getColumnIndex("dateIndex"))
                    +  cursor.getString(cursor.getColumnIndex("start"))
                    +  cursor.getString(cursor.getColumnIndex("overTime"))
                    +  cursor.getString(cursor.getColumnIndex("connect"))
                    +  cursor.getString(cursor.getColumnIndex("content"))
                    +  cursor.getString(cursor.getColumnIndex("place"))
                    +  cursor.getString(cursor.getColumnIndex("memo")));
        }
        cursor.close();
        dbAdapter.close();
    }
}
