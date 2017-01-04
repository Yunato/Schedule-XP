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

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddModelActivity extends AppCompatActivity
        implements TextWatcher    {
    private ArrayList<Card> cards;
    private Card card;
    private int plan_Time;

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

        cards = (ArrayList<Card>) getIntent().getSerializableExtra("cards");
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
                long calendar = (long)plan_Time;
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
            Format f = new DecimalFormat("00");
            ((Button)findViewById(R.id.button_1)).setText(f.format(card.getCalendar()/100) + "時" + f.format(card.getCalendar()%100) + "分(変更時はタップ)");
            ((Button)findViewById(R.id.button_2)).setText("更新");
            ((EditText)findViewById(R.id.editText1)).setText(Integer.toString(card.getLentime()));
            ((EditText)findViewById(R.id.editText2)).setText(card.getContent());
            ((EditText)findViewById(R.id.editText3)).setText(card.getPlace());
            plan_Time = (int)card.getCalendar();
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

    public int addCheck(Card card) {
        int i;
        long start1 = 0, start2 = 0, end1 = 0, end2 = 0;
        if (cards.size() == 0)
            return 0;
        for (i = 0; i < cards.size(); i++) {
            start1 = cards.get(i).getCalendar();
            end1 = cards.get(i).getCalendar() + cards.get(i).getLentime();
            start2 = card.getCalendar();
            end2 = card.getCalendar() + card.getLentime();
            if (start1 > start2) {
                if (!(start1 == start2) || !(start2 == end2)) {
                    if (start1 < end2 && start2 < end1)
                        return -1 * i - 1;
                    else {
                        if (i == 0)
                            return i;
                        start1 = cards.get(i - 1).getCalendar();
                        end1 = cards.get(i - 1).getCalendar() + cards.get(i - 1).getLentime();
                        if (start1 < end2 && start2 < end1)
                            return -1 * (i - 1) - 1;
                        else {
                            if (start2 < end1)
                                return i - 1;
                            else
                                return i;
                        }
                    }
                } else
                    return i - 1;
            }
        }
        if (!(start1 == start2) || !(start2 == end2)) {
            if (start1 < end2 && start2 < end1)
                return -1 * i;
            else
                return cards.size();
        } else
            return cards.size() - 1;
    }
}