package com.example.yukinaito.schedule_xp;

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

public class AddModelActivity extends AppCompatActivity
    implements TextWatcher    {
        private final static String HOURMINUTE = "HOURMINUTE";
        private final static String ADD = "ADD";
        private final static String CANCEL = "CANCEL";
        private int plan_Time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmodel);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //戻るボタンの色変更
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.colorsimbol), PorterDuff.Mode.SRC_ATOP);
        //getSupportActionBar().setHomeAsUpIndicator(upArrow);

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
                finish();
            }
        });
        findViewById(R.id.button_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((Button)findViewById(R.id.button_2)).setEnabled(false);
        ((EditText)findViewById(R.id.editText2)).addTextChangedListener(this);
        ((EditText)findViewById(R.id.editText3)).addTextChangedListener(this);

        plan_Time = -1;
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
                (((EditText)findViewById(R.id.editText2)).getText().toString()).equals("")||
                (((EditText)findViewById(R.id.editText3)).getText().toString()).equals("")) {
            ((Button)findViewById(R.id.button_2)).setEnabled(false);
            return;
        }
        if(plan_Time!=-1&&
                !(((EditText)findViewById(R.id.editText2)).getText().toString()).equals("")&&
                !(((EditText)findViewById(R.id.editText3)).getText().toString()).equals("")) {
            ((Button)findViewById(R.id.button_2)).setEnabled(true);
        }
    }
}