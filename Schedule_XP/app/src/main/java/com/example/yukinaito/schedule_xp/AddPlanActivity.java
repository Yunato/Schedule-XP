package com.example.yukinaito.schedule_xp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddPlanActivity extends AppCompatActivity
        implements TextWatcher {
    private Card card;
    private int plan_Day;
    private int plan_Time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addattime);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("日時・行動の追加");

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.colorsimbol), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        findViewById(R.id.button_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialogfragment datePicker = new DatePickerDialogfragment();
                Bundle bundle = new Bundle();
                bundle.putInt("activity", 1);
                datePicker.setArguments(bundle);
                datePicker.show(getSupportFragmentManager(), "datePicker");
            }
        });
        findViewById(R.id.button_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialogfragment timePicker = new TimePickerDialogfragment();
                Bundle bundle = new Bundle();
                bundle.putInt("activity", 1);
                timePicker.setArguments(bundle);
                timePicker.show(getSupportFragmentManager(), "timePicker");
            }
        });
        findViewById(R.id.button_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, plan_Day/10000);
                plan_Day %= 10000;
                calendar.set(Calendar.MONTH, (plan_Day/100)-1);
                calendar.set(Calendar.DATE, plan_Day%100);
                calendar.set(Calendar.HOUR_OF_DAY, plan_Time/100);
                calendar.set(Calendar.MINUTE, plan_Time%100);
                card.setInfo(calendar,
                        Integer.parseInt(((EditText)findViewById(R.id.editText1)).getText().toString()),
                        ((EditText)findViewById(R.id.editText2)).getText().toString(),
                        ((EditText)findViewById(R.id.editText3)).getText().toString());
                int check = addCheck(card);
                if(check > -1) {
                    Intent intent = new Intent();
                    intent.putExtra("Card", card);
                    intent.putExtra("Position", check);
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    check += 1;
                    check *= -1;
                    final ArrayList<Card> cards = ((SchedlueApplication) getApplication()).getPlanCard();
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddPlanActivity.this);
                    builder.setTitle("警告");
                    builder.setMessage("他の予定と時間が重なっています。\n重なっている予定:" + cards.get(check).getContent());
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
        findViewById(R.id.button_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("Card", card);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        ((EditText)findViewById(R.id.editText1)).addTextChangedListener(this);
        ((EditText)findViewById(R.id.editText2)).addTextChangedListener(this);
        ((EditText)findViewById(R.id.editText3)).addTextChangedListener(this);
        ((Button) findViewById(R.id.button_3)).setEnabled(false);

        card = new Card();
        plan_Day = -1;
        plan_Time = -1;

        if((getIntent().getSerializableExtra("EditingCard")) != null){
            setTitle("日時・行動の変更");
            card = ((Card)getIntent().getSerializableExtra("EditingCard"));
            ((Button)findViewById(R.id.button_1)).setText((new SimpleDateFormat("yyyy年MM月dd日(変更時はタップ)")).format(card.getCalendar().getTime()));
            ((Button)findViewById(R.id.button_2)).setText((new SimpleDateFormat("HH時mm分(変更時はタップ)")).format(card.getCalendar().getTime()));
            ((Button)findViewById(R.id.button_3)).setText("更新");
            ((EditText)findViewById(R.id.editText1)).setText(Integer.toString(card.getLentime()));
            ((EditText)findViewById(R.id.editText2)).setText(card.getContent());
            ((EditText)findViewById(R.id.editText3)).setText(card.getPlace());
            plan_Day = Integer.parseInt((new SimpleDateFormat("yyyyMMdd")).format(card.getCalendar().getTime()));
            plan_Time = Integer.parseInt((new SimpleDateFormat("HHmm")).format(card.getCalendar().getTime()));
        }
    }

    public void onReturnValue(int data, String text, int button) {
        if(button == 1) {
            plan_Day = data;
            Button button_0 = (Button) findViewById(R.id.button_1);
            button_0.setText(text + "(変更時はタップ)");
        }else {
            plan_Time = data;
            Button button_0 = (Button) findViewById(R.id.button_2);
            button_0.setText(text + "(変更時はタップ)");
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
        if(plan_Day==-1||plan_Time==-1||
                (((EditText)findViewById(R.id.editText1)).getText().toString()).equals("")||
                (((EditText)findViewById(R.id.editText2)).getText().toString()).equals("")||
                (((EditText)findViewById(R.id.editText3)).getText().toString()).equals("")) {
            ((Button) findViewById(R.id.button_3)).setEnabled(false);
            return;
        }
        if(plan_Day!=-1&&plan_Time!=-1&&
                !(((EditText)findViewById(R.id.editText1)).getText().toString()).equals("")&&
                !(((EditText)findViewById(R.id.editText2)).getText().toString()).equals("")&&
                !(((EditText)findViewById(R.id.editText3)).getText().toString()).equals("")) {
            ((Button) findViewById(R.id.button_3)).setEnabled(true);
        }
    }

    public int addCheck(Card card){
        int diff;
        boolean check;
        Calendar buffer;
        ArrayList<Card> cards = ((SchedlueApplication)this.getApplication()).getPlanCard();
        if(cards.size() == 0)
            return 0;
        for(int i = 0; i < cards.size(); i++){
            buffer = (Calendar)cards.get(i).getCalendar().clone();
            diff = buffer.compareTo(card.getCalendar());
            check = (new SimpleDateFormat("yyyyMMdd")).format(buffer.getTime()).equals((new SimpleDateFormat("yyyyMMdd")).format(card.getCalendar().getTime()));
            if(check) {
                if(cards.size() - 1 == i) {
                    if(diff > 0){
                        buffer = (Calendar)card.getCalendar().clone();
                        buffer.add(Calendar.MINUTE, card.getLentime());
                        diff = cards.get(i).getCalendar().compareTo(buffer);
                        check = (new SimpleDateFormat("HHmm")).format(cards.get(i).getCalendar().getTime()).equals((new SimpleDateFormat("HHmm")).format(buffer.getTime()));
                        if(diff < 0 && !check)
                            return -1 * i - 1;
                        else
                            return i;
                    }else{
                        buffer = (Calendar)cards.get(i).getCalendar().clone();
                        buffer.add(Calendar.MINUTE, cards.get(i).getLentime());
                        diff = buffer.compareTo(card.getCalendar());
                        check = (new SimpleDateFormat("HHmm")).format(buffer.getTime()).equals((new SimpleDateFormat("HHmm")).format(card.getCalendar().getTime()));
                        if (diff > 0 && !check)
                            return -1 * i - 1;
                        else
                            return cards.size();
                    }
                }
                if(diff > 0){
                    buffer = (Calendar)cards.get(i+1).getCalendar().clone();
                    diff = buffer.compareTo(card.getCalendar());
                    check = (new SimpleDateFormat("HHmm")).format(buffer.getTime()).equals((new SimpleDateFormat("HHmm")).format(card.getCalendar().getTime()));
                    if(check)
                        continue;
                    buffer = (Calendar)cards.get(i-1).getCalendar().clone();
                    buffer.add(Calendar.MINUTE, cards.get(i-1).getLentime());
                    diff = buffer.compareTo(card.getCalendar());
                    check = (new SimpleDateFormat("HHmm")).format(buffer.getTime()).equals((new SimpleDateFormat("HHmm")).format(card.getCalendar().getTime()));
                    if(!(diff > 0) || check) {
                        buffer = (Calendar) card.getCalendar().clone();
                        buffer.add(Calendar.MINUTE, card.getLentime());
                        diff = cards.get(i).getCalendar().compareTo(buffer);
                        check = (new SimpleDateFormat("HHmm")).format(cards.get(i).getCalendar().getTime()).equals((new SimpleDateFormat("HHmm")).format(buffer.getTime()));
                        if (!(diff < 0) || check)
                            return i;
                        else
                            return -1 * i - 1;
                    }
                    else
                        return -1 * i;
                }
            }
            if(diff > 0)
                return i;
        }
        return cards.size();
    }
}