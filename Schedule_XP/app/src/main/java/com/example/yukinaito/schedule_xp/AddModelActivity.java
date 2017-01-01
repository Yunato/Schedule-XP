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

public class AddModelActivity extends AppCompatActivity
        implements TextWatcher    {
    private Card card;
    private int plan_Time;
    private static int arraypos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmodel);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("ひな形(モデル)の追加");

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.colorsimbol), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        arraypos = getIntent().getIntExtra("position", -1);
        findViewById(R.id.button_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialogfragment timePicker = new TimePickerDialogfragment();
                Bundle bundle = new Bundle();
                bundle.putInt("activity", 2);
                timePicker.setArguments(bundle);
                timePicker.show(getSupportFragmentManager(), "timePicker");
            }
        });
        findViewById(R.id.button_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card.setInfo(plan_Time,
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
                    final ArrayList<Card> cards = ((SchedlueApplication) getApplication()).getModelSchedule().get(arraypos).getCards();
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddModelActivity.this);
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
        findViewById(R.id.button_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                card.setInfo(plan_Time,
                        Integer.parseInt(((EditText)findViewById(R.id.editText1)).getText().toString()),
                        ((EditText)findViewById(R.id.editText2)).getText().toString(),
                        ((EditText)findViewById(R.id.editText3)).getText().toString());
                intent.putExtra("Card", card);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        ((Button)findViewById(R.id.button_2)).setEnabled(false);
        ((EditText)findViewById(R.id.editText1)).addTextChangedListener(this);
        ((EditText)findViewById(R.id.editText2)).addTextChangedListener(this);
        ((EditText)findViewById(R.id.editText3)).addTextChangedListener(this);

        card = new Card();
        plan_Time = -1;

        if((getIntent().getSerializableExtra("EditingCard")) != null){
            setTitle("ひな形(モデル)の変更");
            card = ((Card)getIntent().getSerializableExtra("EditingCard"));
            ((Button)findViewById(R.id.button_1)).setText((new SimpleDateFormat("HH時mm分(変更時はタップ)")).format(card.getCalendar().getTime()));
            ((Button)findViewById(R.id.button_2)).setText("更新");
            ((EditText)findViewById(R.id.editText1)).setText(Integer.toString(card.getLentime()));
            ((EditText)findViewById(R.id.editText2)).setText(card.getContent());
            ((EditText)findViewById(R.id.editText3)).setText(card.getPlace());
            plan_Time = Integer.parseInt((new SimpleDateFormat("HHmm")).format(card.getCalendar().getTime()));
        }
    }

    public void onReturnValue(int data, String text, int button) {
        if(button == 2) {
            plan_Time = data;
            Button button_0 = (Button)findViewById(R.id.button_1);
            button_0.setText("時刻:" + text);
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
        if(plan_Time==-1||
                (((EditText)findViewById(R.id.editText1)).getText().toString()).equals("")||
                (((EditText)findViewById(R.id.editText2)).getText().toString()).equals("")||
                (((EditText)findViewById(R.id.editText3)).getText().toString()).equals("")) {
            ((Button)findViewById(R.id.button_2)).setEnabled(false);
            return;
        }
        if(plan_Time!=-1&&
                !(((EditText)findViewById(R.id.editText1)).getText().toString()).equals("")&&
                !(((EditText)findViewById(R.id.editText2)).getText().toString()).equals("")&&
                !(((EditText)findViewById(R.id.editText3)).getText().toString()).equals("")) {
            ((Button)findViewById(R.id.button_2)).setEnabled(true);
        }
    }

    public int addCheck(Card card){
        int diff;
        boolean check;
        Calendar buffer1, buffer2;
        ArrayList<Card> cards = ((SchedlueApplication)this.getApplication()).getModelSchedule().get(arraypos).getCards();
        if(cards.size() == 0)
            return 0;
        for(int i = 0; i < cards.size(); i++){
            buffer1 = (Calendar)cards.get(i).getCalendar().clone();
            diff = buffer1.compareTo(card.getCalendar());
            check = (new SimpleDateFormat("HHmm")).format(buffer1.getTime()).equals((new SimpleDateFormat("HHmm")).format(card.getCalendar().getTime()));
            if(cards.size() - 1 == i) {
                if(diff > 0){
                    buffer1 = (Calendar)cards.get(i).getCalendar().clone();
                    buffer2 = (Calendar)card.getCalendar().clone();
                    buffer2.add(Calendar.MINUTE, card.getLentime());
                    diff = buffer1.compareTo(buffer2);
                    check = (new SimpleDateFormat("HHmm")).format(buffer1.getTime()).equals((new SimpleDateFormat("HHmm")).format(buffer2.getTime()));
                    if(diff < 0 && !check)
                        return -1 * i - 1;
                    else
                        return i;
                }else{
                    buffer1 = (Calendar)cards.get(i).getCalendar().clone();
                    buffer1.add(Calendar.MINUTE, cards.get(i).getLentime());
                    buffer2 = (Calendar)card.getCalendar().clone();
                    diff = buffer1.compareTo(buffer2);
                    check = (new SimpleDateFormat("HHmm")).format(buffer1.getTime()).equals((new SimpleDateFormat("HHmm")).format(buffer2.getTime()));
                    if (diff > 0 && !check)
                        return -1 * i - 1;
                    else
                        return cards.size();
                }
            }
            if(diff > 0){
                buffer1 = (Calendar)cards.get(i-1).getCalendar().clone();
                buffer1.add(Calendar.MINUTE, cards.get(i-1).getLentime());
                diff = buffer1.compareTo(card.getCalendar());
                check = (new SimpleDateFormat("HHmm")).format(buffer1.getTime()).equals((new SimpleDateFormat("HHmm")).format(card.getCalendar().getTime()));
                if(!(diff > 0) || check){
                    if(cards.size()  == i)
                        return i;
                    buffer1 = (Calendar)cards.get(i).getCalendar().clone();
                    buffer2 = (Calendar)card.getCalendar().clone();
                    buffer2.add(Calendar.MINUTE, card.getLentime());
                    diff = buffer1.compareTo(buffer2);
                    check = (new SimpleDateFormat("HHmm")).format(buffer1.getTime()).equals((new SimpleDateFormat("HHmm")).format(buffer2.getTime()));
                    if(!(diff < 0) || check)
                        return i-1;
                    else
                        return -1 * i-1;
                }
                else
                    return -1 * i;
            }
        }
        return cards.size();
    }
}