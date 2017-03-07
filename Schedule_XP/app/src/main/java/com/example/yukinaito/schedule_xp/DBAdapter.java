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
    public static final String WORD_DATEINDEX = "dateindex";
    public static final String WORD_START = "start";
    public static final String WORD_OVERTIME = "overtime";
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
                            + WORD_DATEINDEX + " INTEGER,"
                            + WORD_START + " INTEGER,"
                            + WORD_OVERTIME + " INTEGER,"
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
        return db.query(TABLE_NAME, new String[]{WORD_ID, WORD_DATEINDEX, WORD_START, WORD_OVERTIME, WORD_CONNECT, WORD_CONTENT, WORD_PLACE, WORD_MEMO}, getSearch, field, null, null, "_id ASC");
    }

    //行の削除
    public boolean deleteWord(String id){
        return db.delete(TABLE_NAME, WORD_ID + "=" + id, null) > 0;
    }

    //region 行の挿入
    public long saveWord(ModelCard card){
        ContentValues values = new ContentValues();
        values.put(WORD_DATEINDEX, -2);
        values.put(WORD_START, -5);
        values.put(WORD_OVERTIME, -1);
        values.put(WORD_CONNECT, card.getSaved());
        values.put(WORD_CONTENT, card.getTitle());
        return db.insertOrThrow(TABLE_NAME, null, values);
    }

    public long saveWord(Card card){
        ContentValues values = new ContentValues();
        values.put(WORD_DATEINDEX, card.getDate());
        values.put(WORD_START, card.getStartTime());
        values.put(WORD_OVERTIME, card.getOverTime());
        values.put(WORD_CONNECT, card.getConnect());
        values.put(WORD_CONTENT, card.getContent());
        values.put(WORD_PLACE, card.getPlace());
        values.put(WORD_MEMO, card.getMemo());
        return db.insertOrThrow(TABLE_NAME, null, values);
    }

    public long saveWord(WantPlanCard card){
        ContentValues values = new ContentValues();
        values.put(WORD_DATEINDEX, -1);
        values.put(WORD_START, card.getWhich());
        values.put(WORD_OVERTIME, card.getRatio());
        values.put(WORD_CONNECT, card.getActive());
        values.put(WORD_CONTENT, card.getContent());
        values.put(WORD_PLACE, card.getPlace());
        values.put(WORD_MEMO, card.getSum());
        return db.insertOrThrow(TABLE_NAME, null, values);
    }

    public long saveWord(MustPlanCard card){
        ContentValues values = new ContentValues();
        values.put(WORD_DATEINDEX, card.getLimitDate());
        values.put(WORD_START, -3);
        values.put(WORD_OVERTIME, card.getLimitTime());
        values.put(WORD_CONNECT, card.getActive());
        values.put(WORD_CONTENT, card.getContent());
        values.put(WORD_PLACE, card.getPlace());
        values.put(WORD_MEMO, card.getMemo());
        return db.insertOrThrow(TABLE_NAME, null, values);
    }

    public long saveWord(EventPlanCard card){
        ContentValues values = new ContentValues();
        values.put(WORD_DATEINDEX, card.getDate());
        values.put(WORD_START, -4);
        values.put(WORD_OVERTIME, card.getIndex());
        values.put(WORD_CONNECT, false);
        values.put(WORD_CONTENT, card.getTitle());
        return db.insertOrThrow(TABLE_NAME, null, values);
    }
    //endregion

    //region 行の更新
    public void updateWord(String cardId, ModelCard card){
        ContentValues values = new ContentValues();
        values.put(WORD_DATEINDEX, -2);
        values.put(WORD_START, -5);
        values.put(WORD_OVERTIME, -1);
        values.put(WORD_CONNECT, card.getSaved());
        values.put(WORD_CONTENT, card.getTitle());
        db.update(TABLE_NAME, values, WORD_ID + " = " + cardId, null);
    }

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

    public void updateWord(String cardId, WantPlanCard card){
        ContentValues values = new ContentValues();
        values.put(WORD_DATEINDEX, -1);
        values.put(WORD_START, card.getWhich());
        values.put(WORD_OVERTIME, card.getRatio());
        values.put(WORD_CONNECT, card.getActive());
        values.put(WORD_CONTENT, card.getContent());
        values.put(WORD_PLACE, card.getPlace());
        db.update(TABLE_NAME, values, WORD_ID + " = " + cardId, null);
    }

    public void updateWord(String cardId, MustPlanCard card){
        ContentValues values = new ContentValues();
        values.put(WORD_DATEINDEX, card.getLimitDate());
        values.put(WORD_START, -3);
        values.put(WORD_OVERTIME, card.getLimitTime());
        values.put(WORD_CONNECT, card.getActive());
        values.put(WORD_CONTENT, card.getContent());
        values.put(WORD_PLACE, card.getPlace());
        values.put(WORD_MEMO, card.getMemo());
        db.update(TABLE_NAME, values, WORD_ID + " = " + cardId, null);
    }

    public void updateWord(String cardId, EventPlanCard card){
        ContentValues values = new ContentValues();
        values.put(WORD_DATEINDEX, card.getDate());
        values.put(WORD_START, -4);
        values.put(WORD_OVERTIME, card.getIndex());
        values.put(WORD_CONNECT, false);
        values.put(WORD_CONTENT, card.getTitle());
        db.update(TABLE_NAME, values, WORD_ID + " = " + cardId, null);
    }

    public void updateDate(int sum){
        ContentValues values = new ContentValues();
        values.put(WORD_DATEINDEX, sum);
        db.update(TABLE_NAME, values, WORD_ID + " = 8", null);
    }

    //endregion
}

