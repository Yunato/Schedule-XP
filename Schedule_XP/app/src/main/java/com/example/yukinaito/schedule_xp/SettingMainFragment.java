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
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class SettingMainFragment extends ListFragment {
    private ScheduleApplication scheduleApplication;
    //requestCode
    private static final int ADD_CODE = 1;
    private static final int UPDATE_CODE = 2;
    //表示する予定群
    private CardAdapter cardAdapter;
    //予定を格納
    private ArrayList<Card> cards;
    //固定スケジュールのインデックス
    private static int modelIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        scheduleApplication = (ScheduleApplication)getActivity().getApplication();
        //固定スケジュールのインデックスを取得
        Bundle bundle = getArguments();
        modelIndex = (int)bundle.getSerializable("Position");
        //予定をすべて取得
        cards = scheduleApplication.getModelSchedule().get(modelIndex).getCards();
        View view = inflater.inflate(R.layout.fragment_listmain, container, false);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region 予定の追加画面へ遷移
                Intent intent = new Intent(getActivity(), AddModelActivity.class);
                intent.putExtra("cards", cards);
                intent.putExtra("modelIndex", modelIndex);
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
        builder.setTitle("モデルの操作");
        builder.setMessage("モデルの内容を編集、またはモデルを削除しますか？");
        builder.setPositiveButton("削除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //region 予定の削除
                checkUpdate();
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
                card.setInfo(scheduleApplication.getModelSchedule().get(modelIndex).getCards().get(position).getCalendar(),
                        scheduleApplication.getModelSchedule().get(modelIndex).getCards().get(position).getLenTime(),
                        scheduleApplication.getModelSchedule().get(modelIndex).getCards().get(position).getContent(),
                        scheduleApplication.getModelSchedule().get(modelIndex).getCards().get(position).getPlace());
                cards.remove(position);

                //生成した予定と編集する予定のインデックスをAddPlanActivityへ渡す
                Intent intent = new Intent(getActivity(), AddModelActivity.class);
                intent.putExtra("EditingCard", card);
                intent.putExtra("Position", position);
                intent.putExtra("modelIndex", modelIndex);
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
        public Card getItem(int pos){
            return cards.get(pos);
        }

        @Override
        public long getItemId(int pos){
            return pos;
        }

        @Override
        public View getView(int pos, View view, ViewGroup parent){
            Context context = getActivity().getApplication();
            //ListViewの要素として表示する予定情報
            Card card = cards.get(pos);

            //レイアウトの生成
            if(view == null)
                view = (LayoutInflater.from(context)).inflate(R.layout.list_settingmainfragment, null);

            Format format = new DecimalFormat("00");
            int start = (int)card.getCalendar();
            start = (start / 100) * 60 + (start % 100);
            int finish = card.getLenTime();
            TextView textView1 = (TextView) view.findViewById(R.id.time);
            TextView textView2 = (TextView) view.findViewById(R.id.content);
            TextView textView3 = (TextView) view.findViewById(R.id.place);
            TextView textView4 = (TextView) view.findViewById(R.id.finish);

            //この処理がないとList更新時前のデータが残る
            textView1.setText("");
            textView2.setText("");
            textView3.setText("");
            textView4.setText("");

            textView1.setText(format.format(start/60) + ":" + format.format(start%60));
            textView2.setText(card.getContent());
            textView3.setText(card.getPlace());

            //終了時刻
            int time = start + finish;
            if (time != start)
                textView4.setText("～" + format.format(time/60) + ":" + format.format(time%60));

            return view;
        }
    }

    //Listの要素更新
    public void updateListFragment(){
        scheduleApplication.writeModelFile();
        cardAdapter = new CardAdapter();
        setListAdapter(cardAdapter);
        //画面更新
        cardAdapter.notifyDataSetChanged();
    }

    //イベント日のチェック
    public void checkUpdate(){
        //イベント日の指定で本処理で変更される固定スケジュールをもつものがあるか
        boolean check = false;
        for(int i = 0; i < scheduleApplication.getEventplancards().size(); i++){
            if(scheduleApplication.getEventplancards().get(i).getIndex() == modelIndex) {
                check = true;
                break;
            }
        }
        if(check){
            //イベント日に指定されているため、固定スケジュールを別の場所へ移す
            scheduleApplication.getEventmodel().add(scheduleApplication.getModelSchedule().get(modelIndex));
            for(int i = 0; i < scheduleApplication.getEventplancards().size(); i++) {
                if(scheduleApplication.getEventplancards().get(i).getIndex() == modelIndex)
                    scheduleApplication.getEventplancards().get(i).setIndex(scheduleApplication.getModelSchedule().size() + scheduleApplication.getEventmodel().size() - 1);
            }
            scheduleApplication.writeEventPlanFile();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_CODE){
            //region 予定の追加時
            if(resultCode == RESULT_OK) {
                int pos = data.getIntExtra("Position",-1);
                if (pos != -1) {
                    //イベント日のチェック
                    checkUpdate();
                    //追加する予定の日時が一番遅いかどうか true 一番遅い
                    if (pos == cards.size())
                        cards.add((Card) data.getSerializableExtra("Card"));
                    else
                        cards.add(pos, (Card) data.getSerializableExtra("Card"));
                }
            }else
                return;
            //endregion
        }else if(requestCode == UPDATE_CODE){
            //region 予定の更新時
            if(resultCode == RESULT_OK) {
                int pos = data.getIntExtra("Position", -1);
                if (pos != -1) {
                    checkUpdate();
                    //更新後の日時が一番遅いかどうか true 一番遅い
                    if (pos == cards.size())
                        cards.add((Card) data.getSerializableExtra("Card"));
                    else
                        cards.add(pos, (Card) data.getSerializableExtra("Card"));
                }else {
                    //編集がなかった場合元のインデックスで追加する
                    if (data.getIntExtra("EditPos", -1) != -1)
                        cards.add(data.getIntExtra("EditPos", -1), (Card) data.getSerializableExtra("Card"));
                }
            }else
                return;
            //endregion
        }else
            return;
        updateListFragment();
    }
}
