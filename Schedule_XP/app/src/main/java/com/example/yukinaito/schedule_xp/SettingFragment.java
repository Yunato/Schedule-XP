package com.example.yukinaito.schedule_xp;

import android.app.ActionBar;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class SettingFragment extends ListFragment{
    private static ArrayList<ModelSchedule> model;
    private SchedlueApplication schedlueApplication;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        schedlueApplication = (SchedlueApplication)getActivity().getApplication();
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        model = schedlueApplication.getModelSchedule();
        ArrayList<String> day = new ArrayList<String>();
        for(int i = 0; i < model.size(); i++)
            day.add(model.get(i).getName());
        setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.rowdata, day));
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id){
        Intent intent = new Intent(getActivity(), SettingModelActivity.class);
        intent.putExtra("position", pos);
        startActivity(intent);
    }
}
