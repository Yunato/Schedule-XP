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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class CheckMainFragment extends ListFragment {
    private SchedlueApplication schedlueApplication;
    private static final int ADD_CODE = 1;
    private static final int UPDATE_CODE = 2;
    private CardAdapter cardAdapter;
    private ArrayList<Card> cards;
    private static int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        schedlueApplication = (SchedlueApplication)getActivity().getApplication();
        cards = schedlueApplication.getPlanCard();
        View view = inflater.inflate(R.layout.fragment_listmain, container, false);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddPlanActivity.class);
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
        builder.setTitle("予定の操作");
        builder.setMessage("予定の内容を編集、または予定を削除しますか？");
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
                Intent intent = new Intent(getActivity(), AddPlanActivity.class);
                Card card = new Card();
                card.setInfo(schedlueApplication.getPlanCard().get(position).getCalendar(),
                        schedlueApplication.getPlanCard().get(position).getLentime(),
                        schedlueApplication.getPlanCard().get(position).getContent(),
                        schedlueApplication.getPlanCard().get(position).getPlace());
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
                layout.setOrientation(LinearLayout.VERTICAL);
                view = layout;

                TextView textView = new TextView(context);
                textView.setTag("date");
                textView.setTextColor(Color.parseColor("#424242"));
                textView.setPadding(40, 10, 40, 10);
                textView.setTextSize(45.0f);
                int id = getContext().getResources().getIdentifier("dotted_line1", "drawable", getContext().getPackageName());
                Drawable back = getContext().getResources().getDrawable(id);
                textView.setBackground(back);
                layout.addView(textView);

                LinearLayout layout1 = new LinearLayout(context);
                layout1.setBackgroundColor(Color.WHITE);
                layout1.setOrientation(LinearLayout.HORIZONTAL);

                FrameLayout layout3 = new FrameLayout(context);
                layout1.addView(layout3);
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
                id = getContext().getResources().getIdentifier("dotted_line2", "drawable", getContext().getPackageName());
                back = getContext().getResources().getDrawable(id);
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

                layout.addView(layout1);
                layout1.addView(layout2);
            }

            TextView textView = (TextView)view.findViewWithTag("date");
            textView.setText((new SimpleDateFormat("yyyy/MM/dd")).format(card.getCalendar().getTime()));
            TextView textView1 = (TextView)view.findViewWithTag("time");
            textView1.setText((new SimpleDateFormat("HH:mm")).format(card.getCalendar().getTime()));
            TextView textView2 = (TextView)view.findViewWithTag("content");
            textView2.setText(card.getContent());
            TextView textView3 = (TextView)view.findViewWithTag("place");
            textView3.setText(card.getPlace());
            Calendar cal = (Calendar)card.getCalendar().clone();
            cal.add(Calendar.MINUTE, card.getLentime());
            int diff = cal.compareTo(card.getCalendar());
            if (diff != 0) {
                TextView textView4 = (TextView) view.findViewWithTag("finish");
                textView4.setText((new SimpleDateFormat("～HH:mm")).format(cal.getTime()));
            }
            return view;
        }
    }

    public void updateListfragment(){
        schedlueApplication.writePlanFile();
        cardAdapter = new CardAdapter();
        setListAdapter(cardAdapter);
        cardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_CODE){
            if(resultCode == RESULT_OK) {
                cards.add((Card) data.getSerializableExtra("Card"));
                updateListfragment();
            }
        }else if(requestCode == UPDATE_CODE){
            if(resultCode == RESULT_OK){
                cards.get(position).setUpdate((Card)data.getSerializableExtra("Card"));
                updateListfragment();
            }
        }
    }
}
