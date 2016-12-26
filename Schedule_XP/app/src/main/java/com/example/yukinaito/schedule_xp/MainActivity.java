package com.example.yukinaito.schedule_xp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.SharedPreferencesCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public String ABCDEF = "OK";
    private static ArrayList<ModelSchedule> model;
    private SchedlueApplication schedlueApplication;
    private boolean savecheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        schedlueApplication = (SchedlueApplication)this.getApplication();
        checkState();

        TodayPlanFragment fragment = new TodayPlanFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content_main, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        savecheck = false;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.menu_item0) {
            TodayPlanFragment fragment = new TodayPlanFragment();
            transaction.replace(R.id.content_main, fragment);
        } else if (id == R.id.menu_item1) {
        } else if (id == R.id.menu_item2) {
            AddPlanFragment fragment = new AddPlanFragment();
            transaction.replace(R.id.content_main, fragment);
        } else if (id == R.id.menu_item3) {
        } else if (id == R.id.menu_item4) {
            savecheck = true;
            SettingFragment fragment = new SettingFragment();
            transaction.replace(R.id.content_main, fragment);
        } else if (id == R.id.menu_item5) {
            LocalfileFragment fragment = new LocalfileFragment();
            transaction.replace(R.id.content_main, fragment);
        }

        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void checkState(){
        SharedPreferences preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        if(preference.getBoolean("Launched", false) == false) {

            try {
                OutputStream out = openFileOutput("default.txt", MODE_PRIVATE | MODE_APPEND);
                out.write((getResources().getString(R.string.default_text)).getBytes());
            }catch(IOException e){
            }
            editor.putBoolean("Launched", true).commit();
        }
        schedlueApplication.readModelFile();
    }

    @Override
    public void onDestroy(){
        if(savecheck)
            schedlueApplication.writeModelFile();
        super.onDestroy();
    }
}
