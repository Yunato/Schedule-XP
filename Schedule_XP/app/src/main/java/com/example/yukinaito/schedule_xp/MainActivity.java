package com.example.yukinaito.schedule_xp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_WRITE_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //アプリケーション起動処理
        boolean hasPermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if(!hasPermission){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }
    }

    @Override
    public void onBackPressed() {
        //Backキータップ時の処理
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            //drawerを閉じる
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //アプリを閉じる
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        //StackされたFragmentをClearする
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.menu_item0) {
            setTitle("本日の予定");
            ShowScheduleFragment fragment = new ShowScheduleFragment();
            transaction.replace(R.id.content_main, fragment);
        } else if (id == R.id.menu_item1) {
            setTitle("予定の確認・追加");
            CheckMainFragment fragment = new CheckMainFragment();
            transaction.replace(R.id.content_main, fragment);
        } else if (id == R.id.menu_item2) {

        } else if (id == R.id.menu_item3) {
            setTitle("モデル一覧");
            SettingMainFragment fragment = new SettingMainFragment();
            transaction.replace(R.id.content_main, fragment);
        } else if (id == R.id.menu_item4) {
            Intent intent = new Intent(this, TestActivity.class);
            startActivity(intent);
        }

        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //初回起動であるか判別 起動処理
    public void checkState(){
        SharedPreferences preference = getSharedPreferences("Preference", MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        if(preference.getBoolean("Launched", false) == false) {
            ((ScheduleApplication)this.getApplication()).getModelInfo();
            Calendar week = Calendar.getInstance();
            week.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            SimpleDateFormat weekFormatter = new SimpleDateFormat("E曜日");
            for(int i = 0; i < 7; i++){
                ModelCard modelCard = new ModelCard(null, weekFormatter.format(week.getTime()), true);
                ((ScheduleApplication)this.getApplication()).saveCard(modelCard);
                week.add(Calendar.DAY_OF_MONTH, 1);
            }
            Card addCard = new Card(0, 0, 0, false, "dateindexは、したいことに費やした合計時間", "null");
            ((ScheduleApplication)this.getApplication()).saveData(addCard);
            editor.putBoolean("Launched", true).commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_WRITE_STORAGE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                checkState();
        }
    }
}