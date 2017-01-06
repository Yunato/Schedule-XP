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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class EventPlanFragment extends ListFragment {
    private SchedlueApplication schedlueApplication;
    private static final int ADD_CODE = 1;
    private static final int UPDATE_CODE = 2;
    private EventPlanFragment.CardAdapter cardAdapter;
    private ArrayList<EventCard> cards;
    private EventCard eventCard;
    private static int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        schedlueApplication = (SchedlueApplication)getActivity().getApplication();
        cards = schedlueApplication.getEventplancards();
        View view = inflater.inflate(R.layout.fragment_listmain, container, false);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddEventPlanActivity.class);
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
        cardAdapter = new EventPlanFragment.CardAdapter();
        setListAdapter(cardAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, final int pos, long id){
        this.position = pos;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("イベント日の操作");
        builder.setMessage("イベント日の詳細を編集、またはイベント日の情報を削除しますか？");
        builder.setPositiveButton("削除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int array = cards.get(position).getIndex();
                cards.remove(position);
                if(array > schedlueApplication.getModelSchedule().size() - 1) {
                    boolean check = true;
                    for (int j = 0; j < cards.size(); j++) {
                        if (cards.get(j).getIndex() == array)
                            check = false;
                    }
                    if (check) {
                        for (int j = 0; j < cards.size(); j++) {
                            if (cards.get(j).getIndex() > array) {
                                int buf = cards.get(j).getIndex();
                                cards.get(j).setIndex(buf - 1);
                            }
                        }
                        schedlueApplication.getEventmodel().remove(array - schedlueApplication.getModelSchedule().size());
                    }
                }
                updateListfragment();
            }
        });
        builder.setNegativeButton("編集", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getActivity(), AddEventPlanActivity.class);
                eventCard = new EventCard();
                eventCard.setInfo(schedlueApplication.getEventplancards().get(position).getDate(),
                        schedlueApplication.getEventplancards().get(position).getIndex());
                eventCard.setContent(schedlueApplication.getEventplancards().get(position).getCards());
                intent.putExtra("EditingCard", eventCard);
                intent.putExtra("position", position);
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
        public EventCard getItem(int pos){
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
            final EventCard card = cards.get(pos);
            Format f1 = new DecimalFormat("0000");
            Format f2 = new DecimalFormat("00");

            //レイアウトの生成
            if(view == null){
                LinearLayout layout = new LinearLayout(context);
                layout.setBackgroundColor(Color.WHITE);
                layout.setPadding(10, 10, 10, 10);
                layout.setOrientation(LinearLayout.VERTICAL);

                view = layout;

                TextView textView1 = new TextView(context);
                textView1.setTextColor(Color.parseColor("#424242"));
                textView1.setPadding(10,10,10,5);
                textView1.setTextSize(45.0f);
                int id = getContext().getResources().getIdentifier("dotted_line1", "drawable", getContext().getPackageName());
                Drawable back = getContext().getResources().getDrawable(id);
                textView1.setBackground(back);
                long start = card.getDate() % 10000;
                textView1.setText(f1.format(card.getDate() / 10000) + "年" + f2.format((start / 100)) + "月" + f2.format(start % 100) + "日");
                layout.addView(textView1);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

                TextView textView2 = new TextView(context);
                textView2.setTextColor(Color.parseColor("#424242"));
                textView2.setPadding(10,5,10,10);
                textView2.setTextSize(20.0f);
                textView2.setGravity(Gravity.RIGHT);
                if(card.getIndex() < schedlueApplication.getModelSchedule().size())
                    textView2.setText(schedlueApplication.getModelSchedule().get(card.getIndex()).getName());
                else
                    textView2.setText(schedlueApplication.getModelSchedule().get(card.getIndex() - schedlueApplication.getModelSchedule().size()).getName());
                layout.addView(textView2, lp);
            }
            return view;
        }
    }

    public void updateListfragment(){
        schedlueApplication.writeEventPlanFile();
        cardAdapter = new EventPlanFragment.CardAdapter();
        setListAdapter(cardAdapter);
        cardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_CODE){
            if(resultCode == RESULT_OK) {
                int pos = data.getIntExtra("Position", -1);
                Log.d("OK",Integer.toString(pos));
                if (pos > -1) {
                    if (pos == cards.size())
                        cards.add((EventCard) data.getSerializableExtra("Card"));
                    else
                        cards.add(pos, (EventCard) data.getSerializableExtra("Card"));
                }
                updateListfragment();
            }
        }else if(requestCode == UPDATE_CODE){
            if(resultCode == RESULT_OK) {
                int pos = data.getIntExtra("Position", -1);
                if (pos > -1) {
                    if (pos == cards.size())
                        cards.add((EventCard) data.getSerializableExtra("Card"));
                    else
                        cards.add(pos, (EventCard) data.getSerializableExtra("Card"));
                } else if(pos == -1)
                    cards.add(position, eventCard);
                updateListfragment();
            }
        }
    }
}
