package com.example.yukinaito.schedule_xp;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CheckHavetoPlanFragment extends ListFragment {
    private SchedlueApplication schedlueApplication;
    private static final int ADD_CODE = 1;
    private static final int UPDATE_CODE = 2;
    private CheckHavetoPlanFragment.CardAdapter cardAdapter;
    private ArrayList<HavetoPlanCard> cards;
    private static int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        schedlueApplication = (SchedlueApplication)getActivity().getApplication();
        cards = schedlueApplication.getHavetoplancards();
        View view = inflater.inflate(R.layout.fragment_listmain, container, false);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddHavetoPlanActivity.class);
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
        cardAdapter = new CheckHavetoPlanFragment.CardAdapter();
        setListAdapter(cardAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id){
        this.position = pos;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("予定の操作");
        builder.setMessage("やるべきの内容を編集、またはやるべきことを削除しますか？");
        builder.setPositiveButton("削除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cards.remove(position);
                updateListfragment();
            }
        });
        builder.setNegativeButton("編集", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getActivity(), AddHavetoPlanActivity.class);
                HavetoPlanCard card = new HavetoPlanCard();
                card.setInfo(schedlueApplication.getHavetoplancards().get(position).getName(),
                        schedlueApplication.getHavetoplancards().get(position).getHaveto(),
                        schedlueApplication.getHavetoplancards().get(position).getStart(),
                        schedlueApplication.getHavetoplancards().get(position).getLimit(),
                        schedlueApplication.getHavetoplancards().get(position).getForcast(),
                        schedlueApplication.getHavetoplancards().get(position).getPlace());
                intent.putExtra("EditingCard", card);
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
        public HavetoPlanCard getItem(int pos){
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
            final HavetoPlanCard card = cards.get(pos);
            Format f1 = new DecimalFormat("0000");
            Format f2 = new DecimalFormat("00");

            //レイアウトの生成
            if(view == null){
                LinearLayout layout = new LinearLayout(context);
                layout.setBackgroundColor(Color.WHITE);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                view = layout;

                RelativeLayout layout4 = new RelativeLayout(context);
                layout4.setBackgroundColor(Color.WHITE);
                layout.addView(layout4,  new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT));

                Switch switch1 = new Switch(context);
                switch1.setChecked(card.getHaveto());
                switch1.setPadding(10, 10, 10, 10);
                switch1.setFocusableInTouchMode(false);
                switch1.setFocusable(false);
                switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked == true) {
                            card.setWant(true);
                            buttonView.setChecked(true);
                        } else {
                            card.setWant(false);
                            buttonView.setChecked(false);
                        }
                        updateListfragment();
                    }
                });

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
                lp.addRule(RelativeLayout.ALIGN_BOTTOM);
                layout4.addView(switch1, lp);

                LinearLayout layout1 = new LinearLayout(context);
                layout1.setBackgroundColor(Color.WHITE);
                layout1.setOrientation(LinearLayout.VERTICAL);
                int id = getContext().getResources().getIdentifier("dotted_line4", "drawable", getContext().getPackageName());
                Drawable back = getContext().getResources().getDrawable(id);
                layout1.setBackground(back);
                layout.addView(layout1, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,1f));

                TextView textView1 = new TextView(context);
                textView1.setTextColor(Color.parseColor("#424242"));
                textView1.setPadding(40, 10, 40, 10);
                textView1.setTextSize(45.0f);
                textView1.setText(card.getName());
                id = getContext().getResources().getIdentifier("dotted_line1", "drawable", getContext().getPackageName());
                back = getContext().getResources().getDrawable(id);
                textView1.setBackground(back);
                layout1.addView(textView1);

                LinearLayout layout2 = new LinearLayout(context);
                layout2.setBackgroundColor(Color.WHITE);
                layout2.setOrientation(LinearLayout.HORIZONTAL);
                id = getContext().getResources().getIdentifier("dotted_line1", "drawable", getContext().getPackageName());
                back = getContext().getResources().getDrawable(id);
                layout2.setBackground(back);
                layout1.addView(layout2, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,1f));

                TextView textView2 = new TextView(context);
                textView2.setTextColor(Color.parseColor("#424242"));
                textView2.setPadding(10, 10, 10, 10);
                textView2.setTextSize(20.0f);
                textView2.setText("開始");
                layout2.addView(textView2);

                TextView textView3 = new TextView(context);
                textView3.setTextColor(Color.parseColor("#424242"));
                textView3.setPadding(10, 10, 10, 10);
                textView3.setTextSize(20.0f);
                long start1 = card.getStart() % 100000000;
                long start2 = card.getStart() % 10000;
                textView3.setText(f1.format(card.getStart() / 100000000) + "/" + f2.format(start1/1000000) + "/" + f2.format((start1%1000000)/10000) + " " + f2.format(start2/100) + ":" + f2.format(start2%100));
                id = getContext().getResources().getIdentifier("dotted_line3", "drawable", getContext().getPackageName());
                back = getContext().getResources().getDrawable(id);
                textView3.setBackground(back);
                layout2.addView(textView3);

                LinearLayout layout3 = new LinearLayout(context);
                layout3.setOrientation(LinearLayout.HORIZONTAL);
                layout1.addView(layout3, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,1f));

                TextView textView4 = new TextView(context);
                textView4.setTextColor(Color.parseColor("#424242"));
                textView4.setPadding(10, 10, 10, 10);
                textView4.setTextSize(20.0f);
                textView4.setText("終了");
                layout3.addView(textView4);

                TextView textView5 = new TextView(context);
                textView5.setTextColor(Color.parseColor("#424242"));
                textView5.setPadding(10, 10, 10, 10);
                textView5.setTextSize(20.0f);
                long end1 = card.getLimit() % 100000000;
                long end2 = card.getLimit() % 10000;
                textView5.setText(f1.format(card.getLimit() / 100000000) + "/" + f2.format(end1/1000000) + "/" + f2.format((end1%1000000)/10000) + " " + f2.format(end2/100) + ":" + f2.format(end2%100));
                id = getContext().getResources().getIdentifier("dotted_line3", "drawable", getContext().getPackageName());
                back = getContext().getResources().getDrawable(id);
                textView5.setBackground(back);
                layout3.addView(textView5);
            }
            return view;
        }
    }

    public void updateListfragment(){
        schedlueApplication.writeHavetoPlanFile();
        cardAdapter = new CheckHavetoPlanFragment.CardAdapter();
        setListAdapter(cardAdapter);
        cardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_CODE){
            if(resultCode == RESULT_OK) {
                cards.add((HavetoPlanCard) data.getSerializableExtra("Card"));
                updateListfragment();
            }
        }else if(requestCode == UPDATE_CODE){
            if(resultCode == RESULT_OK){
                cards.get(position).setUpdate((HavetoPlanCard)data.getSerializableExtra("Card"));
                updateListfragment();
            }
        }
    }
}
