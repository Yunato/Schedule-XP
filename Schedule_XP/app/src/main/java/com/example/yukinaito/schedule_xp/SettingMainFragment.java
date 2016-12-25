package com.example.yukinaito.schedule_xp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SettingMainFragment extends ListFragment {
    private ArrayList<ModelSchedule.Card> cards;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Bundle bundle = getArguments();
        cards = ((ModelSchedule)bundle.getSerializable("day_information")).getCards();
        return inflater.inflate(R.layout.updatemodelday, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        setListAdapter(new CardAdapter());
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id){

    }

    private class CardAdapter extends BaseAdapter {
        @Override
        public int getCount(){
            return cards.size();
        }

        @Override
        public ModelSchedule.Card getItem(int pos){
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
            ModelSchedule.Card card = cards.get(pos);

            //レイアウトの生成
            if(view == null){
                LinearLayout layout = new LinearLayout(context);
                layout.setBackgroundColor(Color.WHITE);
                layout.setPadding(10, 10, 10, 10);
                layout.setOrientation(LinearLayout.HORIZONTAL);

                view = layout;

                TextView textView1 = new TextView(context);
                textView1.setTag("time");
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

            TextView textView1 = (TextView)view.findViewWithTag("time");
            textView1.setText(Integer.toString(card.time));
            TextView textView2 = (TextView)view.findViewWithTag("place");
            textView2.setText(card.content);
            TextView textView3 = (TextView)view.findViewWithTag("content");
            textView3.setText(card.place);
            return view;
        }
    }
}
