package com.example.yukinaito.schedule_xp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
    private ArrayList<Card> cards;
    private EventCard card;
    private int plan_Day;
    private int pos;
    private int year, month, day;
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
                card.setInfo(plan_Day, pos-1);
                Intent intent = new Intent();
                intent.putExtra("Card", card);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        findViewById(R.id.button_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(AddEventPlanActivity.this);
                builder.setTitle("モデルの操作");
                builder.setMessage("モデルの内容を編集、またはモデルを削除しますか？");
                builder.setPositiveButton("削除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cards.remove(AddEventPlanActivity.this.position);
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
                        startActivityForResult(intent,UPDATE_CODE);
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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                inputCheck();
                createList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        card = new EventCard();
        plan_Day = -1;

        if ((getIntent().getSerializableExtra("EditingCard")) != null) {
            setTitle("日時・行動の変更");
            card = ((EventCard) getIntent().getSerializableExtra("EditingCard"));
            year = card.getDate()/10000;
            month = card.getDate()%10000/100 + 1;
            day = card.getDate()%100;
            ((Button)findViewById(R.id.button_3)).setText(Integer.toString(year) + "年" + Integer.toString(month) + "月" + Integer.toString(day) + "日(変更時はタップ)");
            plan_Day = card.getDate();
            spinner.setSelection(card.getIndex()+1);
        }
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
        if(pos != 0){
            (findViewById(R.id.relativelayout2)).setVisibility(View.VISIBLE);
            cards = new ArrayList<Card>(schedlueApplication.getModelSchedule().get(pos-1).getCards().size());
            for(int i = 0; i < schedlueApplication.getModelSchedule().get(pos-1).getCards().size(); i++){
                Card card = new Card();
                card = schedlueApplication.getModelSchedule().get(pos-1).getCards().get(i).getCard();
                cards.add(card);
            }
            ListView listView = (ListView)findViewById(R.id.listview);
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
            Card card = cards.get(pos);

            //レイアウトの生成
            if(view == null){
                LinearLayout layout = new LinearLayout(context);
                layout.setBackgroundColor(Color.WHITE);
                layout.setOrientation(LinearLayout.HORIZONTAL);

                view = layout;

                FrameLayout layout3 = new FrameLayout(context);
                layout.addView(layout3);
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

                LinearLayout layout2 = new LinearLayout(context);
                layout2.setBackgroundColor(Color.WHITE);
                layout2.setPadding(0, 0, 0, 0);
                layout2.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                layout2.setOrientation(LinearLayout.VERTICAL);

                TextView textView2 = new TextView(context);
                textView2.setTag("content");
                textView2.setTextColor(Color.parseColor("#424242"));
                textView2.setPadding(10, 10, 10, 10);
                textView2.setTextSize(20.0f);
                int id = AddEventPlanActivity.this.getResources().getIdentifier("dotted_line2", "drawable", AddEventPlanActivity.this.getPackageName());
                Drawable back = AddEventPlanActivity.this.getResources().getDrawable(id);
                textView2.setBackground(back);
                layout2.addView(textView2, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,1f));

                TextView textView3 = new TextView(context);
                textView3.setTag("place");
                textView3.setTextColor(Color.parseColor("#424242"));
                textView3.setPadding(10, 10, 10, 10);
                textView3.setTextSize(20.0f);
                id = AddEventPlanActivity.this.getResources().getIdentifier("dotted_line3", "drawable", AddEventPlanActivity.this.getPackageName());
                back = AddEventPlanActivity.this.getResources().getDrawable(id);
                textView3.setBackground(back);
                layout2.addView(textView3, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,1f));

                layout.addView(layout2);
            }

            Format f = new DecimalFormat("00");
            TextView textView1 = (TextView)view.findViewWithTag("time");
            textView1.setText(f.format(card.getCalendar()/100) + ":" + f.format(card.getCalendar()%100));
            TextView textView2 = (TextView)view.findViewWithTag("content");
            textView2.setText(card.getContent());
            TextView textView3 = (TextView)view.findViewWithTag("place");
            textView3.setText(card.getPlace());
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
                }else
                    cards.add(position, (Card) data.getSerializableExtra("Card"));
                updateListfragment();
            }
        }
    }
}
