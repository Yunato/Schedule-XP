package com.example.yukinaito.schedule_xp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SettingModelActivity extends AppCompatActivity {
    private boolean visible = false;
    private SchedlueApplication schedlueApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_model);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //左上の戻るボタンの表示
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //戻るボタンの色変更
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.colorsimbol), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        if((int)getIntent().getSerializableExtra("position") > 6)
            visible = true;

        schedlueApplication = (SchedlueApplication)this.getApplication();
        SettingMainFragment fragment = new SettingMainFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        ModelSchedule model = schedlueApplication.getModelSchedule().get((int)getIntent().getSerializableExtra("position"));

        bundle.putSerializable("day_information", model);
        bundle.putSerializable("position", (int)getIntent().getSerializableExtra("position"));
        fragment.setArguments(bundle);
        transaction.add(R.id.activity_setting_model, fragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if(visible)
            getMenuInflater().inflate(R.menu.modeldelete,menu);
        return true;
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

    @Override
    public void onDestroy(){
        schedlueApplication.writeModelFile();
        super.onDestroy();
    }
}
