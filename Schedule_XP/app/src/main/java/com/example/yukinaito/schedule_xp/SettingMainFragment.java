package com.example.yukinaito.schedule_xp;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SettingMainFragment extends ListFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.updatemodelday, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        /*
        String[] day = getResources().getStringArray(R.array.day_array);
        setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.rowdata, day));*/
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id){

    }
}
