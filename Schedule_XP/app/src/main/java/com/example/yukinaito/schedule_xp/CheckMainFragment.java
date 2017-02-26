package com.example.yukinaito.schedule_xp;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CheckMainFragment extends ListFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_checkmain, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //ListViewの区切り線
        ColorDrawable separate_line_color = new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.separate_line));
        getListView().setDivider(separate_line_color);
        getListView().setDividerHeight(5);
    }

    //要素のタップ時
    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        if(position == 0){
            getActivity().setTitle("日時・行動一覧");
            CheckPlanFragment fragment = new CheckPlanFragment();
            transaction.replace(R.id.content_main, fragment);
        }else if(position == 1){
            getActivity().setTitle("したいこと一覧");
            CheckAddWantFragment fragment = new CheckAddWantFragment();
            transaction.replace(R.id.content_main, fragment);
        }else if(position == 2){
            getActivity().setTitle("すべきこと一覧");
            CheckMustFragment fragment = new CheckMustFragment();
            transaction.replace(R.id.content_main, fragment);
        }else if(position == 3){
            getActivity().setTitle("イベント日一覧");
            CheckEventFragment fragment = new CheckEventFragment();
            transaction.replace(R.id.content_main, fragment);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
