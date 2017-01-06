package com.example.yukinaito.schedule_xp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class SettingMainFragment extends ListFragment {
    private SchedlueApplication schedlueApplication;
    private static final int ADD_CODE = 1;
    private static final int UPDATE_CODE = 2;
    private CardAdapter cardAdapter;
    private ArrayList<Card> cards;
    private Card before;
    private static int arraypos;
    private static int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Bundle bundle = getArguments();
        schedlueApplication = (SchedlueApplication)getActivity().getApplication();
        arraypos = (int)bundle.getSerializable("position");
        cards = schedlueApplication.getModelSchedule().get(this.arraypos).getCards();
        View view = inflater.inflate(R.layout.fragment_listmain, container, false);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddModelActivity.class);
                intent.putExtra("cards", cards);
                startActivityForResult(intent, ADD_CODE);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        ColorDrawable separate_line_color = new ColorDrawable(this.getResources().getColor(R.color.separate_line));
        getListView().setDivider(separate_line_color);
        getListView().setDividerHeight(5);
        cardAdapter = new CardAdapter();
        setListAdapter(cardAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id){
        this.position = pos;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("モデルの操作");
        builder.setMessage("モデルの内容を編集、またはモデルを削除しますか？");
        builder.setPositiveButton("削除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkUpdate();
                cards.remove(position);
                updateListfragment();
            }
        });
        builder.setNegativeButton("編集", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getActivity(), AddModelActivity.class);
                Card card = new Card();
                before = card;
                card.setInfo(schedlueApplication.getModelSchedule().get(arraypos).getCards().get(position).getCalendar(),
                        schedlueApplication.getModelSchedule().get(arraypos).getCards().get(position).getLentime(),
                        schedlueApplication.getModelSchedule().get(arraypos).getCards().get(position).getContent(),
                        schedlueApplication.getModelSchedule().get(arraypos).getCards().get(position).getPlace());
                intent.putExtra("cards", cards);
                intent.putExtra("EditingCard", card);
                cards.remove(position);
                startActivityForResult(intent,UPDATE_CODE);
            }
        });
        builder.setNeutralButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

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
                layout.setOrientation(LinearLayout.HORIZONTAL);

                view = layout;

                FrameLayout layout3 = new FrameLayout(context);
                layout.addView(layout3);
                layout3.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));

                TextView textView1 = new TextView(context);
                textView1.setTag("time");
                textView1.setTextColor(Color.parseColor("#424242"));
                textView1.setPadding(40, 5, 40, 20);
                textView1.setTextSize(45.0f);
                layout3.addView(textView1);

                TextView textView4 = new TextView(context);
                textView4.setTag("finish");
                textView4.setTextColor(Color.parseColor("#424242"));
                textView4.setPadding(0, 10, 10, 0);
                textView4.setTextSize(15.0f);
                textView4.setGravity(Gravity.RIGHT|Gravity.BOTTOM);
                layout3.addView(textView4);

                LinearLayout layout2 = new LinearLayout(context);
                layout2.setBackgroundColor(Color.WHITE);
                layout2.setPadding(0, 0, 0, 0);
                layout2.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                layout2.setOrientation(LinearLayout.VERTICAL);

                TextView textView2 = new TextView(context);
                textView2.setTag("content");
                textView2.setTextColor(Color.parseColor("#424242"));
                textView2.setPadding(10, 10, 10, 10);
                textView2.setTextSize(20.0f);
                int id = getContext().getResources().getIdentifier("dotted_line2", "drawable", getContext().getPackageName());
                Drawable back = getContext().getResources().getDrawable(id);
                textView2.setBackground(back);
                layout2.addView(textView2, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,1f));

                TextView textView3 = new TextView(context);
                textView3.setTag("place");
                textView3.setTextColor(Color.parseColor("#424242"));
                textView3.setPadding(10, 10, 10, 10);
                textView3.setTextSize(20.0f);
                id = getContext().getResources().getIdentifier("dotted_line3", "drawable", getContext().getPackageName());
                back = getContext().getResources().getDrawable(id);
                textView3.setBackground(back);
                layout2.addView(textView3, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,1f));

                layout.addView(layout2);
            }

            Format f = new DecimalFormat("00");
            TextView textView1 = (TextView)view.findViewWithTag("time");
            textView1.setText(f.format(card.getCalendar()/100) + ":" + f.format(card.getCalendar()%100));
            TextView textView2 = (TextView)view.findViewWithTag("content");
            textView2.setText(card.getContent());
            TextView textView3 = (TextView)view.findViewWithTag("place");
            textView3.setText(card.getPlace());
            long buffer = (card.getCalendar() / 100) * 60 + card.getCalendar() % 100 + card.getLentime();
            long time = (buffer / 60) * 100 + (buffer % 60);
            if (time != card.getCalendar()) {
                TextView textView4 = (TextView) view.findViewWithTag("finish");
                textView4.setText("～" + f.format(time/100) + ":" + f.format(time%100));
            }
            return view;
        }
    }

    public void updateListfragment(){
        schedlueApplication.writeModelFile();
        cardAdapter = new CardAdapter();
        setListAdapter(cardAdapter);
        cardAdapter.notifyDataSetChanged();
    }

    public void checkUpdate(){
        boolean check = false;
        for(int i = 0; i < schedlueApplication.getEventplancards().size(); i++){
            if(schedlueApplication.getEventplancards().get(i).getIndex() == arraypos)
                check = true;
        }
        if(check){
            ModelSchedule modelSchedule = new ModelSchedule();
            modelSchedule.setName(schedlueApplication.getModelSchedule().get(this.arraypos).getName());
            for(int i = 0; i < schedlueApplication.getModelSchedule().get(this.arraypos).getCards().size(); i++){
                Card card = new Card();
                card.setInfo(schedlueApplication.getModelSchedule().get(this.arraypos).getCards().get(i).getCalendar(),
                        schedlueApplication.getModelSchedule().get(this.arraypos).getCards().get(i).getLentime(),
                        schedlueApplication.getModelSchedule().get(this.arraypos).getCards().get(i).getContent(),
                        schedlueApplication.getModelSchedule().get(this.arraypos).getCards().get(i).getPlace());
                modelSchedule.getCards().add(card);
            }
            schedlueApplication.getEventmodel().add(modelSchedule);
            for(int i = 0; i < schedlueApplication.getEventplancards().size(); i++) {
                if(schedlueApplication.getEventplancards().get(i).getIndex() == arraypos)
                    schedlueApplication.getEventplancards().get(i).setIndex(schedlueApplication.getModelSchedule().size() + schedlueApplication.getEventmodel().size() - 1);
            }
            schedlueApplication.writeEventPlanFile();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_CODE){
            if(resultCode == RESULT_OK) {
                int pos = data.getIntExtra("Position",-1);
                if (pos != -1) {
                    checkUpdate();
                    if (pos == cards.size())
                        cards.add((Card) data.getSerializableExtra("Card"));
                    else
                        cards.add(pos, (Card) data.getSerializableExtra("Card"));
                    updateListfragment();
                }
            }
        }else if(requestCode == UPDATE_CODE){
            if(resultCode == RESULT_OK) {
                int pos = data.getIntExtra("Position", -1);
                if (pos != -1) {
                    cards.add(position, before);
                    checkUpdate();
                    cards.remove(position);
                    if (pos == cards.size())
                        cards.add((Card) data.getSerializableExtra("Card"));
                    else
                        cards.add(pos, (Card) data.getSerializableExtra("Card"));
                }else
                    cards.add(position, (Card) data.getSerializableExtra("Card"));
                updateListfragment();
            }
        }
    }
}
