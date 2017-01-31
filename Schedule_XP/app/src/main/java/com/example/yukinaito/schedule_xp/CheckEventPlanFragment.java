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
import android.support.v4.content.ContextCompat;
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
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class CheckEventPlanFragment extends ListFragment {
    private ScheduleApplication scheduleApplication;
    private static final int ADD_CODE = 1;
    private static final int UPDATE_CODE = 2;
    private CheckEventPlanFragment.CardAdapter cardAdapter;
    private ArrayList<EventCard> cards;
    private EventCard eventCard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        scheduleApplication = (ScheduleApplication)getActivity().getApplication();
        //イベント日をすべて取得
        cards = scheduleApplication.getEventplancards();
        View view = inflater.inflate(R.layout.fragment_listmain, container, false);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region イベント日時の追加画面へ遷移
                Intent intent = new Intent(getActivity(), AddEventPlanActivity.class);
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
        cardAdapter = new CheckEventPlanFragment.CardAdapter();
        setListAdapter(cardAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, final int pos, long id){
        final int position = pos;
        //ダイアログの生成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("イベント日の操作");
        builder.setMessage("イベント日の詳細を編集、またはイベント日の情報を削除しますか？");
        builder.setPositiveButton("削除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //region イベント日時の削除
                int array = cards.get(position).getIndex();
                if(array > scheduleApplication.getModelSchedule().size() - 1) {
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
                        scheduleApplication.getEventmodel().remove(array - scheduleApplication.getModelSchedule().size());
                    }
                }
                cards.remove(position);
                updateListFragment();
                //endregion
            }
        });
        builder.setNegativeButton("編集", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //region 予定の編集画面へ遷移
                //予定の生成
                eventCard = new EventCard();
                eventCard.setInfo(scheduleApplication.getEventplancards().get(position).getDate(),
                        scheduleApplication.getEventplancards().get(position).getIndex());
                eventCard.setContent(scheduleApplication.getEventplancards().get(position).getCards());
                cards.remove(position);

                //生成したイベント日時と編集するインデックスをAddPlanActivityへ渡す
                Intent intent = new Intent(getActivity(), AddEventPlanActivity.class);
                intent.putExtra("EditingCard", eventCard);
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
        public EventCard getItem(int pos){
            return cards.get(pos);
        }

        @Override
        public long getItemId(int pos){
            return pos;
        }

        @Override
        public View getView(int pos, View view, ViewGroup parent){
            Context context = getActivity().getApplication();
            final EventCard card = cards.get(pos);

            //レイアウトの生成
            if(view == null){
                view = (LayoutInflater.from(context)).inflate(R.layout.list_checkeventplanfragment, null);
            }

            String start = Long.toString(card.getDate());
            TextView textView1 = (TextView)view.findViewById(R.id.TextView1);
            TextView textView2 = (TextView)view.findViewById(R.id.TextView2);

            textView1.setText(start.substring(0, 4) + "年" + start.substring(4, 6) + "月" + start.substring(6, 8) + "日");
            if(card.getIndex() < scheduleApplication.getModelSchedule().size())
                textView2.setText(scheduleApplication.getModelSchedule().get(card.getIndex()).getName());
            else
                textView2.setText(scheduleApplication.getEventmodel().get(card.getIndex() - scheduleApplication.getModelSchedule().size()).getName());
            return view;
        }
    }

    //Listの要素更新
    public void updateListFragment(){
        scheduleApplication.writeEventPlanFile();
        cardAdapter = new CheckEventPlanFragment.CardAdapter();
        setListAdapter(cardAdapter);
        //画面更新
        cardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_CODE){
            //region イベント日時の追加時
            if(resultCode == RESULT_OK) {
                int position = data.getIntExtra("Position", -1);
                if (position != -1) {
                    //更新後の日時が一番遅いかどうか true 一番遅い
                    if (position == cards.size())
                        cards.add((EventCard) data.getSerializableExtra("Card"));
                    else
                        cards.add(position, (EventCard) data.getSerializableExtra("Card"));
                }
            }else
                return;
            //endregion
        }else if(requestCode == UPDATE_CODE){
            //region イベント日時の更新時
            if(resultCode == RESULT_OK) {
                int position = data.getIntExtra("Position", -1);
                if (position != -1) {
                    //更新後の日時が一番遅いかどうか true 一番遅い
                    if (position == cards.size())
                        cards.add((EventCard) data.getSerializableExtra("Card"));
                    else
                        cards.add(position, (EventCard) data.getSerializableExtra("Card"));
                } else{
                    //編集がなかった場合元のインデックスで追加する
                    if (data.getIntExtra("EditPos", -1) != -1)
                        cards.add(data.getIntExtra("EditPos", -1), (EventCard) data.getSerializableExtra("Card"));
                }
            }else
                return;
            //endregion
        }
        updateListFragment();
    }
}
