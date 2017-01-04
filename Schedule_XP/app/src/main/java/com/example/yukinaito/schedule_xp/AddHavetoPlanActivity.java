package com.example.yukinaito.schedule_xp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.Format;

public class AddHavetoPlanActivity extends AppCompatActivity
        implements TextWatcher {
    private HavetoPlanCard card;
    private int plan_Day1;
    private int plan_Time1;
    private int plan_Day2;
    private int plan_Time2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addhavetoplan);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("やるべきことの追加");

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.colorsimbol), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        findViewById(R.id.button_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialogfragment datePicker = new DatePickerDialogfragment();
                Bundle bundle = new Bundle();
                bundle.putInt("activity", 2);
                datePicker.setArguments(bundle);
                datePicker.show(getSupportFragmentManager(), "datePicker");
            }
        });
        findViewById(R.id.button_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialogfragment timePicker = new TimePickerDialogfragment();
                Bundle bundle = new Bundle();
                bundle.putInt("activity", 3);
                timePicker.setArguments(bundle);
                timePicker.show(getSupportFragmentManager(), "timePicker");
            }
        });
        findViewById(R.id.button_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialogfragment datePicker = new DatePickerDialogfragment();
                Bundle bundle = new Bundle();
                bundle.putInt("activity", 3);
                datePicker.setArguments(bundle);
                datePicker.show(getSupportFragmentManager(), "datePicker");
            }
        });
        findViewById(R.id.button_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialogfragment timePicker = new TimePickerDialogfragment();
                Bundle bundle = new Bundle();
                bundle.putInt("activity", 4);
                timePicker.setArguments(bundle);
                timePicker.show(getSupportFragmentManager(), "timePicker");
            }
        });
        findViewById(R.id.button_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long start = (long)plan_Day1 * 10000 + (long)plan_Time1;
                long end = (long)plan_Day2 * 10000 + (long)plan_Time2;
                card.setInfo(((EditText)findViewById(R.id.editText2)).getText().toString(),
                        false, start, end,
                        Integer.parseInt(((EditText)findViewById(R.id.editText1)).getText().toString()),
                        ((EditText)findViewById(R.id.editText3)).getText().toString());
                Intent intent = new Intent();
                intent.putExtra("Card", card);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        findViewById(R.id.button_6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        ((EditText)findViewById(R.id.editText1)).addTextChangedListener(this);
        ((EditText)findViewById(R.id.editText2)).addTextChangedListener(this);
        ((EditText)findViewById(R.id.editText3)).addTextChangedListener(this);
        ((Button) findViewById(R.id.button_5)).setEnabled(false);

        card = new HavetoPlanCard();
        plan_Day1 = -1;
        plan_Time1 = -1;
        plan_Day2 = -1;
        plan_Time2 = -1;

        if((getIntent().getSerializableExtra("EditingCard")) != null){
            setTitle("やるべきことの変更");
            card = ((HavetoPlanCard)getIntent().getSerializableExtra("EditingCard"));
            Format f1 = new DecimalFormat("0000");
            Format f2 = new DecimalFormat("00");
            long start = card.getStart() % 100000000;
            ((Button)findViewById(R.id.button_1)).setText(f1.format(card.getStart() / 100000000) + "年" + f2.format(start/1000000) + "月" + f2.format((start%1000000)/10000) + "日");
            start = card.getStart() % 10000;
            ((Button)findViewById(R.id.button_2)).setText(f2.format(start/100) + "時" + f2.format(start%100) + "分");
            long end = card.getLimit() % 100000000;
            ((Button)findViewById(R.id.button_3)).setText(f1.format(card.getLimit() / 100000000) + "年" + f2.format(end/1000000) + "月" + f2.format((end%1000000)/10000) + "日");
            end = card.getLimit() % 10000;
            ((Button)findViewById(R.id.button_4)).setText(f2.format(end/100) + "時" + f2.format(end%100) + "分");
            ((Button)findViewById(R.id.button_5)).setText("更新");
            ((EditText)findViewById(R.id.editText1)).setText(Integer.toString(card.getForcast()));
            ((EditText)findViewById(R.id.editText2)).setText(card.getName());
            ((EditText)findViewById(R.id.editText3)).setText(card.getPlace());
            plan_Day1 = (int)(card.getStart() / 10000);
            plan_Time1 = (int)(card.getStart() % 10000);
            plan_Day2 = (int)(card.getLimit() / 10000);
            plan_Time2 = (int)(card.getLimit() % 10000);
        }
    }

    public void onReturnValue(int data, String text, int button) {
        if(button == 1) {
            plan_Day1 = data;
            Button button_0 = (Button) findViewById(R.id.button_1);
            button_0.setText(text);
        }else if(button == 2) {
            plan_Time1 = data;
            Button button_0 = (Button) findViewById(R.id.button_2);
            button_0.setText(text);
        }else if(button == 3) {
            plan_Day2 = data;
            Button button_0 = (Button) findViewById(R.id.button_3);
            button_0.setText(text);
        }else if(button == 4) {
            plan_Time2 = data;
            Button button_0 = (Button) findViewById(R.id.button_4);
            button_0.setText(text);
        }
        inputCheck();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        inputCheck();
    }

    public void inputCheck(){
        if(plan_Day1==-1||plan_Time1==-1||plan_Day2==-1||plan_Time2==-1||
                (((EditText)findViewById(R.id.editText1)).getText().toString()).equals("")||
                (((EditText)findViewById(R.id.editText2)).getText().toString()).equals("")||
                (((EditText)findViewById(R.id.editText3)).getText().toString()).equals("")) {
            ((Button) findViewById(R.id.button_5)).setEnabled(false);
            return;
        }
        if(plan_Day1!=-1&&plan_Time1!=-1&&plan_Day2!=-1&&plan_Time2!=-1&&
                !(((EditText)findViewById(R.id.editText1)).getText().toString()).equals("")&&
                !(((EditText)findViewById(R.id.editText2)).getText().toString()).equals("")&&
                !(((EditText)findViewById(R.id.editText3)).getText().toString()).equals("")) {
            ((Button) findViewById(R.id.button_5)).setEnabled(true);
        }
    }
}
