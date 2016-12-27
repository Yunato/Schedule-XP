package com.example.yukinaito.schedule_xp;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddAtTimePlan extends AppCompatActivity
        implements View.OnClickListener, TextWatcher {
    private final static String YEARMONTHDAY = "YEARMONTHDAY";
    private final static String HOURMINUTE = "HOURMINUTE";
    private final static String ADD = "ADD";
    private final static String CANCEL = "CANCEL";
    private Card card;
    private int plan_Day;
    private int plan_Time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addattime);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //左上の戻るボタンの表示
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //戻るボタンの色変更
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.colorsimbol), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        ((EditText)findViewById(R.id.editText2)).addTextChangedListener(this);
        ((EditText)findViewById(R.id.editText3)).addTextChangedListener(this);
        ((Button) findViewById(R.id.button_3)).setEnabled(false);

        card = new Card();
        plan_Day = -1;
        plan_Time = -1;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        boolean result = true;

        switch (id) {
            //左上の戻るボタンがおされたおされたとき
            case android.R.id.home:
                finish();
                break;
            default:
                result = super.onOptionsItemSelected(item);
        }

        return result;
    }

    public void onClick(View v){
        String tag = (String)v.getTag();
        if (YEARMONTHDAY.equals(tag)){
            DatePickerDialogfragment datePicker = new DatePickerDialogfragment();
            datePicker.show(getSupportFragmentManager(), "datePicker");
        } else if (HOURMINUTE.equals(tag)){
            TimePickerDialogfragment timePicker = new TimePickerDialogfragment();
            Bundle bundle = new Bundle();
            bundle.putInt("activity", 1);
            timePicker.setArguments(bundle);
            timePicker.show(getSupportFragmentManager(), "timePicker");
        } else if (ADD.equals(tag)){
            try{
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, plan_Day/10000);
                plan_Day %= 10000;
                calendar.set(Calendar.MONTH, plan_Day/100);
                calendar.set(Calendar.DATE, plan_Day%100);
                calendar.set(Calendar.HOUR_OF_DAY, plan_Time/100);
                calendar.set(Calendar.MINUTE, plan_Time%100);
                card.setInfo(calendar,
                        Integer.parseInt(((EditText)findViewById(R.id.editText1)).getText().toString()),
                        ((EditText)findViewById(R.id.editText2)).getText().toString(),
                        ((EditText)findViewById(R.id.editText3)).getText().toString());
                Format f = new DecimalFormat("0000");
                String str = String.format((new SimpleDateFormat("yyyyMMddHHmm")).format(card.getCalendar().getTime())
                        + " " + f.format(card.getLentime()))
                        + " " + card.getContent()
                        + " " + card.getPlace() + "\n";
                FileOutputStream out = openFileOutput("plan.txt",MODE_APPEND|MODE_WORLD_READABLE);
                out.write(str.getBytes());
            }catch(IOException e){
                e.printStackTrace();
            }
            finish();
        } else if (CANCEL.equals(tag)){
            finish();
        }
    }

    //Dialogfragmentで入力が終わった時に呼ばれる処理
    public void onReturnValue(int data, String text, int button) {
        if(button == 1) {
            plan_Day = data;
            Button button_0 = (Button) findViewById(R.id.button_1);
            button_0.setText("年月日:" + text);
        }else {
            plan_Time = data;
            Button button_0 = (Button) findViewById(R.id.button_2);
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
        if(plan_Day==-1||plan_Time==-1||
                (((EditText)findViewById(R.id.editText2)).getText().toString()).equals("")||
                (((EditText)findViewById(R.id.editText3)).getText().toString()).equals("")) {
            ((Button) findViewById(R.id.button_3)).setEnabled(false);
            return;
        }
        if(plan_Day!=-1&&plan_Time!=-1&&
                !(((EditText)findViewById(R.id.editText2)).getText().toString()).equals("")&&
                !(((EditText)findViewById(R.id.editText3)).getText().toString()).equals("")) {
            ((Button) findViewById(R.id.button_3)).setEnabled(true);
        }
    }
}

