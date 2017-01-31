package com.example.yukinaito.schedule_xp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class CheckMainFragment extends ListFragment {
    private ScheduleApplication scheduleApplication;
    //requestCode
    private static final int ADD_CODE = 1;
    private static final int UPDATE_CODE = 2;
    private static final int MEMO_UPDATE_CODE = 3;
    //表示する予定群
    private CardAdapter cardAdapter;
    //予定を格納
    private ArrayList<Card> cards;
    //メモ記入時に対象の予定を格納する変数
    private Card memoCard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        scheduleApplication = (ScheduleApplication)getActivity().getApplication();
        //予定をすべて取得
        cards = scheduleApplication.getPlanCard();
        View view = inflater.inflate(R.layout.fragment_listmain, container, false);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region 予定の追加画面へ遷移
                Intent intent = new Intent(getActivity(), AddPlanActivity.class);
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
        updateListFragment();
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id){
        final int position = pos;
        //ダイアログの生成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("予定の操作");
        builder.setMessage("予定の内容を編集、または予定を削除しますか？");
        builder.setPositiveButton("削除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //region 予定の削除
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
                Card card = new Card();
                card.setInfo(scheduleApplication.getPlanCard().get(position).getCalendar(),
                        scheduleApplication.getPlanCard().get(position).getLenTime(),
                        scheduleApplication.getPlanCard().get(position).getContent(),
                        scheduleApplication.getPlanCard().get(position).getPlace());
                cards.remove(position);

                //生成した予定と編集する予定のインデックスをAddPlanActivityへ渡す
                Intent intent = new Intent(getActivity(), AddPlanActivity.class);
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
        public int getCount() {
            return cards.size();
        }

        @Override
        public Card getItem(int pos) {
            return cards.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int pos, View view, ViewGroup parent) {
            Context context = getActivity().getApplication();
            //ListViewの要素として表示する予定情報
            final Card card = cards.get(pos);

            //レイアウトの生成
            if (view == null) {
                view = (LayoutInflater.from(context)).inflate(R.layout.list_checkmainfragment, null);
                (view.findViewById(R.id.memo)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //region メモ入力画面へ遷移
                        memoCard = card;
                        Intent intent = new Intent(getActivity(), AddMemoActivity.class);
                        if (card.getMemo() != null)
                            intent.putExtra("memo", card.getMemo());
                        else
                            intent.putExtra("memo", "");
                        startActivityForResult(intent, MEMO_UPDATE_CODE);
                        //endregion
                    }
                });
            }

            Format format = new DecimalFormat("00");
            String start = Long.toString(card.getCalendar());
            String finish = Long.toString(card.getLenTime());
            TextView textView = (TextView) view.findViewById(R.id.date);
            TextView textView1 = (TextView) view.findViewById(R.id.time);
            TextView textView2 = (TextView) view.findViewById(R.id.content);
            TextView textView3 = (TextView) view.findViewById(R.id.place);
            TextView textView4 = (TextView) view.findViewById(R.id.finish);
            Button button1 = (Button) view.findViewById(R.id.memo);

            textView.setText(start.substring(0, 4) + "/" + start.substring(4, 6) + "/" + start.substring(6, 8));
            textView1.setText(start.substring(8, 10) + ":" + start.substring(10, 12));
            textView2.setText(card.getContent());
            textView3.setText(card.getPlace());

            //メモ情報の有無
            if (card.getMemo() != null) {
                button1.setText("メモ\nあり");
                button1.setBackgroundColor(Color.parseColor("#4CAF50"));
            } else {
                button1.setText("メモ\nなし");
                button1.setBackgroundColor(Color.parseColor("#424242"));
            }

            //終了時刻
            int time = Integer.parseInt(start.substring(8, 10)) * 60 +
                    Integer.parseInt(start.substring(10, 12)) +
                    Integer.parseInt(finish);
            if (time != (Integer.parseInt(start.substring(8, 10)) * 60 + Integer.parseInt(start.substring(10, 12)))) {
                textView4.setText("～" + format.format(time / 60) + ":" + format.format(time % 60));
            }
            return view;
        }
    }

    //Listの要素更新
    public void updateListFragment(){
        scheduleApplication.writePlanFile();
        cardAdapter = new CardAdapter();
        setListAdapter(cardAdapter);
        //画面更新
        cardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_CODE){
            //region 予定の追加時
            if(resultCode == RESULT_OK) {
                int position = data.getIntExtra("Position",-1);
                if (position != -1) {
                    //追加する予定の日時が一番遅いかどうか true 一番遅い
                    if (position == cards.size())
                        cards.add((Card) data.getSerializableExtra("Card"));
                    else
                        cards.add(position, (Card) data.getSerializableExtra("Card"));
                }
            }else
                return;
            //endregion
        }else if(requestCode == UPDATE_CODE){
            //region 予定の更新時
            if(resultCode == RESULT_OK) {
                int position = data.getIntExtra("Position", -1);
                if (position != -1) {
                    //更新後の日時が一番遅いかどうか true 一番遅い
                    if (position == cards.size())
                        cards.add((Card) data.getSerializableExtra("Card"));
                    else
                        cards.add(position, (Card) data.getSerializableExtra("Card"));
                }else {
                    //編集がなかった場合元のインデックスで追加する
                    if (data.getIntExtra("EditPos", -1) != -1)
                        cards.add(data.getIntExtra("EditPos", -1), (Card) data.getSerializableExtra("Card"));
                }
            }else
                return;
            //endregion
        }else if(requestCode == MEMO_UPDATE_CODE){
            //region メモの更新時
            if(resultCode == RESULT_OK) {
                if(data.getStringExtra("Memo") != null && (data.getStringExtra("Memo").length() != 0))
                    memoCard.setMemo(data.getStringExtra("Memo"));
                else
                    memoCard.setMemo(null);
            }else
                return;
            //endregion
        }else
            return;
        updateListFragment();
    }
}
