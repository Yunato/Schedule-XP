package com.example.yukinaito.schedule_xp;

import android.app.Application;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class ScheduleApplication extends Application {
    static DBAdapter dbAdapter;

    private ArrayList<ModelCard> modelInfo;
    private ArrayList<Card> modelCards;
    private ArrayList<Card> planCards;
    private ArrayList<WantPlanCard> investmentCards;
    private ArrayList<WantPlanCard> wasteCards;
    private ArrayList<MustPlanCard> mustCards;
    private ArrayList<EventPlanCard> eventCards;

    private String modelIndex = null;

    @Override
    public void onCreate() {
        super.onCreate();
        dbAdapter = new DBAdapter(this);
    }

    //region DBアクセス
    //region 保存
    public void saveCard(ModelCard card){
        dbAdapter.open();
        long id = dbAdapter.saveWord(card);
        dbAdapter.close();

        modelInfo.add(card.setId(id));
    }

    public void saveCard(ArrayList<Card> cards, int index){
        for(int i = 0; i < cards.size(); i++){
            if(cards.get(i).getId() == null){
                //DBへ保存
                dbAdapter.open();
                long id = dbAdapter.saveWord(cards.get(i));
                dbAdapter.close();

                //Listへ追加
                if(index + i == modelCards.size()){
                    modelCards.add(cards.get(i).setId(id));
                }else{
                    modelCards.add(index + i, cards.get(i).setId(id));
                }
            }else{
                //DBの更新
                dbAdapter.open();
                dbAdapter.updateWord(cards.get(i).getId(), cards.get(i));
                dbAdapter.close();

                //Listへ追加
                if(index + i == modelCards.size()){
                    modelCards.add(cards.get(i));
                }else{
                    modelCards.add(index + i, cards.get(i));
                }
            }
        }
    }

    public void saveCard(Card card, int index){
        //DBへ保存
        dbAdapter.open();
        long id = dbAdapter.saveWord(card);
        dbAdapter.close();

        //Listへ追加
        if(index == planCards.size()){
            planCards.add(card.setId(id));
        }else{
            planCards.add(index, card.setId(id));
        }
    }

    public void saveCard(WantPlanCard card){
        dbAdapter.open();
        dbAdapter.saveWord(card);
        dbAdapter.close();

        if(card.getWhich() == -1){
            investmentCards.add(card);
        }else if(card.getWhich() == -2){
            wasteCards.add(card);
        }
    }

    public void saveCard(MustPlanCard card, int index){
        //DBへ保存
        dbAdapter.open();
        long id = dbAdapter.saveWord(card);
        dbAdapter.close();

        //Listへ追加
        if(index == mustCards.size()){
            mustCards.add(card.setId(id));
        }else{
            mustCards.add(index, card.setId(id));
        }
    }

    public void saveCard(EventPlanCard card, int index){
        //DBへ保存
        dbAdapter.open();
        long id = dbAdapter.saveWord(card);
        dbAdapter.close();

        //Listへ追加
        if(index == eventCards.size()){
            eventCards.add(card.setId(id));
        }else{
            eventCards.add(index, card.setId(id));
        }
    }
    //endregion

    //region 取得
    public ArrayList<String> getModelNames(){
        getModelInfo();

        ArrayList<String> modelNames = new ArrayList<>();
        Iterator iterator = modelInfo.iterator();
        while(iterator.hasNext()){
            ModelCard addCard;
            if((addCard = (ModelCard)iterator.next()).getSaved()){
                modelNames.add(addCard.getTitle());
            }
        }
        return modelNames;
    }

    public ArrayList<ModelCard> getModelInfo(){
        if(modelInfo == null){
            modelInfo = new ArrayList<>();
            dbAdapter.open();
            Cursor cursor = dbAdapter.getPlanInfo("dateindex = ? and start = ?",new String[]{"-2", "-5"});

            while(cursor.moveToNext()){
                ModelCard addCard = new ModelCard(cursor.getString(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("content")),
                        cursor.getInt(cursor.getColumnIndex("connect")) > 0);
                modelInfo.add(addCard);
            }
            cursor.close();
            dbAdapter.close();
        }
        return modelInfo;
    }

    public ArrayList<Card> getModelCards(){
        return modelCards;
    }

    public ArrayList<Card> getPlanCards(){
        if(planCards == null){
            planCards = new ArrayList<>();
            dbAdapter.open();
            Cursor cursor = dbAdapter.getPlanInfo("dateindex >= ? and start >= ?",new String[]{"10000000", "0"});

            while(cursor.moveToNext()){
                Card addCard = new Card(cursor.getString(cursor.getColumnIndex("_id")),
                        cursor.getInt(cursor.getColumnIndex("dateindex")),
                        cursor.getInt(cursor.getColumnIndex("start")),
                        cursor.getInt(cursor.getColumnIndex("overtime")),
                        cursor.getInt(cursor.getColumnIndex("connect")) > 0,
                        cursor.getString(cursor.getColumnIndex("content")),
                        cursor.getString(cursor.getColumnIndex("place")));
                planCards.add(addCard);
            }
            cursor.close();
            dbAdapter.close();
            //ソート
            Collections.sort(planCards, new PlanCardComparator());
        }
        return planCards;
    }

    public ArrayList<WantPlanCard> getInvestmentCards(){
        if(investmentCards == null) {
            investmentCards = new ArrayList<>();
            dbAdapter.open();
            Cursor cursor = dbAdapter.getPlanInfo("dateindex" + " = ? and start = ?", new String[]{"-1", "-1"});

            while (cursor.moveToNext()) {
                WantPlanCard addCard = new WantPlanCard(cursor.getString(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("content")),
                        cursor.getInt(cursor.getColumnIndex("connect")) > 0,
                        cursor.getInt(cursor.getColumnIndex("overtime")),
                        cursor.getString(cursor.getColumnIndex("place")),
                        -1);
                investmentCards.add(addCard);
            }
            cursor.close();
            dbAdapter.close();
        }
        return investmentCards;
    }

    public ArrayList<WantPlanCard> getWasteCards(){
        if(wasteCards == null){
            wasteCards = new ArrayList<>();
            dbAdapter.open();
            Cursor cursor = dbAdapter.getPlanInfo("dateindex" + " = ? and start = ?", new String[]{"-1", "-2"});

            while (cursor.moveToNext()) {
                WantPlanCard addCard = new WantPlanCard(cursor.getString(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("content")),
                        cursor.getInt(cursor.getColumnIndex("connect")) > 0,
                        cursor.getInt(cursor.getColumnIndex("overtime")),
                        cursor.getString(cursor.getColumnIndex("place")),
                        -2);
                wasteCards.add(addCard);
            }
            cursor.close();
            dbAdapter.close();
        }
        return wasteCards;
    }

    public ArrayList<MustPlanCard> getMustCards(){
        if(mustCards == null){
            mustCards = new ArrayList<>();
            dbAdapter.open();
            Cursor cursor = dbAdapter.getPlanInfo("start" + " = ?",new String[]{"-3"});

            while(cursor.moveToNext()){
                MustPlanCard addCard = new MustPlanCard(cursor.getString(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("content")),
                        cursor.getInt(cursor.getColumnIndex("connect")) > 0,
                        cursor.getInt(cursor.getColumnIndex("dateindex")),
                        cursor.getInt(cursor.getColumnIndex("overtime")),
                        cursor.getString(cursor.getColumnIndex("place")));
                mustCards.add(addCard);
            }
            cursor.close();
            dbAdapter.close();
            //ソート
            Collections.sort(mustCards, new MustCardComparator());
        }
        return mustCards;
    }

    public ArrayList<EventPlanCard> getEventCards(){
        if(eventCards == null){
            eventCards = new ArrayList<>();
            dbAdapter.open();
            Cursor cursor = dbAdapter.getPlanInfo("start" + " = ?",new String[]{"-4"});

            while(cursor.moveToNext()){
                EventPlanCard addCard = new EventPlanCard(cursor.getString(cursor.getColumnIndex("_id")),
                                cursor.getInt(cursor.getColumnIndex("dateindex")),
                                cursor.getString(cursor.getColumnIndex("content")),
                                cursor.getInt(cursor.getColumnIndex("overtime")));
                eventCards.add(addCard);
            }
            cursor.close();
            dbAdapter.close();
            //ソート
            Collections.sort(eventCards, new EventCardComparator());
        }
        return eventCards;
    }
    //endregion

    //region 更新
    public void updateCard(String wordId, ModelCard card){
        dbAdapter.open();
        dbAdapter.updateWord(wordId, card);
        dbAdapter.close();
    }

    public void updateCard(String wordId, Card card, int index){
        dbAdapter.open();
        dbAdapter.updateWord(wordId, card);
        dbAdapter.close();

        if(index == planCards.size()){
            planCards.add(card.setId(Long.parseLong(wordId)));
        }else{
            planCards.add(index, card.setId(Long.parseLong(wordId)));
        }
    }

    public void updateCard(String wordId, WantPlanCard card){
        dbAdapter.open();
        dbAdapter.updateWord(wordId, card);
        dbAdapter.close();

        if(card.getWhich() == -1){
            investmentCards.add(card);
        }else if(card.getWhich() == -2){
            wasteCards.add(card);
        }
    }

    public void updateCard(String wordId, MustPlanCard card, int index){
        dbAdapter.open();
        dbAdapter.updateWord(wordId, card);
        dbAdapter.close();

        if(index == mustCards.size()){
            mustCards.add(card.setId(Long.parseLong(wordId)));
        }else{
            mustCards.add(index, card.setId(Long.parseLong(wordId)));
        }
    }

    public void updateCard(String wordId, EventPlanCard card, int index){
        dbAdapter.open();
        dbAdapter.updateWord(wordId, card);
        dbAdapter.close();

        if(index >= eventCards.size()){
            eventCards.add(card.setId(Long.parseLong(wordId)));
        }else{
            eventCards.add(index, card.setId(Long.parseLong(wordId)));
        }
    }

    //endregion

    //画面遷移の通知の受信
    public void startModelFragment(int modelIndex){
        if(this.modelIndex == null || modelCards == null || this.modelIndex != modelInfo.get(modelIndex).getId()){
            modelCards = new ArrayList<>();
            dbAdapter.open();
                Log.d("始まりですにゃ、今のモデルインデックスは",modelInfo.get(modelIndex).getId());
            Cursor cursor = dbAdapter.getPlanInfo("dateindex = ?",new String[]{modelInfo.get(modelIndex).getId()});

            while(cursor.moveToNext()){
                Card addCard = new Card(cursor.getString(cursor.getColumnIndex("_id")),
                        cursor.getInt(cursor.getColumnIndex("dateindex")),
                        cursor.getInt(cursor.getColumnIndex("start")),
                        cursor.getInt(cursor.getColumnIndex("overtime")),
                        cursor.getInt(cursor.getColumnIndex("connect")) > 0,
                        cursor.getString(cursor.getColumnIndex("content")),
                        cursor.getString(cursor.getColumnIndex("place")));
                modelCards.add(addCard);
            }
            cursor.close();
            dbAdapter.close();
            //ソート
            Collections.sort(modelCards, new PlanCardComparator());

            this.modelIndex = modelInfo.get(modelIndex).getId();
        }
    }

    public String getModelIndex(){
        return this.modelIndex;
    }

    //モデルを削除する場合、インデックスをリセット
    public void checkModelIndex(String modelIndex){
        if(this.modelIndex == modelIndex){
            this.modelIndex = null;
        }
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
            Log.d("SQLApp 出力結果ですよ", cursor.getColumnIndex("dateindex")+ " "
                    +  cursor.getColumnIndex("start")+ " "
                    +  cursor.getColumnIndex("overtime")+ " "
                    +  cursor.getColumnIndex("connect")+ " "
                    +  cursor.getColumnIndex("content")+ " "
                    +  cursor.getColumnIndex("place")+ " "
                    +  cursor.getColumnIndex("memo") );
            Log.d("SQLApp 出力結果ですよ", cursor.getString(cursor.getColumnIndex("dateindex"))+ " "
                    +  cursor.getString(cursor.getColumnIndex("start"))+ " "
                    +  cursor.getString(cursor.getColumnIndex("overtime"))+ " "
                    +  cursor.getString(cursor.getColumnIndex("connect"))+ " "
                    +  cursor.getString(cursor.getColumnIndex("content"))+ " "
                    +  cursor.getString(cursor.getColumnIndex("place"))+ " "
                    +  cursor.getString(cursor.getColumnIndex("memo")));
        }
        cursor.close();
        dbAdapter.close();
    }
    //endregion

    public class PlanCardComparator implements Comparator<Card>{
        public int compare(Card PlanCard1, Card PlanCard2){
            int date1 = PlanCard1.getDate() + PlanCard1.getStartTime() + PlanCard1.getOverTime();
            int date2 = PlanCard2.getDate() + PlanCard2.getStartTime() + PlanCard2.getOverTime();
            if(date1 > date2){
                return 1;
            }else if(date1 == date2){
                return 0;
            }else{
                return -1;
            }
        }
    }

    public class MustCardComparator implements Comparator<MustPlanCard>{
        public int compare(MustPlanCard PlanCard1, MustPlanCard PlanCard2){
            int date1 = PlanCard1.getLimitDate() + PlanCard1.getLimitTime();
            int date2 = PlanCard2.getLimitDate() + PlanCard2.getLimitTime();
            if(date1 > date2){
                return 1;
            }else if(date1 == date2){
                return 0;
            }else{
                return -1;
            }
        }
    }

    public class EventCardComparator implements Comparator<EventPlanCard>{
        public int compare(EventPlanCard PlanCard1, EventPlanCard PlanCard2){
            int date1 = PlanCard1.getDate();
            int date2 = PlanCard2.getDate();
            if(date1 > date2){
                return 1;
            }else if(date1 == date2){
                return 0;
            }else{
                return -1;
            }
        }
    }
}
