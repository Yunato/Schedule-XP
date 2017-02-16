package com.example.yukinaito.schedule_xp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class DBAdapter {
    static final String DATABASE_NAME = Environment.getExternalStorageDirectory() + "/Schedule_XP.db";
    static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "cards";
    public static final String WORD_ID = "_id";
    public static final String WORD_DATEINDEX = "dateIndex";
    public static final String WORD_START = "start";
    public static final String WORD_OVERTIME = "overTime";
    public static final String WORD_CONNECT = "connect";
    public static final String WORD_CONTENT = "content";
    public static final String WORD_PLACE = "place";
    public static final String WORD_MEMO = "memo";

    protected final Context context;
    protected DatabaseHelper dbHelper;
    protected SQLiteDatabase db;

    public DBAdapter(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(this.context);
    }

    public class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(
                    "CREATE TABLE " + TABLE_NAME + " ("
                            + WORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + WORD_DATEINDEX + " ,"
                            + WORD_START + " ,"
                            + WORD_OVERTIME + " ,"
                            + WORD_CONNECT + " TEXT NOT NULL,"
                            + WORD_CONTENT + " TEXT NOT NULL,"
                            + WORD_PLACE + " ,"
                            + WORD_MEMO + " );");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    //データベースのオープン
    public DBAdapter open(){
        db = dbHelper.getWritableDatabase();
        return this;
    }

    //データベースのクローズ
    public void close(){
        dbHelper.close();
    }

    //予定情報の取得
    public Cursor getPlanInfo(String getSearch, String[] field){
        return db.query(TABLE_NAME, new String[]{WORD_DATEINDEX, WORD_START, WORD_OVERTIME, WORD_CONNECT, WORD_CONTENT, WORD_PLACE, WORD_MEMO}, getSearch, field, null, null, "_id ASC");
    }

    //行の削除
    public boolean deleteWord(String id){
        return db.delete(TABLE_NAME, WORD_ID + "=" + id, null) > 0;
    }

    //行の挿入
    public void saveWord(Card card){
        ContentValues values = new ContentValues();
        values.put(WORD_DATEINDEX, card.getDate());
        values.put(WORD_START, card.getStartTime());
        values.put(WORD_OVERTIME, card.getOverTime());
        values.put(WORD_CONNECT, card.getConnect());
        values.put(WORD_CONTENT, card.getContent());
        values.put(WORD_PLACE, card.getPlace());
        values.put(WORD_MEMO, card.getMemo());
        db.insertOrThrow(TABLE_NAME, null, values);
    }

    //行の更新
    public void updateWord(String cardId, Card card){
        ContentValues values = new ContentValues();
        values.put(WORD_DATEINDEX, card.getDate());
        values.put(WORD_START, card.getStartTime());
        values.put(WORD_OVERTIME, card.getOverTime());
        values.put(WORD_CONNECT, card.getConnect());
        values.put(WORD_CONTENT, card.getContent());
        values.put(WORD_PLACE, card.getPlace());
        values.put(WORD_MEMO, card.getMemo());
        db.update(TABLE_NAME, values, WORD_ID + " = " + cardId, null);
    }

}

