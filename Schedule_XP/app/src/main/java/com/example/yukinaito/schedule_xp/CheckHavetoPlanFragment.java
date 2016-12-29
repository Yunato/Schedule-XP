package com.example.yukinaito.schedule_xp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.Switch;
import android.widget.TextView;

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

            //レイアウトの生成
            if(view == null){
                LinearLayout layout = new LinearLayout(context);
                layout.setBackgroundColor(Color.WHITE);
                layout.setPadding(10, 10, 10, 10);
                layout.setOrientation(LinearLayout.HORIZONTAL);

                view = layout;

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
                layout.addView(switch1);

                TextView textView1 = new TextView(context);
                textView1.setTextColor(Color.BLACK);
                textView1.setPadding(10,10, 10, 10);
                textView1.setTextSize(50.0f);
                textView1.setText(card.getName());
                layout.addView(textView1);
            }
            return view;
        }
    }

    public void updateListfragment(){
        cardAdapter = new CheckHavetoPlanFragment.CardAdapter();
        setListAdapter(cardAdapter);
        cardAdapter.notifyDataSetChanged();
        schedlueApplication.writeHavetoPlanFile();
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
