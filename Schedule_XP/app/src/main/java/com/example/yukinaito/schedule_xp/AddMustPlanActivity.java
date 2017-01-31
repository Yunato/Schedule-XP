package com.example.yukinaito.schedule_xp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.Format;

public class AddMustPlanActivity extends AppCompatActivity
        implements TextWatcher {
    private MustPlanCard card;
    private int plan_Day1 = -1;
    private int plan_Time1 = -1;
    private int plan_Day2 = -1;
    private int plan_Time2 = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmustplan);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //region 前画面に戻るボタンの生成
        final Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.abc_ic_ab_back_material, null);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorsimbol), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        //endregion

        findViewById(R.id.button_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region 予定の開始の日付を選択 DatePickerの呼び出し
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("activity", 2);
                datePicker.setArguments(bundle);
                datePicker.show(getSupportFragmentManager(), "datePicker");
                //endregion
            }
        });
        findViewById(R.id.button_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region 予定の開始時刻を選択 DatePickerの呼び出し
                TimePickerDialogFragment timePicker = new TimePickerDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("activity", 3);
                timePicker.setArguments(bundle);
                timePicker.show(getSupportFragmentManager(), "timePicker");
                //endregion
            }
        });
        findViewById(R.id.button_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region 予定の終了の日付を選択 DatePickerの呼び出し
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("activity", 3);
                datePicker.setArguments(bundle);
                datePicker.show(getSupportFragmentManager(), "datePicker");
                //endregion
            }
        });
        findViewById(R.id.button_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region 予定の終了時刻を選択 DatePickerの呼び出し
                TimePickerDialogFragment timePicker = new TimePickerDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("activity", 4);
                timePicker.setArguments(bundle);
                timePicker.show(getSupportFragmentManager(), "timePicker");
                //endregion
            }
        });
        findViewById(R.id.button_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region 情報をすべて入力後
                card = new MustPlanCard();
                long start = (long)plan_Day1 * 10000 + (long)plan_Time1;
                long end = (long)plan_Day2 * 10000 + (long)plan_Time2;
                card.setInfo(((EditText)findViewById(R.id.editText1)).getText().toString(),
                        false, start, end,
                        Integer.parseInt(((EditText)findViewById(R.id.editText2)).getText().toString()),
                        ((EditText)findViewById(R.id.editText3)).getText().toString());
                Intent intent = new Intent();
                intent.putExtra("Card", card);
                intent.putExtra("EditPos", getIntent().getIntExtra("Position", -1));
                setResult(RESULT_OK, intent);
                finish();
                //endregion
            }
        });
        findViewById(R.id.button_6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region キャンセルボタンタップ時
                Intent intent = new Intent();
                intent.putExtra("Card", card);
                intent.putExtra("EditPos", getIntent().getIntExtra("Position", -1));
                setResult(RESULT_OK, intent);
                finish();
                //endregion
            }
        });

        ((EditText)findViewById(R.id.editText1)).addTextChangedListener(this);
        ((EditText)findViewById(R.id.editText2)).addTextChangedListener(this);
        ((EditText)findViewById(R.id.editText3)).addTextChangedListener(this);
        (findViewById(R.id.button_5)).setEnabled(false);

        //region 分岐する起動処理
        if((getIntent().getSerializableExtra("EditingCard")) != null){
            //編集の場合 編集する予定の情報を反映させる
            setTitle("やるべきことの変更");
            card = ((MustPlanCard)getIntent().getSerializableExtra("EditingCard"));
            Format f1 = new DecimalFormat("0000");
            Format f2 = new DecimalFormat("00");
            String start = Long.toString(card.getStart());
            ((Button)findViewById(R.id.button_1)).setText(start.substring(0, 4) + "年" + start.substring(4, 6) + "月" + start.substring(6, 8) + "日");
            ((Button)findViewById(R.id.button_2)).setText(start.substring(8, 10) + "時" + start.substring(10, 12) + "分");
            String end = Long.toString(card.getLimit());
            ((Button)findViewById(R.id.button_3)).setText(end.substring(0, 4) + "年" + end.substring(4, 6) + "月" + end.substring(6, 8) + "日");
            ((Button)findViewById(R.id.button_4)).setText(end.substring(8, 10) + "時" + end.substring(10, 12) + "分");
            ((Button)findViewById(R.id.button_5)).setText("更新");
            ((EditText)findViewById(R.id.editText1)).setText(card.getName());
            ((EditText)findViewById(R.id.editText2)).setText(Integer.toString(card.getForCast()));
            ((EditText)findViewById(R.id.editText3)).setText(card.getPlace());
            plan_Day1 = (int)(card.getStart() / 10000);
            plan_Time1 = (int)(card.getStart() % 10000);
            plan_Day2 = (int)(card.getLimit() / 10000);
            plan_Time2 = (int)(card.getLimit() % 10000);
        }else {
            setTitle("やるべきことの追加");
        }
        //endregion
    }

    public void onReturnValue(int data, String text, int picker) {
        if(picker == 1) {
            plan_Day1 = data;
            Button button = (Button) findViewById(R.id.button_1);
            button.setText(text);
        }else if(picker == 2) {
            plan_Time1 = data;
            Button button = (Button) findViewById(R.id.button_2);
            button.setText(text);
        }else if(picker == 3) {
            plan_Day2 = data;
            Button button = (Button) findViewById(R.id.button_3);
            button.setText(text);
        }else if(picker == 4) {
            plan_Time2 = data;
            Button button = (Button) findViewById(R.id.button_4);
            button.setText(text);
        }
        inputCheck();
    }

    //region EditTextのリスナーに登録された3つのイベント inputCheck()のみ
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
    //endregion

    //入力チェック
    public void inputCheck(){
        if(plan_Day1==-1||plan_Time1==-1||plan_Day2==-1||plan_Time2==-1||
                (((EditText)findViewById(R.id.editText1)).getText().toString()).equals("")||
                (((EditText)findViewById(R.id.editText2)).getText().toString()).equals("")||
                (((EditText)findViewById(R.id.editText3)).getText().toString()).equals(""))
            (findViewById(R.id.button_5)).setEnabled(false);
        if(plan_Day1!=-1&&plan_Time1!=-1&&plan_Day2!=-1&&plan_Time2!=-1&&
                !(((EditText)findViewById(R.id.editText1)).getText().toString()).equals("")&&
                !(((EditText)findViewById(R.id.editText2)).getText().toString()).equals("")&&
                !(((EditText)findViewById(R.id.editText3)).getText().toString()).equals(""))
            (findViewById(R.id.button_5)).setEnabled(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            //戻るボタンを押されたときの処理
            Intent intent = new Intent();
            intent.putExtra("Card", card);
            intent.putExtra("EditPos", getIntent().getIntExtra("Position", -1));
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
