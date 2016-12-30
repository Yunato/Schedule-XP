package com.example.yukinaito.schedule_xp;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddEventPlanActivity  extends AppCompatActivity {
    private SchedlueApplication schedlueApplication;
    private EventCard card;
    private int plan_Day;
    private int pos;
    private int year, month, day;

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
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, plan_Day / 10000);
                plan_Day %= 10000;
                calendar.set(Calendar.MONTH, (plan_Day / 100) - 1);
                calendar.set(Calendar.DATE, plan_Day % 100);
                card.setInfo(calendar, pos-1);
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

        ((Button) findViewById(R.id.button_1)).setEnabled(false);

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
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        card = new EventCard();
        plan_Day = year * 10000 + month * 100 + day;

        if ((getIntent().getSerializableExtra("EditingCard")) != null) {
            setTitle("日時・行動の変更");
            card = ((EventCard) getIntent().getSerializableExtra("EditingCard"));
            year = card.getDate().getInstance().get(Calendar.YEAR);
            month = card.getDate().getInstance().get(Calendar.MONTH);
            day = card.getDate().getInstance().get(Calendar.DAY_OF_MONTH);
            plan_Day = Integer.parseInt((new SimpleDateFormat("yyyyMMdd")).format(card.getDate().getTime()));
        }

        final DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);

        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener(){
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                plan_Day = year * 10000 + monthOfYear * 100 + dayOfMonth;
                inputCheck();
            }
        });
    }

    public void inputCheck() {
        if ((((Spinner)findViewById(R.id.spinner)).getSelectedItem()).equals("モデルの選択")) {
            ((Button) findViewById(R.id.button_1)).setEnabled(false);
            return;
        }
        if (!(((Spinner)findViewById(R.id.spinner)).getSelectedItem()).equals("モデルの選択")) {
            ((Button) findViewById(R.id.button_1)).setEnabled(true);
        }
    }
}
