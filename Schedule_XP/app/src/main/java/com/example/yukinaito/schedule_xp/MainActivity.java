package com.example.yukinaito.schedule_xp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
        checkState();
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
            // Handle the camera action
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

        }

        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //初回起動であるか判別 起動処理
    public void checkState(){
        SharedPreferences preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        if(preference.getBoolean("Launched", false) == false) {
            try {
                //初回起動時にてデフォルトファイルの生成
                OutputStream out = openFileOutput("modelName.txt", MODE_PRIVATE | MODE_APPEND);
                out.write((getResources().getString(R.string.modelName_text)).getBytes());
            }catch(IOException e){
            }
            editor.putBoolean("Launched", true).commit();
        }
    }
}
