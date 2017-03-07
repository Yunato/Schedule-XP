package com.example.yukinaito.schedule_xp;

import android.app.Application;
import android.database.Cursor;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
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

    private ArrayList<Card> SchCards;

    private String modelIndex = null;
    private int showDate = 0;

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
        long id = dbAdapter.saveWord(card);
        dbAdapter.close();

        if(card.getWhich() == -1){
            investmentCards.add(card.setId(id));
        }else if(card.getWhich() == -2){
            wasteCards.add(card.setId(id));
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

    //したいことに費やした時間の合計値を保存
    public void saveData(Card card){
        //DBへ保存
        dbAdapter.open();
        dbAdapter.saveWord(card);
        dbAdapter.close();
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
            Cursor cursor = dbAdapter.getPlanInfo("dateindex = ? and start = ?", new String[]{"-1", "-1"});

            while (cursor.moveToNext()) {
                WantPlanCard addCard = new WantPlanCard(cursor.getString(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("content")),
                        cursor.getInt(cursor.getColumnIndex("connect")) > 0,
                        cursor.getInt(cursor.getColumnIndex("overtime")),
                        cursor.getString(cursor.getColumnIndex("place")),
                        -1,
                        cursor.getInt(cursor.getColumnIndex("memo")));
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
            Cursor cursor = dbAdapter.getPlanInfo("dateindex = ? and start = ?", new String[]{"-1", "-2"});

            while (cursor.moveToNext()) {
                WantPlanCard addCard = new WantPlanCard(cursor.getString(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("content")),
                        cursor.getInt(cursor.getColumnIndex("connect")) > 0,
                        cursor.getInt(cursor.getColumnIndex("overtime")),
                        cursor.getString(cursor.getColumnIndex("place")),
                        -2,
                        cursor.getInt(cursor.getColumnIndex("memo")));
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
            Cursor cursor = dbAdapter.getPlanInfo("start = ?",new String[]{"-3"});

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
            Cursor cursor = dbAdapter.getPlanInfo("start = ?",new String[]{"-4"});

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

    public void updateCard(String wordId, WantPlanCard card, boolean check){
        dbAdapter.open();
        dbAdapter.updateWord(wordId, card);
        dbAdapter.close();

        //checkがtrueなら更新後のデータをlistに追加する
        if(check){
            if(card.getWhich() == -1){
                investmentCards.add(card);
            }else if(card.getWhich() == -2){
                wasteCards.add(card);
            }
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

    //したいことに費やした時間の合計値を更新
    public void updateData(int sum){
        dbAdapter.open();
        dbAdapter.updateDate(sum);
        dbAdapter.close();
    }

    //endregion

    //画面遷移の通知の受信
    public void startModelFragment(int position){
        if(this.modelIndex == null || modelCards == null) {
            modelIndex = "-1";
            modelCards = new ArrayList<>();
        }
        if(this.modelIndex != modelInfo.get(position).getId()){
            modelCards.clear();
            dbAdapter.open();
            Cursor cursor = dbAdapter.getPlanInfo("dateindex = ?",new String[]{modelInfo.get(position).getId()});

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
            Collections.sort(modelCards, new CardComparator());

            this.modelIndex = modelInfo.get(position).getId();
        }
    }

    public String getModelIndex(){
        return this.modelIndex;
    }

    //モデルを削除する場合、インデックスをリセット
    public void checkModelIndex(String modelIndex){
        if(this.modelIndex == modelIndex){
            modelCards = null;
            this.modelIndex = null;
        }
        deleteCard(modelIndex);

        for(int i = 0; i < modelInfo.size(); i++){
            if(modelInfo.get(i).getId() == modelIndex){
                modelInfo.remove(i);
                break;
            }
        }

        dbAdapter.open();
        Cursor cursor = dbAdapter.getPlanInfo("dateindex = ?",new String[]{modelIndex});

        while(cursor.moveToNext()){
            deleteCard(cursor.getString(cursor.getColumnIndex("_id")));
        }
        cursor.close();
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

    //日替わりに一度行う
    public ArrayList<Card> createSchedule(int date){
        if(SchCards == null){
            SchCards = new ArrayList<>();
        }
        //スケジュールの新規作成
        if(showDate == 0 || showDate != date){
            SchCards.clear();

            //浪費にかける時間を保持
            int wasteTime = 0;

            dbAdapter.open();
            //region イベント日/モデル関連
            Cursor cursor = dbAdapter.getPlanInfo("dateindex = ? and start = ?",new String[]{Integer.toString(date), "-4"});
            if(cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndex("overtime"));
                cursor = dbAdapter.getPlanInfo("dateindex = ?",new String[]{Integer.toString(id)});

                while(cursor.moveToNext()){
                    Card addCard = new Card(cursor.getString(cursor.getColumnIndex("_id")),
                            cursor.getInt(cursor.getColumnIndex("dateindex")),
                            cursor.getInt(cursor.getColumnIndex("start")),
                            cursor.getInt(cursor.getColumnIndex("overtime")),
                            cursor.getInt(cursor.getColumnIndex("connect")) > 0,
                            cursor.getString(cursor.getColumnIndex("content")),
                            cursor.getString(cursor.getColumnIndex("place")));
                    SchCards.add(addCard);
                }

                cursor = dbAdapter.getPlanInfo("_id = ?",new String[]{Integer.toString(id)});
            }else {
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(Integer.parseInt(Integer.toString(date).substring(0, 4)),
                        Integer.parseInt(Integer.toString(date).substring(4, 6)) - 1,
                        Integer.parseInt(Integer.toString(date).substring(6, 8)));
                int week = calendar.get(Calendar.DAY_OF_WEEK);
                cursor = dbAdapter.getPlanInfo("dateindex = ?", new String[]{Integer.toString(week)});

                while (cursor.moveToNext()) {
                    Card addCard = new Card(cursor.getString(cursor.getColumnIndex("_id")),
                            cursor.getInt(cursor.getColumnIndex("dateindex")),
                            cursor.getInt(cursor.getColumnIndex("start")),
                            cursor.getInt(cursor.getColumnIndex("overtime")),
                            cursor.getInt(cursor.getColumnIndex("connect")) > 0,
                            cursor.getString(cursor.getColumnIndex("content")),
                            cursor.getString(cursor.getColumnIndex("place")));
                    SchCards.add(addCard);
                }

                cursor = dbAdapter.getPlanInfo("_id = ?", new String[]{Integer.toString(week)});
            }
            if(cursor.moveToNext()){
                wasteTime = 10 * 6;
                //後で修正
                //wasteTime = 10 * cursor.getInt(cursor.getColumnIndex("overtime");
            }
            //endregion

            Collections.sort(SchCards, new CardComparator());

            //region 予定関連
            cursor = dbAdapter.getPlanInfo("dateindex = ? and start >= ?",new String[]{Integer.toString(date), "0"});
            while(cursor.moveToNext()){
                Card addCard = new Card(cursor.getString(cursor.getColumnIndex("_id")),
                        cursor.getInt(cursor.getColumnIndex("dateindex")),
                        cursor.getInt(cursor.getColumnIndex("start")),
                        cursor.getInt(cursor.getColumnIndex("overtime")),
                        cursor.getInt(cursor.getColumnIndex("connect")) > 0,
                        cursor.getString(cursor.getColumnIndex("content")),
                        cursor.getString(cursor.getColumnIndex("place")));

                //追加する予定の開始時刻とスケジュール表の開始時刻を確認
                int index, start, time;
                for(index = 0; index < SchCards.size(); index++){
                    if((start = SchCards.get(index).getStartTime()) >= addCard.getStartTime()){
                        while(index < SchCards.size() && SchCards.get(index).getStartTime() < addCard.getOverTime()){
                            if(SchCards.get(index).getConnect()){
                                if(index + 1 < SchCards.size()){
                                    time = SchCards.get(index + 1).getStartTime();
                                }else{
                                    time = 2400;
                                }
                            }else{
                                time = SchCards.get(index).getOverTime();
                            }

                            if(time <= addCard.getOverTime()){
                                SchCards.remove(index);
                            }else{
                                SchCards.get(index).setStartTime(addCard.getOverTime());
                            }
                        }
                        if(index > 0 && start > addCard.getOverTime() && SchCards.get(index - 1).getOverTime() == -1){
                            SchCards.add(index, new Card(date, addCard.getOverTime(), -1, true,
                                            SchCards.get(index - 1).getContent(), SchCards.get(index - 1).getPlace()));
                        }
                        if(index > 0 && SchCards.get(index - 1).getOverTime() > addCard.getStartTime()){
                            SchCards.get(index - 1).setOverTime(addCard.getStartTime());
                        }
                        break;
                    }
                }
                if(index < SchCards.size()){
                    SchCards.add(index, addCard);
                }else{
                    SchCards.add(addCard);
                }
            }
            //endregion

            //region したいこと関連
            getInvestmentCards();
            ArrayList<Boolean> SchWantChecks = new ArrayList<>();
            for(int i = 0; i < investmentCards.size(); i++){
                SchWantChecks.add(true);
            }
            cursor.close();
            dbAdapter.close();

            createSchWantCards(0, SchWantChecks, wasteTime);
            //endregion

            dbAdapter.open();

            for(int i = 0; i < SchCards.size(); i++){
                Log.d("TEST", i + "番目  " + SchCards.get(i).getContent());
            }

            //idはいらないよ
            dbAdapter.close();

            investmentCards = null;
            showDate = date;
        }
        return SchCards;
    }

    //SchCardsはblank時間以外は完成させておく必要がある。
    //nowTimeは現在時刻 この時刻移行の空き時間を調べる
    //timesは戻り値用 各したいことにかける時間を保持 checksは各したいことを行うかどうか
    public void createSchWantCards(int nowTime, ArrayList<Boolean> checks, int wasteTime){
        int blankTime = 1440;
        int Sum = -1;

        //region 空き時間作成
        for(int i = 0; i < SchCards.size(); i++){
            if(SchCards.get(i).getConnect()){
                if(i + 1 < SchCards.size()){
                    blankTime -= convertMinute(SchCards.get(i + 1).getStartTime()) - convertMinute(SchCards.get(i).getStartTime());
                }else{
                    blankTime -= 1440 - convertMinute(SchCards.get(i).getStartTime());
                }
            }else{
                //region 小さな誤差時間にblankを補完する
                if(i + 1 < SchCards.size()){
                    if(SchCards.get(i).getOverTime() != SchCards.get(i + 1).getStartTime()){
                        if(SchCards.get(i).getOverTime()/5 != SchCards.get(i + 1).getStartTime()/5){
                            if(SchCards.get(i).getOverTime() % 5 != 0){
                                blankTime = createBlankCard(-4, SchCards.get(i).getOverTime(), ((SchCards.get(i).getOverTime() * 2 + 8) / 10) * 5, blankTime);
                            }
                            if(SchCards.get(i + 1).getStartTime() % 5 != 0){
                                blankTime = createBlankCard(-4, ((SchCards.get(i + 1).getStartTime() * 2) / 10) * 5, SchCards.get(i + 1).getStartTime(), blankTime);
                            }
                        }else{
                            blankTime = createBlankCard(-3, SchCards.get(i).getOverTime(), SchCards.get(i + 1).getStartTime(), blankTime);
                        }
                    }
                }else{
                    if(SchCards.get(i).getOverTime() % 5 != 0){
                        blankTime = createBlankCard(-4, SchCards.get(i).getOverTime(), ((SchCards.get(i).getOverTime() * 2 + 8) / 10) * 5, blankTime);
                    }
                }
                //endregion
                blankTime -= convertMinute(SchCards.get(i).getOverTime()) - convertMinute(SchCards.get(i).getStartTime());
            }
        }
        //endregion

        int investmentTime = blankTime - wasteTime;
        investmentTime /= 5;

        getWasteCards();
        ArrayList<Integer> SchTime = new ArrayList<>();

        //region それぞれのしたいことにかける時間を計算
        int count;
        for(count = 0; count < checks.size(); count++){
            if(checks.get(count)){
                if(investmentCards.get(count).getActive()){
                    investmentCards.get(count).setActive(false);
                    investmentCards.add(investmentCards.get(count));
                    checks.add(checks.get(count));
                    investmentCards.remove(count);
                    checks.remove(count);
                    count--;
                }else{
                    SchTime.add((int)(investmentTime * ((double)investmentCards.get(count).getRatio() / 100)));
                    //investmentCards.get(count).addSum(SchTime[count]);
                }
            }
        }
        //endregion

        for(int i = 0; i < investmentCards.size(); i++){
            investmentTime -= SchTime.get(i);
        }

        dbAdapter.open();
        Cursor cursor = dbAdapter.getPlanInfo("_id = ?",new String[]{"8"});
        if(cursor.moveToNext()){
            Sum = Integer.parseInt(cursor.getString(cursor.getColumnIndex("dateindex")));
        }
        cursor.close();
        dbAdapter.close();

        for(int i = investmentCards.size() - 1; i >= 0 && investmentTime > 0; i--){
            if(Sum <= 0 || (double)investmentCards.get(i).getSum() / Sum * 100 <= investmentCards.get(i).getRatio()){
                SchTime.set(i, SchTime.get(i) + 1);
                investmentTime--;
            }
        }

        //region 修正必要
        SchTime.add(SchTime.size()/2, wasteTime/5);
        investmentCards.add(SchTime.size()/2, new WantPlanCard("フリー", false, 0, "部屋", -1));
        //endregion

        for(int i = 0; i < SchCards.size(); i++){
            Log.d("TEST", i + "番目  " + SchCards.get(i).getContent());
        }
        for(int i = 0; i < investmentCards.size(); i++){
            Log.d("TEST", i + "番目  " + investmentCards.get(i).getContent());
        }
        Log.d("TEST","投資 : " + investmentTime + " 浪費 : " + wasteTime);
        for(int i = 0; i < SchTime.size(); i++){
            Log.d("TEST", i + "番目 " + SchTime.get(i));
        }

        //region 空き時間に追加するカードを作成する
        count = 0;
        if(SchCards.size() > 0){
            for(int i = 0, start, finish; i < SchCards.size() && count < investmentCards.size(); i++) {
                if (!SchCards.get(i).getConnect()) {
                    if (i + 1 < SchCards.size()) {
                        if (SchCards.get(i).getOverTime() != SchCards.get(i + 1).getStartTime()) {
                            int remaining = convertMinute(SchCards.get(i + 1).getStartTime()) - convertMinute(SchCards.get(i).getOverTime());
                            remaining = remaining / 5;
                            //region 投資予定を作成
                            for (; remaining > 0; i++) {
                                if (SchCards.get(i).getDate() == -4) {
                                    start = SchCards.get(i).getStartTime();
                                    SchCards.remove(i);
                                    i--;
                                } else {
                                    start = SchCards.get(i).getOverTime();
                                }
                                if (SchCards.get(i + 1).getDate() == -4) {
                                    finish = SchCards.get(i + 1).getOverTime();
                                    SchCards.remove(i + 1);
                                } else {
                                    finish = SchCards.get(i + 1).getStartTime();
                                }
                                if ((convertMinute(finish) - convertMinute(start)) / 5 > SchTime.get(count)) {
                                    finish = convertMinute(start) + SchTime.get(count) * 5;
                                    finish = finish / 60 * 100 + finish % 60;
                                    remaining -= SchTime.get(count);
                                    SchTime.set(count, 0);
                                } else {
                                    remaining -= (convertMinute(finish) - convertMinute(start)) / 5;
                                    SchTime.set(count, SchTime.get(count) - (convertMinute(finish) - convertMinute(start)) / 5);
                                }
                                Card addCard = new Card(-3, start, finish, false,
                                        investmentCards.get(count).getContent(), investmentCards.get(count).getPlace());
                                SchCards.add(i + 1, addCard);
                                if (SchTime.get(count) == 0) {
                                    count++;
                                }
                            }
                            i--;
                            //endregion
                        }
                    } else {
                        int remaining = 2400 - SchCards.get(i).getOverTime();
                        remaining = convertMinute(remaining) / 5;
                        //region 投資予定を作成
                        for (; remaining > 0; i++) {
                            if (SchCards.get(i).getDate() == -4) {
                                start = SchCards.get(i).getStartTime();
                                SchCards.remove(i);
                            } else {
                                start = SchCards.get(i).getOverTime();
                            }
                            finish = 2400;
                            if ((convertMinute(finish) - convertMinute(start)) / 5 > SchTime.get(count)) {
                                finish = convertMinute(start) + SchTime.get(count) * 5;
                                finish = finish / 60 * 100 + finish % 60;
                                remaining -= SchTime.get(count);
                                SchTime.set(count, 0);
                            } else {
                                remaining -= (convertMinute(finish) - convertMinute(start)) / 5;
                                SchTime.set(count, SchTime.get(count) - (convertMinute(finish) - convertMinute(start)) / 5);
                            }
                            Card addCard = new Card(-3, start, finish, false,
                                    investmentCards.get(count).getContent(), investmentCards.get(count).getPlace());
                            SchCards.add(i + 1, addCard);
                            if (SchTime.get(count) == 0) {
                                count++;
                            }
                        }
                        i--;
                        //endregion
                    }
                }
            }
        }else{
            int start = 0, finish;
            for(; count < investmentCards.size(); count++){
                finish = start + SchTime.get(count) * 5;
                start = start / 60 * 100 + start % 60;
                finish = finish / 60 * 100 + finish % 60;
                SchCards.add(new Card(-3, start, finish, false,
                        investmentCards.get(count).getContent(), investmentCards.get(count).getPlace()));
                start = convertMinute(finish);
            }
        }
        //endregion

        //updateData(blankTime - wasteTime);
        Collections.sort(SchCards, new CardComparator());

        wasteCards = null;
    }

    //hh:mmをminuteにコンバート
    public int convertMinute(int time){
        return time / 100 * 60 + time % 100;
    }

    public int createBlankCard(int date, int startTime, int overTime, int blankTime){
        Card addCard = new Card(date,
                startTime,
                overTime,
                false,
                "空き時間",
                "―");
        SchCards.add(addCard);
        blankTime -= convertMinute(addCard.getOverTime()) - convertMinute(addCard.getStartTime());
        return blankTime;
    }

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

    public class CardComparator implements Comparator<Card>{
        public int compare(Card PlanCard1, Card PlanCard2){
            int date1 = PlanCard1.getStartTime() + PlanCard1.getOverTime();
            int date2 = PlanCard2.getStartTime() + PlanCard2.getOverTime();
            if(PlanCard1.getStartTime() > PlanCard2.getStartTime() ||
                    (PlanCard1.getStartTime() == PlanCard2.getStartTime() &&
                            PlanCard1.getOverTime() > PlanCard2.getOverTime())){
                return 1;
            }else{
                return -1;
            }
        }
    }
}
