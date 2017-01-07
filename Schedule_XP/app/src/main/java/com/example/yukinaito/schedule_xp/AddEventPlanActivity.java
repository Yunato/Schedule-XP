package com.example.yukinaito.schedule_xp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddEventPlanActivity  extends AppCompatActivity {
    private SchedlueApplication schedlueApplication;
    private AddEventPlanActivity.CardAdapter cardAdapter;
    private static final int UPDATE_CODE = 1;
    private static final int MEMO_UPDATE_CODE = 3;
    private ArrayList<Card> cards;
    private boolean[] check;
    private EventCard eventCard;
    private Card memocard;
    private int plan_Day;
    private int pos;
    private int year, month, day;
    private static int arraypos;
    private static int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addeventplan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("イベント日の追加");

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.colorsimbol), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        schedlueApplication = (SchedlueApplication)this.getApplication();
        findViewById(R.id.button_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventCard.setInfo(plan_Day, pos-1);
                int addcheck = addCheck(eventCard);
                if(addcheck > -1) {
                    final Intent intent = new Intent();
                    intent.putExtra("Card", eventCard);
                    Calendar cal = Calendar.getInstance();
                    year = plan_Day/10000;
                    month = plan_Day%10000/100 - 1;
                    day = plan_Day%100;
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, month);
                    cal.set(Calendar.DAY_OF_MONTH, day);
                    if(cal.get(Calendar.DAY_OF_WEEK) == pos && (eventCard.getCards() == null || (eventCard.getCards() != null && eventCard.getCards().size() == 0))) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddEventPlanActivity.this);
                        builder.setTitle("注意");
                        builder.setMessage("デフォルトで指定されたモデルのままであるため、追加しません。");
                        builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                intent.putExtra("Position", -2);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }else {
                        intent.putExtra("Position", addcheck);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }else{
                    addcheck += 1;
                    addcheck *= -1;
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddEventPlanActivity.this);
                    builder.setTitle("警告");
                    builder.setMessage("他のイベントと日程が重なっています。\n重なっているイベント日:" + schedlueApplication.getEventplancards().get(addcheck).getDate());
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        findViewById(R.id.button_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("Card", eventCard);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        findViewById(R.id.button_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialogfragment datePicker = new DatePickerDialogfragment();
                Bundle bundle = new Bundle();
                bundle.putInt("activity", 4);
                datePicker.setArguments(bundle);
                datePicker.show(getSupportFragmentManager(), "datePicker");
            }
        });
        ((ListView)findViewById(R.id.listview)).setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                AddEventPlanActivity.this.position = position;
                if(check[AddEventPlanActivity.this.position]) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddEventPlanActivity.this);
                    builder.setTitle("モデルの操作");
                    builder.setMessage("モデルの内容を編集、またはモデルを削除しますか？");
                    builder.setPositiveButton("削除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            check[AddEventPlanActivity.this.position] = false;
                            EventModelCard card = new EventModelCard();
                            for(int j = 0; j < eventCard.getCards().size(); j++){
                                if(eventCard.getCards().get(j).getIndex() == AddEventPlanActivity.this.position){
                                    card = eventCard.getCards().get(j);
                                    card.setUpdate(true);
                                    updateListfragment();
                                    return;
                                }
                            }
                            card.setmodelInfo(true, AddEventPlanActivity.this.position, cards.get(AddEventPlanActivity.this.position));
                            eventCard.getCards().add(card);
                            updateListfragment();
                        }
                    });
                    builder.setNegativeButton("編集", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(AddEventPlanActivity.this, AddModelActivity.class);
                            Card card = new Card();
                            card.setInfo(cards.get(AddEventPlanActivity.this.position).getCalendar(),
                                    cards.get(AddEventPlanActivity.this.position).getLentime(),
                                    cards.get(AddEventPlanActivity.this.position).getContent(),
                                    cards.get(AddEventPlanActivity.this.position).getPlace());
                            intent.putExtra("cards", cards);
                            intent.putExtra("EditingCard", card);
                            cards.remove(AddEventPlanActivity.this.position);
                            startActivityForResult(intent, UPDATE_CODE);
                        }
                    });
                    builder.setNeutralButton("キャンセル", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddEventPlanActivity.this);
                    builder.setTitle("モデルの操作");
                    builder.setMessage("この項目を元に戻しますか？");
                    builder.setPositiveButton("戻す", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            check[AddEventPlanActivity.this.position] = true;
                            EventModelCard card = new EventModelCard();
                            for(int j = 0; j < eventCard.getCards().size(); j++){
                                if(eventCard.getCards().get(j).getIndex() == AddEventPlanActivity.this.position){
                                    card = eventCard.getCards().get(j);
                                    card.setUpdate(false);
                                    updateListfragment();
                                    return;
                                }
                            }
                            card.setmodelInfo(false, AddEventPlanActivity.this.position, cards.get(AddEventPlanActivity.this.position));
                            eventCard.getCards().add(card);
                            updateListfragment();
                        }
                    });
                    builder.setNeutralButton("キャンセル", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        (findViewById(R.id.relativelayout1)).setVisibility(View.GONE);
        (findViewById(R.id.relativelayout2)).setVisibility(View.GONE);
        //((Button) findViewById(R.id.button_1)).setEnabled(false);
        ColorDrawable separate_line_color = new ColorDrawable(this.getResources().getColor(R.color.separate_line));
        ((ListView)findViewById(R.id.listview)).setDivider(separate_line_color);
        ((ListView)findViewById(R.id.listview)).setDividerHeight(5);

        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        adapter.add("モデルの選択");
        for(int i = 0; i < schedlueApplication.getModelSchedule().size(); i++)
            adapter.add(schedlueApplication.getModelSchedule().get(i).getName());
        spinner.setAdapter(adapter);

        eventCard = new EventCard();
        plan_Day = -1;
        Calendar cal = Calendar.getInstance();
        year = plan_Day/10000;
        month = plan_Day%10000/100 - 1;
        day = plan_Day%100;
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);

        if ((getIntent().getSerializableExtra("EditingCard")) != null) {
            String name;
            setTitle("日時・行動の変更");
            ((Button) findViewById(R.id.button_1)).setText("更新");
            eventCard = ((EventCard) getIntent().getSerializableExtra("EditingCard"));
            year = eventCard.getDate() / 10000;
            month = eventCard.getDate() % 10000 / 100;
            day = eventCard.getDate() % 100;
            ((Button) findViewById(R.id.button_3)).setText(Integer.toString(year) + "年" + Integer.toString(month) + "月" + Integer.toString(day) + "日(変更時はタップ)");
            plan_Day = eventCard.getDate();
            pos = eventCard.getIndex() + 1;
            if (eventCard.getIndex() > (schedlueApplication.getModelSchedule().size() - 1)) {
                name = schedlueApplication.getEventmodel().get(eventCard.getIndex() - schedlueApplication.getModelSchedule().size()).getName();
            }else {
                name = schedlueApplication.getModelSchedule().get(eventCard.getIndex()).getName();
            }
            boolean exist = false;
            int place = 0;
            for (int i = 0; i < schedlueApplication.getModelSchedule().size(); i++) {
                if (schedlueApplication.getModelSchedule().get(i).getName() .equals(name)) {
                    exist = true;
                    place = i + 1;
                }
            }
            if (!exist) {
                adapter.add(name);
                place = schedlueApplication.getModelSchedule().size() + 1;
            }
            spinner.setSelection(place);
            inputCheck();
            createList();
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner)findViewById(R.id.spinner);
                if(spinner.isFocusable() == false) {
                    spinner.setFocusable(true);
                    return;
                }else{
                    final int b_position = pos;
                    pos = position;
                    if(eventCard.getCards() != null && eventCard.getCards().size() != 0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddEventPlanActivity.this);
                        builder.setTitle("注意");
                        builder.setMessage("モデルの変更により、編集内容を破棄します。(確認を押した後キャンセルを押すと破棄されません。)\nよろしいですか？");
                        builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                eventCard = new EventCard();
                                inputCheck();
                                createList();
                                updateListfragment();
                            }
                        });
                        builder.setNeutralButton("キャンセル", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                pos = b_position;
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }else {
                        inputCheck();
                        eventCard = new EventCard();
                        createList();
                        if(cards != null)
                            updateListfragment();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        ((Spinner)findViewById(R.id.spinner)).setFocusable(false);
    }

    public void onReturnValue(int data, String text, int button) {
        if(button == 1) {
            plan_Day = data;
            Button button_0 = (Button) findViewById(R.id.button_3);
            button_0.setText(text + "(変更時はタップ)");
        }
        inputCheck();
    }

    public void inputCheck() {
        if((((Spinner)findViewById(R.id.spinner)).getSelectedItem()).equals("モデルの選択"))
            (findViewById(R.id.relativelayout2)).setVisibility(View.GONE);
        if ((((Spinner)findViewById(R.id.spinner)).getSelectedItem()).equals("モデルの選択")||plan_Day == -1) {
            (findViewById(R.id.relativelayout1)).setVisibility(View.GONE);
            //((Button) findViewById(R.id.button_1)).setEnabled(false);
            return;
        }
        if (!(((Spinner)findViewById(R.id.spinner)).getSelectedItem()).equals("モデルの選択")&&plan_Day != -1) {
            (findViewById(R.id.relativelayout1)).setVisibility(View.VISIBLE);
            //((Button) findViewById(R.id.button_1)).setEnabled(true);
        }
    }

    public void createList(){
        if(!((Spinner)findViewById(R.id.spinner)).getSelectedItem().toString().equals("モデルの選択")){
            if (eventCard.getIndex() <= (schedlueApplication.getModelSchedule().size() - 1)) {
                (findViewById(R.id.relativelayout2)).setVisibility(View.VISIBLE);
                check = new boolean[schedlueApplication.getModelSchedule().get(pos - 1).getCards().size()];
                cards = new ArrayList<Card>(schedlueApplication.getModelSchedule().get(pos - 1).getCards().size());
                for (int i = 0, j = 0; i < schedlueApplication.getModelSchedule().get(pos - 1).getCards().size(); i++) {
                    if (eventCard.getCards() != null && eventCard.getCards().size() != j) {
                        if (eventCard.getCards().get(j).getIndex() == i) {
                            if (!eventCard.getCards().get(j).getUpdate()) {
                                check[i] = true;
                                Card card = eventCard.getCards().get(j).getCard();
                                cards.add(card);
                            } else {
                                check[i] = false;
                                Card card = schedlueApplication.getModelSchedule().get(pos - 1).getCards().get(i).getCard();
                                cards.add(card);
                            }
                            j++;
                            if (eventCard.getCards().size() != j && eventCard.getCards().get(j).getIndex() == i)
                                i--;
                            continue;
                        }
                    }
                    check[i] = true;
                    Card card = new Card();
                    card = schedlueApplication.getModelSchedule().get(pos - 1).getCards().get(i).getCard();
                    cards.add(card);
                }
            }else{
                int array = eventCard.getIndex() - schedlueApplication.getModelSchedule().size();
                (findViewById(R.id.relativelayout2)).setVisibility(View.VISIBLE);
                check = new boolean[schedlueApplication.getEventmodel().get(array).getCards().size()];
                cards = new ArrayList<Card>(schedlueApplication.getEventmodel().get(array).getCards().size());
                for (int i = 0, j = 0; i < schedlueApplication.getEventmodel().get(array).getCards().size(); i++) {
                    if (eventCard.getCards() != null && eventCard.getCards().size() != j) {
                        if (eventCard.getCards().get(j).getIndex() == i) {
                            if (!eventCard.getCards().get(j).getUpdate()) {
                                check[i] = true;
                                Card card = eventCard.getCards().get(j).getCard();
                                cards.add(card);
                            } else {
                                check[i] = false;
                                Card card = schedlueApplication.getEventmodel().get(array).getCards().get(i).getCard();
                                cards.add(card);
                            }
                            j++;
                            if (eventCard.getCards().size() != j && eventCard.getCards().get(j).getIndex() == i)
                                i--;
                            continue;
                        }
                    }
                    check[i] = true;
                    Card card = new Card();
                    card = schedlueApplication.getEventmodel().get(array).getCards().get(i).getCard();
                    cards.add(card);
                }
            }
            ListView listView = (ListView) findViewById(R.id.listview);
            listView.setScrollingCacheEnabled(false);
            listView.setAdapter(new CardAdapter());
        }
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
        public View getView(int pos, View view, ViewGroup parent){
            //Context context = getActivity();
            Context context = AddEventPlanActivity.this;
            final int array = pos;
            final Card card = cards.get(pos);
            Log.d("CHECK",Integer.toString(pos) +" " +Boolean.toString(check[pos]));
            int id;

            //レイアウトの生成
            if(view == null){
                LinearLayout layout1 = new LinearLayout(context);
                layout1.setBackgroundColor(Color.WHITE);
                layout1.setOrientation(LinearLayout.HORIZONTAL);

                view = layout1;

                FrameLayout layout3 = new FrameLayout(context);
                layout1.addView(layout3);
                layout3.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));

                TextView textView1 = new TextView(context);
                textView1.setTag("time");
                textView1.setTextColor(Color.parseColor("#424242"));
                textView1.setPadding(40, 5, 40, 20);
                textView1.setTextSize(45.0f);
                layout3.addView(textView1);

                TextView textView4 = new TextView(context);
                textView4.setTag("finish");
                textView4.setTextColor(Color.parseColor("#424242"));
                textView4.setPadding(0, 10, 10, 0);
                textView4.setTextSize(15.0f);
                textView4.setGravity(Gravity.RIGHT|Gravity.BOTTOM);
                layout3.addView(textView4);

                LinearLayout layout4 = new LinearLayout(context);
                layout4.setBackgroundColor(Color.WHITE);
                layout4.setPadding(0, 0, 0, 0);
                layout4.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                layout4.setOrientation(LinearLayout.HORIZONTAL);
                layout1.addView(layout4);

                LinearLayout layout2 = new LinearLayout(context);
                layout2.setBackgroundColor(Color.WHITE);
                layout2.setPadding(0, 0, 0, 0);
                layout2.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                layout2.setOrientation(LinearLayout.VERTICAL);
                layout4.addView(layout2, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,1f));

                TextView textView2 = new TextView(context);
                textView2.setTag("content");
                textView2.setTextColor(Color.parseColor("#424242"));
                textView2.setPadding(10, 10, 10, 10);
                textView2.setTextSize(20.0f);
                layout2.addView(textView2, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,1f));

                TextView textView3 = new TextView(context);
                textView3.setTag("place");
                textView3.setTextColor(Color.parseColor("#424242"));
                textView3.setPadding(10, 10, 10, 10);
                textView3.setTextSize(20.0f);
                layout2.addView(textView3, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,1f));

                Button button1 = new Button(context);
                button1.setTag("memo");
                button1.setTextColor(Color.parseColor("#ffffff"));
                button1.setTextSize(20.0f);
                button1.setPadding(10, 10, 10, 10);
                button1.setFocusableInTouchMode(false);
                button1.setFocusable(false);
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        memocard = card;
                        arraypos = array;
                        Intent intent = new Intent(AddEventPlanActivity.this, AddMemoActivity.class);
                        if(card.getMemo() != null)
                            intent.putExtra("memo", card.getMemo());
                        else
                            intent.putExtra("memo", "");
                        startActivityForResult(intent, MEMO_UPDATE_CODE);
                    }
                });
                layout4.addView(button1, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
            }

            Format f = new DecimalFormat("00");
            TextView textView1 = (TextView)view.findViewWithTag("time");
            textView1.setText(f.format(card.getCalendar()/100) + ":" + f.format(card.getCalendar()%100));
            if(!check[pos])
                textView1.setBackgroundColor(Color.parseColor("#757575"));
            else
                textView1.setBackgroundColor(Color.parseColor("#ffffff"));
            TextView textView2 = (TextView)view.findViewWithTag("content");
            textView2.setText(card.getContent());
            if(check[pos])
                id = AddEventPlanActivity.this.getResources().getIdentifier("dotted_line2", "drawable", AddEventPlanActivity.this.getPackageName());
            else
                id = AddEventPlanActivity.this.getResources().getIdentifier("dotted_line5", "drawable", AddEventPlanActivity.this.getPackageName());
            Drawable back = AddEventPlanActivity.this.getResources().getDrawable(id);
            textView2.setBackground(back);
            TextView textView3 = (TextView)view.findViewWithTag("place");
            textView3.setText(card.getPlace());
            if(check[pos])
                id = AddEventPlanActivity.this.getResources().getIdentifier("dotted_line3", "drawable", AddEventPlanActivity.this.getPackageName());
            else
                id = AddEventPlanActivity.this.getResources().getIdentifier("dotted_line6", "drawable", AddEventPlanActivity.this.getPackageName());
            back = AddEventPlanActivity.this.getResources().getDrawable(id);
            textView3.setBackground(back);
            Button button1 = (Button)view.findViewWithTag("memo");
            if(card.getMemo() != null) {
                button1.setText("メモ\nあり");
                button1.setBackgroundColor(Color.parseColor("#4CAF50"));
            }else {
                button1.setText("メモ\nなし");
                button1.setBackgroundColor(Color.parseColor("#424242"));
            }
            long buffer = (card.getCalendar() / 100) * 60 + card.getCalendar() % 100 + card.getLentime();
            long time = (buffer / 60) * 100 + (buffer % 60);
            if (time != card.getCalendar()) {
                TextView textView4 = (TextView) view.findViewWithTag("finish");
                textView4.setText("～" + f.format(time/100) + ":" + f.format(time%100));
            }
            return view;
        }
    }

    public void updateListfragment(){
        cardAdapter = new AddEventPlanActivity.CardAdapter();
        ((ListView)findViewById(R.id.listview)).setAdapter(cardAdapter);
        cardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == UPDATE_CODE){
            if(resultCode == RESULT_OK) {
                int pos = data.getIntExtra("Position", -1);
                if (pos != -1) {
                    if (pos == cards.size())
                        cards.add((Card) data.getSerializableExtra("Card"));
                    else
                        cards.add(pos, (Card) data.getSerializableExtra("Card"));
                    check[AddEventPlanActivity.this.position] = true;
                    EventModelCard card = new EventModelCard();
                    for(int i = 0; i < eventCard.getCards().size(); i++){
                        if(eventCard.getCards().get(i).getIndex() == AddEventPlanActivity.this.position){
                            card = eventCard.getCards().get(i);
                            eventCard.getCards().remove(i);
                        }
                    }
                    card.setmodelInfo(false, AddEventPlanActivity.this.position, (Card) data.getSerializableExtra("Card"));
                    eventCard.getCards().add(card);
                    updateListfragment();
                }else
                    cards.add(position, (Card) data.getSerializableExtra("Card"));
                updateListfragment();
            }
        }else if(requestCode == MEMO_UPDATE_CODE){
            if(resultCode == RESULT_OK) {
                if(data.getStringExtra("Memo") != null && (data.getStringExtra("Memo").length() != 0)){
                    memocard.setMemo(data.getStringExtra("Memo"));
                    EventModelCard card = new EventModelCard();
                    for(int i = 0; i < eventCard.getCards().size(); i++){
                        if(eventCard.getCards().get(i).getIndex() == arraypos){
                            card = eventCard.getCards().get(i);
                            eventCard.getCards().remove(i);
                        }
                    }
                    card.setmodelInfo(!check[arraypos], arraypos, memocard);
                    eventCard.getCards().add(card);
                }else{
                    memocard.setMemo(null);
                }
                updateListfragment();
            }
        }
    }

    public int addCheck(EventCard card) {
        int i;
        long start = 0, end = 0;
        if (schedlueApplication.getEventplancards() == null || (schedlueApplication.getEventplancards() != null && schedlueApplication.getEventplancards().size() == 0))
            return 0;
        Log.d("TEST",Integer.toString(schedlueApplication.getEventplancards().size()));
        for (i = 0; i < schedlueApplication.getEventplancards().size(); i++) {
            start = schedlueApplication.getEventplancards().get(i).getDate();
            end = card.getDate();
            Log.d("TEST",Long.toString(start)+ " " + Long.toString(end));
            if (start == end)
                return -1 * i - 1;
            if (start > end) {
                if (i == 0)
                    return 0;
                else
                    return i - 1;
            }
        }
        return schedlueApplication.getEventplancards().size();
    }
}
