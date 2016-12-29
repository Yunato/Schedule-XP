package com.example.yukinaito.schedule_xp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TodayPlanFragment extends ListFragment {
    public static ArrayList<Card> cards;
    static public final String DATE_PATTERN = "HH:mm";
/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_todayplan, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState){
        super.onViewCreated(view, saveInstanceState);
    }
    */

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        cards = new ArrayList<Card>();
        //setListAdapter(new CardAdapter());
    }
/*
    private class CardAdapter extends BaseAdapter {
        @Override
        public int getCount(){
            return cards.size();
        }

        @Override
        public Card getItem(int pos){
            return cards.get(pos);
        }

        @Override
        public long getItemId(int pos){
            return pos;
        }

        @Override
        public View getView(int pos, View view, ViewGroup parent){
            //Context context = getActivity();
            Context context = getActivity().getApplication();
            Card card = cards.get(pos);

            //レイアウトの生成
            if(view == null){
                LinearLayout layout = new LinearLayout(context);
                layout.setBackgroundColor(Color.WHITE);
                layout.setPadding(10, 10, 10, 10);
                layout.setOrientation(LinearLayout.HORIZONTAL);

                view = layout;

                TextView textView1 = new TextView(context);
                textView1.setTag("calendar");
                textView1.setTextColor(Color.BLACK);
                textView1.setPadding(30, 10, 10, 10);
                textView1.setTextSize(60.0f);
                layout.addView(textView1);

                LinearLayout layout2 = new LinearLayout(context);
                layout2.setBackgroundColor(Color.WHITE);
                layout2.setPadding(0, 0, 0, 0);
                layout2.setOrientation(LinearLayout.VERTICAL);

                TextView textView2 = new TextView(context);
                textView2.setTag("place");
                textView2.setTextColor(Color.BLACK);
                textView2.setPadding(10,10, 10, 10);
                textView2.setTextSize(30.0f);
                layout2.addView(textView2);

                TextView textView3 = new TextView(context);
                textView3.setTag("content");
                textView3.setTextColor(Color.BLACK);
                textView3.setPadding(10, 10, 10, 10);
                textView3.setTextSize(30.0f);
                layout2.addView(textView3);

                layout.addView(layout2);
            }

            TextView textView1 = (TextView)view.findViewWithTag("calendar");
            textView1.setText(convertDate2String(card.calendar.getTime()));
            TextView textView2 = (TextView)view.findViewWithTag("place");
            textView2.setText(card.content);
            TextView textView3 = (TextView)view.findViewWithTag("content");
            textView3.setText(card.place);
            return view;
        }
    }*/

    public String convertDate2String(java.util.Date date) {
        return (new SimpleDateFormat(DATE_PATTERN)).format(date);
    }
}
