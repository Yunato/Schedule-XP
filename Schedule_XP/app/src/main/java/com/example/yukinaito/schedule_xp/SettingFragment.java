package com.example.yukinaito.schedule_xp;

import android.app.ActionBar;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class SettingFragment extends ListFragment{

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);\
        String[] day = getResources().getStringArray(R.array.day_array);
        setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.rowdata, day));
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id){

    }
}
