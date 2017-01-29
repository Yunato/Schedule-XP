package com.example.yukinaito.schedule_xp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class CheckMustPlanFragment extends ListFragment {
    private ScheduleApplication scheduleApplication;
    private static final int ADD_CODE = 1;
    private static final int UPDATE_CODE = 2;
    private CheckMustPlanFragment.CardAdapter cardAdapter;
    private ArrayList<MustPlanCard> cards;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        scheduleApplication = (ScheduleApplication)getActivity().getApplication();
        //予定をすべて取得
        cards = scheduleApplication.getHavetoplancards();
        View view = inflater.inflate(R.layout.fragment_listmain, container, false);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region すべきことの追加画面へ遷移
                Intent intent = new Intent(getActivity(), AddMustPlanActivity.class);
                startActivityForResult(intent, ADD_CODE);
                //endregion
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //ListViewの区切り線
        ColorDrawable separate_line_color = new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.separate_line));
        getListView().setDivider(separate_line_color);
        getListView().setDividerHeight(5);

        //ListViewの値セット
        cardAdapter = new CheckMustPlanFragment.CardAdapter();
        setListAdapter(cardAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id){
        final int position = pos;
        //ダイアログの生成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("予定の操作");
        builder.setMessage("すべきの内容を編集、またはやるべきことを削除しますか？");
        builder.setPositiveButton("削除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //region すべきことの削除
                cards.remove(position);
                updateListFragment();
                //endregion
            }
        });
        builder.setNegativeButton("編集", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //region すべきことの編集画面へ遷移
                //すべきことの生成
                MustPlanCard card = new MustPlanCard();
                card.setInfo(scheduleApplication.getHavetoplancards().get(position).getName(),
                        scheduleApplication.getHavetoplancards().get(position).getHaveto(),
                        scheduleApplication.getHavetoplancards().get(position).getStart(),
                        scheduleApplication.getHavetoplancards().get(position).getLimit(),
                        scheduleApplication.getHavetoplancards().get(position).getForcast(),
                        scheduleApplication.getHavetoplancards().get(position).getPlace());
                cards.remove(position);

                //生成した予定をAddMustPlanActivityへ渡す
                Intent intent = new Intent(getActivity(), AddMustPlanActivity.class);
                intent.putExtra("EditingCard", card);
                intent.putExtra("Position", position);
                startActivityForResult(intent,UPDATE_CODE);
                //endregion
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
        public MustPlanCard getItem(int pos){
            return cards.get(pos);
        }

        @Override
        public long getItemId(int pos){
            return pos;
        }

        @Override
        public View getView(int pos, View view, ViewGroup parent){
            Context context = getActivity().getApplication();
            final MustPlanCard card = cards.get(pos);

            //レイアウトの生成
            if(view == null){
                view = (LayoutInflater.from(context)).inflate(R.layout.list_checkmustplanfragment, null);
                ((Switch)view.findViewById(R.id.Switch1)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked == true) {
                            card.setWant(true);
                            buttonView.setChecked(true);
                        } else {
                            card.setWant(false);
                            buttonView.setChecked(false);
                        }
                        updateListFragment();
                    }
                });
            }

            String start = Long.toString(card.getStart());
            String end = Long.toString(card.getLimit());
            Switch bool = (Switch)view.findViewById(R.id.Switch1);
            TextView textView1 = (TextView)view.findViewById(R.id.TextView1);
            TextView textView3 = (TextView)view.findViewById(R.id.TextView3);
            TextView textView5 = (TextView)view.findViewById(R.id.TextView5);

            bool.setChecked(card.getHaveto());
            textView1.setText(card.getName());
            textView3.setText(start.substring(0, 4) + "/" + start.substring(4, 6) + "/" + start.substring(6, 8) + " " + start.substring(8, 10) + "/" + start.substring(10, 12));
            textView5.setText(end.substring(0, 4) + "/" + end.substring(4, 6) + "/" + end.substring(6, 8) + " " + end.substring(8, 10) + "/" + end.substring(10, 12));
            return view;
        }
    }

    //Listの要素更新
    public void updateListFragment(){
        scheduleApplication.writeHavetoPlanFile();
        cardAdapter = new CheckMustPlanFragment.CardAdapter();
        setListAdapter(cardAdapter);
        //画面更新
        cardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_CODE){
            //region すべきことの追加時
            if(resultCode == RESULT_OK) {
                if(data.getSerializableExtra("Card") != null) {
                    cards.add((MustPlanCard) data.getSerializableExtra("Card"));
                    updateListFragment();
                }
            }
            //endregion
        }else if(requestCode == UPDATE_CODE){
            //region すべてきことの更新時
            if(resultCode == RESULT_OK){
                cards.add(data.getIntExtra("EditPos", -1), (MustPlanCard) data.getSerializableExtra("Card"));
                updateListFragment();
            }
            //endregion
        }
    }
}
