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
            Bundle bundle = new Bundle();
            bundle.putSerializable("ModelSchedule", model);
            SettingFragment fragment = new SettingFragment();
            fragment.setArguments(bundle);
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
        readModelFile();
    }

    public void readModelFile(){
        model = new ArrayList<ModelSchedule>();
        String[] buffer = new String[4];
        String tmp;
        char c;
        try{
            Log.d("test","OK1");
            FileInputStream in = openFileInput("default.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            while((tmp = reader.readLine())!=null) {
                Log.d("test","OK2");
                Arrays.fill(buffer, "");
                for (int i = 0, j = 0; i < tmp.length(); i++) {
                    c = tmp.charAt(i);
                    if (c == ' ') {
                        j++;
                        continue;
                    }
                    buffer[j] += c;
                }
                ModelSchedule modelSch = new ModelSchedule();
                modelSch.setName(buffer[2]);
                Log.d("test","OK3");
                int count = Integer.parseInt(buffer[1]);
                Log.d("test","OK3.5");
                Log.d("test",tmp);
                Log.d("test",Integer.toString(count));
                for (int i = 0; i < count; i++) {
                    tmp = reader.readLine();
                    Arrays.fill(buffer, "");
                    for(int j = 0, k = 0; j < tmp.length(); j++){
                        c = tmp.charAt(j);
                        if (c == ' ') {
                            k++;
                            continue;
                        }
                        buffer[k] += c;
                    }
                    Log.d("text","OK5");
                    modelSch.setCardproperty(Integer.parseInt(buffer[0]),
                            Integer.parseInt(buffer[1]),
                            buffer[2], buffer[3]);
                }
                Log.d("test","OK6");
                model.add(modelSch);
            }
        }catch(IOException e){
        }
    }
}
