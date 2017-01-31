package com.example.yukinaito.schedule_xp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;

public class AddEventPlanActivity  extends AppCompatActivity {
    private ScheduleApplication scheduleApplication;
    //requestCode
    private static final int UPDATE_CODE = 1;
    private static final int MEMO_UPDATE_CODE = 2;
    //表示する予定群
    private CardAdapter cardAdapter;
    //予定を格納
    private ArrayList<Card> cards;
    //ListViewのItemの状態がOnかOff
    private boolean[] visibleItem;
    private EventCard eventCard;
    //メモ記入時に対象の予定の情報を格納する変数
    private Card memoCard;
    private static int memoIndex;

    private int plan_Day = -1;
    private int modelIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addeventplan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //region 前画面に戻るボタンの生成
        final Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.abc_ic_ab_back_material, null);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorsimbol), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        //endregion

        scheduleApplication = (ScheduleApplication) this.getApplication();

        findViewById(R.id.button_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region イベントの日付を選択 DatePickerの呼び出し
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("activity", 4);
                datePicker.setArguments(bundle);
                datePicker.show(getSupportFragmentManager(), "datePicker");
                //endregion
            }
        });
        findViewById(R.id.button_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region 情報をすべて入力し終わったあとの確認
                //追加できなかったとき用にバックアップ
                EventCard save = new EventCard();
                save.setInfo(eventCard.getDate(), eventCard.getIndex());
                save.deepCopy(eventCard.getCards());

                eventCard.setInfo(plan_Day, modelIndex);
                int addCheck = addCheck(eventCard);
                if (addCheck > -1) {
                    final Intent intent = new Intent();
                    intent.putExtra("Card", eventCard);

                    //選択した日付の曜日を取得するために必要
                    Calendar cal = Calendar.getInstance();
                    String time = Integer.toString(plan_Day);
                    cal.set(Calendar.YEAR, Integer.parseInt(time.substring(0, 4)));
                    cal.set(Calendar.MONTH, Integer.parseInt(time.substring(4, 6)) - 1);
                    cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(time.substring(6, 8)));

                    if (cal.get(Calendar.DAY_OF_WEEK) - 1 == modelIndex && (eventCard.getCards() == null || (eventCard.getCards() != null && eventCard.getCards().size() == 0))) {
                        //追加できないことをダイアログで知らせる
                        eventCard = save;

                        AlertDialog.Builder builder = new AlertDialog.Builder(AddEventPlanActivity.this);
                        builder.setTitle("注意");
                        builder.setMessage("デフォルトで指定されたモデルのままであるため、追加しません。");
                        builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        intent.putExtra("Position", addCheck);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } else {
                    //追加する位置が見つからなかった場合
                    addCheck = (addCheck + 1) * -1;
                    eventCard = save;

                    //追加できないことをダイアログで知らせる
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddEventPlanActivity.this);
                    builder.setTitle("警告");
                    builder.setMessage("他のイベントと日程が重なっています。\n重なっているイベント日:" + scheduleApplication.getEventplancards().get(addCheck).getDate());
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                //endregion
            }
        });
        findViewById(R.id.button_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region キャンセルボタンタップ時
                Intent intent = new Intent();
                intent.putExtra("Card", eventCard);
                intent.putExtra("EditPos", getIntent().getIntExtra("Position", -1));
                setResult(RESULT_OK, intent);
                finish();
                //endregion
            }
        });
        ((ListView) findViewById(R.id.ListView)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                //region ListViewのitemタップ時
                final int position = pos;
                if (visibleItem[position]) {
                    //region 編集を問うダイアログの生成
                    //ダイアログの生成
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddEventPlanActivity.this);
                    builder.setTitle("モデルの操作");
                    builder.setMessage("モデルの内容を編集、またはモデルを削除しますか？");
                    builder.setPositiveButton("削除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //region 選択したitemをグレーにする
                            visibleItem[position] = false;

                            for(i = 0; i < eventCard.getCards().size(); i++){
                                if(eventCard.getCards().get(i).equalsCard(cards.get(position))){
                                    eventCard.getCards().get(i).setVisible(true);
                                    updateListFragment();
                                    return;
                                }
                            }
                            EventModelCard card = new EventModelCard();
                            card.setModelInfo(true, position, cards.get(position));
                            eventCard.getCards().add(card);
                            updateListFragment();
                            //endregion
                        }
                    });
                    builder.setNegativeButton("編集", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //region 予定の編集画面へ遷移
                            //予定の生成
                            Card card = new Card();
                            card.setInfo(cards.get(position).getCalendar(),
                                    cards.get(position).getLenTime(),
                                    cards.get(position).getContent(),
                                    cards.get(position).getPlace());
                            cards.remove(position);

                            Intent intent = new Intent(AddEventPlanActivity.this, AddModelActivity.class);
                            intent.putExtra("cards", cards);
                            intent.putExtra("EditingCard", card);
                            intent.putExtra("Position", position);

                            for(i = 0; i < eventCard.getCards().size(); i++){
                                if(eventCard.getCards().get(i).equalsCard(card)){
                                    intent.putExtra("Event_Index", eventCard.getCards().get(i).getIndex());
                                    eventCard.getCards().remove(i);
                                    break;
                                }
                            }

                            startActivityForResult(intent, UPDATE_CODE);
                            //endregion
                        }
                    });
                    builder.setNeutralButton("キャンセル", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    //endregion
                } else {
                    //region visibleにするか問うダイアログの生成
                    //ダイアログの生成
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddEventPlanActivity.this);
                    builder.setTitle("モデルの操作");
                    builder.setMessage("この項目を元に戻しますか？");
                    builder.setPositiveButton("戻す", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //region 選択したitemをアクティブに戻す
                            visibleItem[position] = true;
                            for(i = 0; i < eventCard.getCards().size(); i++){
                                if(eventCard.getCards().get(i).equalsCard(cards.get(position)))
                                    break;
                            }
                            //固定スケジュールの選択 falseは削除済み
                            if(eventCard.getIndex() <= (scheduleApplication.getModelSchedule().size() - 1)) {
                                if (eventCard.getCards().get(i).equalsCard(scheduleApplication.getModelSchedule().get(modelIndex).getCards().get(eventCard.getCards().get(i).getIndex())))
                                    eventCard.getCards().remove(i);
                                else
                                    eventCard.getCards().get(i).setVisible(false);
                            }else {
                                if (eventCard.getCards().get(i).equalsCard(scheduleApplication.getEventmodel().get(eventCard.getIndex() - scheduleApplication.getModelSchedule().size()).getCards().get(eventCard.getCards().get(i).getIndex())))
                                    eventCard.getCards().remove(i);
                                else
                                    eventCard.getCards().get(i).setVisible(false);
                            }
                            updateListFragment();
                            //eventCard.getCards().get(i).setVisible(false);
                            //endregion
                        }
                    });
                    builder.setNeutralButton("キャンセル", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    //endregion
                }
                //endregion
            }
        });

        (findViewById(R.id.RelativeLayout2)).setVisibility(View.GONE);
        (findViewById(R.id.RelativeLayout3)).setVisibility(View.GONE);

        //ListViewの区切り線
        ColorDrawable separate_line_color = new ColorDrawable(ContextCompat.getColor(this, R.color.separate_line));
        ((ListView) findViewById(R.id.ListView)).setDivider(separate_line_color);
        ((ListView) findViewById(R.id.ListView)).setDividerHeight(5);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setPrompt("モデルの選択");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        for (int i = 0; i < scheduleApplication.getModelSchedule().size(); i++)
            adapter.add(scheduleApplication.getModelSchedule().get(i).getName());
        spinner.setAdapter(adapter);
        spinner.setFocusable(false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //region spinner選択時
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner)findViewById(R.id.spinner);
                if(spinner.isFocusable() == false){
                    spinner.setFocusable(true);
                    return;
                }else {
                    //spinnerの変更前の位置と変更後の位置
                    final int b_position = modelIndex;
                    modelIndex = position;

                    //spinnerで一度でも選択しているか true 選択している false 選択していない
                    if (eventCard.getCards() != null && eventCard.getCards().size() != 0) {
                        //再度spinnerで選択したとき
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddEventPlanActivity.this);
                        builder.setTitle("注意");
                        builder.setMessage("モデルの変更により、編集内容を破棄します。(確認を押した後キャンセルを押すと破棄されません。)\nよろしいですか？");
                        builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //region 編集内容の削除
                                eventCard = new EventCard();
                                createList();
                                //endregion
                            }
                        });
                        builder.setNeutralButton("キャンセル", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                modelIndex = b_position;
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }else{
                        createList();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
            //endregion
        });

        //region 分岐する起動処理
        if ((getIntent().getSerializableExtra("EditingCard")) != null) {
            //編集の場合 編集する予定の情報を反映させる
            setTitle("日時・行動の変更");
            eventCard = ((EventCard) getIntent().getSerializableExtra("EditingCard"));
            plan_Day = eventCard.getDate();

            String name;
            String start = Integer.toString(eventCard.getDate());
            ((Button) findViewById(R.id.button_1)).setText(start.substring(0, 4) + "年" + start.substring(4, 6) + "月" + start.substring(6, 8) + "日(変更時はタップ)");
            ((Button) findViewById(R.id.button_2)).setText("更新");

            modelIndex = eventCard.getIndex();

            //region 固定スケジュールの選択 true 削除された固定スケジュール false 今もある固定スケジュール
            if (eventCard.getIndex() > (scheduleApplication.getModelSchedule().size() - 1))
                name = scheduleApplication.getEventmodel().get(eventCard.getIndex() - scheduleApplication.getModelSchedule().size()).getName();
            else
                name = scheduleApplication.getModelSchedule().get(eventCard.getIndex()).getName();
            //endregion

            //region spinnerのadapter関連
            int place = -1;
            //表示する固定スケジュールの名前が他と被っていないか 被っていたら重複するため新たにspinnerへ追加する必要がない
            for (int i = 0; i < scheduleApplication.getModelSchedule().size(); i++) {
                if (scheduleApplication.getModelSchedule().get(i).getName().equals(name)) {
                    place = i;
                    break;
                }
            }

            //被っていないとき名前をspinnerに追加
            if (place == -1) {
                adapter.add(name);
                place = scheduleApplication.getModelSchedule().size();
            }
            //endregion

            spinner.setSelection(place);
            inputCheck();
        } else {
            setTitle("イベント日の追加");
            eventCard = new EventCard();
        }
        createList();
        //endregion

    }

    //Pickerによるデータの反映処理
    public void onReturnValue(int data, String text, int picker) {
        if(picker == 1) {
            plan_Day = data;
            Button button = (Button) findViewById(R.id.button_1);
            button.setText(text + "(変更時はタップ)");
        }
        inputCheck();
    }

    //入力チェック
    public void inputCheck() {
        if (plan_Day == -1)
            (findViewById(R.id.RelativeLayout3)).setVisibility(View.GONE);
        else
            (findViewById(R.id.RelativeLayout3)).setVisibility(View.VISIBLE);
    }

    //画面下部に描画するListViewの生成 初回のみ
    public void createList(){
        ListView listView = (ListView) findViewById(R.id.ListView);
        listView.setScrollingCacheEnabled(false);
        //固定スケジュールの選択
        if (eventCard.getIndex() <= (scheduleApplication.getModelSchedule().size() - 1)) {
            //region 今もある固定スケジュール
            (findViewById(R.id.RelativeLayout2)).setVisibility(View.VISIBLE);

            visibleItem = new boolean[scheduleApplication.getModelSchedule().get(modelIndex).getCards().size()];
            cards = new ArrayList<>(scheduleApplication.getModelSchedule().get(modelIndex).getCards().size());

            //itemCount card用インデックス updateCount eventCard用インデックス
            for (int itemCount = 0, updateCount = 0; itemCount < scheduleApplication.getModelSchedule().get(modelIndex).getCards().size(); itemCount++) {
                //変更の要素はnullでなく、かつ変更点の要素数がupdateCountと等しくないとき
                if (eventCard.getCards() != null && eventCard.getCards().size() != updateCount) {
                    //固定スケジュールに何か変更点があるかどうか indexが同じなら変更点あり
                    if (eventCard.getCards().get(updateCount).getIndex() == itemCount) {
                        //固定スケジュールのある要素をグレーにするのか true しない false する
                        if (!eventCard.getCards().get(updateCount).getVisible()) {
                            visibleItem[itemCount] = true;
                            Card card = eventCard.getCards().get(updateCount).getCard();
                            cards.add(card);
                        } else {
                            visibleItem[itemCount] = false;
                            Card card = eventCard.getCards().get(updateCount).getCard();
                            cards.add(card);
                        }
                        updateCount++;
                        if (eventCard.getCards().size() != updateCount && eventCard.getCards().get(updateCount).getIndex() == itemCount)
                            itemCount--;
                        continue;
                    }
                }
                visibleItem[itemCount] = true;
                Card card = scheduleApplication.getModelSchedule().get(modelIndex).getCards().get(itemCount).getCard();
                cards.add(card);
            }
            //endregion
        }else{
            //region 削除された固定スケジュール
            (findViewById(R.id.RelativeLayout2)).setVisibility(View.VISIBLE);

            //削除された固定スケジュールのindexを取得
            int index = eventCard.getIndex() - scheduleApplication.getModelSchedule().size();
            visibleItem = new boolean[scheduleApplication.getEventmodel().get(index).getCards().size()];
            cards = new ArrayList<>(scheduleApplication.getEventmodel().get(index).getCards().size());

            //itemCount card用インデックス updateCount eventCard用インデックス
            for (int itemCount = 0, updateCount = 0; itemCount < scheduleApplication.getEventmodel().get(index).getCards().size(); itemCount++) {
                //変更の要素はnullでなく、かつ変更点の要素数がupdateCountと等しくないとき
                if (eventCard.getCards() != null && eventCard.getCards().size() != updateCount) {
                    //固定スケジュールに何か変更点があるかどうか indexが同じなら変更点あり
                    if (eventCard.getCards().get(updateCount).getIndex() == itemCount) {
                        //固定スケジュールのある要素をグレーにするのか true しない false する
                        if (!eventCard.getCards().get(updateCount).getVisible()) {
                            visibleItem[itemCount] = true;
                            Card card = eventCard.getCards().get(updateCount).getCard();
                            cards.add(card);
                        } else {
                            visibleItem[itemCount] = false;
                            Card card = scheduleApplication.getEventmodel().get(index).getCards().get(itemCount).getCard();
                            cards.add(card);
                        }
                        updateCount++;
                        if (eventCard.getCards().size() != updateCount && eventCard.getCards().get(updateCount).getIndex() == itemCount)
                            itemCount--;
                        continue;
                    }
                }
                visibleItem[itemCount] = true;
                Card card = scheduleApplication.getEventmodel().get(index).getCards().get(itemCount).getCard();
                cards.add(card);
            }
            //endregion
        }
        sortCards();
        updateListFragment();
    }

    private class CardAdapter extends BaseAdapter {
        @Override
        public int getCount(){
            return cards.size();
        }

        @Override
        public Card getItem(int pos){
            return cards.get(pos);
        }

        @Override
        public long getItemId(int pos){
            return pos;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){
            Context context = AddEventPlanActivity.this;
            //ListViewの要素として表示する予定情報
            final Card card = cards.get(position);
            final int index = position;

            //レイアウトの生成
            if(view == null){
                view = (LayoutInflater.from(context)).inflate(R.layout.list_addeventplanactivity, null);
                (view.findViewById(R.id.memo)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //region メモ入力画面へ遷移
                        memoCard = card;
                        memoIndex = index;
                        Intent intent = new Intent(AddEventPlanActivity.this, AddMemoActivity.class);
                        if(card.getMemo() != null)
                            intent.putExtra("memo", card.getMemo());
                        else
                            intent.putExtra("memo", "");

                        for(int i = 0; i < eventCard.getCards().size(); i++){
                            if(eventCard.getCards().get(i).equalsCard(memoCard)){
                                intent.putExtra("Event_Index", eventCard.getCards().get(i).getIndex());
                                eventCard.getCards().remove(i);
                                break;
                            }
                        }

                        startActivityForResult(intent, MEMO_UPDATE_CODE);
                        //endregion
                    }
                });
            }

            int id;
            Drawable back1 ,back2;
            Format format = new DecimalFormat("00");
            int start = (int)card.getCalendar();
            start = (start / 100) * 60 + (start % 100);
            int finish = card.getLenTime();
            TextView textView1 = (TextView)view.findViewById(R.id.time);
            TextView textView2 = (TextView)view.findViewById(R.id.content);
            TextView textView3 = (TextView)view.findViewById(R.id.place);
            TextView textView4 = (TextView) view.findViewById(R.id.finish);
            Button button1 = (Button)view.findViewById(R.id.memo);

            //この処理がないとList更新時前のデータが残る
            textView1.setText("");
            textView2.setText("");
            textView3.setText("");
            textView4.setText("");

            textView1.setText(format.format(start/60) + ":" + format.format(start%60));
            textView2.setText(card.getContent());
            textView3.setText(card.getPlace());

            if(!visibleItem[position]) {
                (view.findViewById(R.id.Layout1)).setBackgroundColor(Color.parseColor("#757575"));
                textView1.setBackgroundColor(Color.parseColor("#757575"));
                id = getResources().getIdentifier("dotted_line5", "drawable", getPackageName());
                back1 = ResourcesCompat.getDrawable(getResources(), id, null);
                id = getResources().getIdentifier("dotted_line6", "drawable", getPackageName());
                back2 = ResourcesCompat.getDrawable(getResources(), id, null);
            }else {
                (view.findViewById(R.id.Layout1)).setBackgroundColor(Color.parseColor("#ffffff"));
                textView1.setBackgroundColor(Color.parseColor("#ffffff"));
                id = getResources().getIdentifier("dotted_line2", "drawable", getPackageName());
                back1 = ResourcesCompat.getDrawable(getResources(), id, null);
                id = getResources().getIdentifier("dotted_line3", "drawable", getPackageName());
                back2 = ResourcesCompat.getDrawable(getResources(), id, null);
            }
            textView2.setBackground(back1);
            textView3.setBackground(back2);

            if(card.getMemo() != null) {
                button1.setText("メモ\nあり");
                button1.setBackgroundColor(Color.parseColor("#4CAF50"));
            }else {
                button1.setText("メモ\nなし");
                button1.setBackgroundColor(Color.parseColor("#424242"));
            }
            //終了時刻
            int time = start + finish;
            if (time != start)
                textView4.setText("～" + format.format(time/60) + ":" + format.format(time%60));

            return view;
        }
    }

    //Listの要素更新
    public void updateListFragment(){
        //Collections.sort(cards, new CardComparator());
        cardAdapter = new CardAdapter();
        ((ListView)findViewById(R.id.ListView)).setAdapter(cardAdapter);
        //画面更新
        cardAdapter.notifyDataSetChanged();
    }

    //入力情報の追加位置
    public int addCheck(EventCard card) {
        long target, mine;
        //イベント日の予定がひとつもないとき
        if (scheduleApplication.getEventplancards() == null || (scheduleApplication.getEventplancards() != null && scheduleApplication.getEventplancards().size() == 0))
            return 0;

        //追加する位置を特定
        for (int i = 0; i < scheduleApplication.getEventplancards().size(); i++) {
            target = scheduleApplication.getEventplancards().get(i).getDate();
            mine = card.getDate();
            if (target == mine)
                return -1 * i - 1;
            if (target > mine) {
                if (i == 0)
                    return 0;
                else
                    return i - 1;
            }
        }
        return scheduleApplication.getEventplancards().size();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == UPDATE_CODE){
            //region 予定の更新時
            if(resultCode == RESULT_OK) {
                int position = data.getIntExtra("Position", -1);
                if (position != -1) {
                    if (position == cards.size())
                        cards.add((Card) data.getSerializableExtra("Card"));
                    else
                        cards.add(position, (Card) data.getSerializableExtra("Card"));
                    for(int i = data.getIntExtra("EditPos", -1); i < data.getIntExtra("Position", -1); i++)
                        visibleItem[i] = visibleItem[i+1];
                    visibleItem[data.getIntExtra("Position", -1)] = true;
                }else {
                    //キャンセルしたとき
                    cards.add(data.getIntExtra("EditPos", -1), (Card) data.getSerializableExtra("Card"));
                    if(data.getIntExtra("Event_Index", -1) == -1) {
                        updateListFragment();
                        return;
                    }
                }
            }
            EventModelCard card = new EventModelCard();
            int index = data.getIntExtra("EditPos", -1);
            if(data.getIntExtra("Event_Index", -1) != -1)
                index = data.getIntExtra("Event_Index", -1);
            card.setModelInfo(false, index, (Card) data.getSerializableExtra("Card"));
            eventCard.getCards().add(card);
            //endregion
        }else if(requestCode == MEMO_UPDATE_CODE) {
            //region メモの更新時
            if (resultCode == RESULT_OK) {
                if (data.getStringExtra("Memo") != null && (data.getStringExtra("Memo").length() != 0))
                    memoCard.setMemo(data.getStringExtra("Memo"));
                else {
                    memoCard.setMemo(null);
                    if(data.getIntExtra("Event_Index", -1) == -1) {
                        updateListFragment();
                        return;
                    }
                }

                EventModelCard card = new EventModelCard();
                int index = data.getIntExtra("Event_Index", -1);
                if(index != -1)
                    card.setModelInfo(!visibleItem[memoIndex], index, memoCard);
                else
                    card.setModelInfo(!visibleItem[memoIndex], memoIndex, memoCard);
                eventCard.getCards().add(card);
            }
            //endregion
        }else
            return;
        updateListFragment();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            //戻るボタンを押されたときの処理
            Intent intent = new Intent();
            intent.putExtra("Card", eventCard);
            intent.putExtra("EditPos", getIntent().getIntExtra("Position", -1));
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    //ソート用
    public void sortCards(){
        ArrayList<Card> buffer = new ArrayList<>();
        ArrayList<Boolean> buf = new ArrayList<>();
        boolean next;
        for(int i = 0; i < cards.size(); i++){
            next = false;
            for(int j = 0; j < buffer.size(); j++){
                if(cards.get(i).getCalendar() < buffer.get(j).getCalendar()){
                    buffer.add(j, cards.get(i));
                    buf.add(j, visibleItem[i]);
                    next = true;
                    break;
                }else if(cards.get(i).getCalendar() == buffer.get(j).getCalendar()){
                    //b ソート前のList a ソート後のList
                    int bStart = (int)cards.get(i).getCalendar();
                    bStart = (bStart / 100) * 60 + (bStart % 100);
                    int bFinish = cards.get(i).getLenTime();
                    int bTime = bStart + bFinish;
                    int aStart = (int)buffer.get(j).getCalendar();
                    aStart = (aStart / 100) * 60 + (aStart % 100);
                    int aFinish = buffer.get(j).getLenTime();
                    int aTime = aStart + aFinish;
                    if(bTime > aTime){
                        buffer.add(j + 1, cards.get(i));
                        buf.add(j + 1, visibleItem[i]);
                        next = true;
                    }else {
                        buffer.add(j, cards.get(i));
                        buf.add(j, visibleItem[i]);
                        next = true;
                    }
                    break;
                }
            }
            if(next)
                continue;
            buffer.add(cards.get(i));
            buf.add(visibleItem[i]);
        }

        cards = buffer;
        for(int i = 0; i < cards.size(); i++)
            visibleItem[i] = buf.get(i);
    }
}
