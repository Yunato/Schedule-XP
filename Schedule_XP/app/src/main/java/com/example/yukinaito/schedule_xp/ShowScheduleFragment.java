package com.example.yukinaito.schedule_xp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;

public class ShowScheduleFragment extends ListFragment {
    //requestCode
    private static final int ADD_EDIT_PLAN = 1;

    //データ
    private ShowScheduleFragment.CardAdapter cardAdapter;
    private ArrayList<Card> cards;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listandfbutton, container, false);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        cards = ((ScheduleApplication)getActivity().getApplication()).createSchedule(year * 10000 + month * 100 + day);
        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region 予定の追加画面へ遷移
                //日付はその日をセットするように
                //Intent intent = new Intent(getActivity(), AddModelActivity.class);
                //startActivityForResult(intent, ADD_EDIT_PLAN);
                //endregion
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
        //ListViewの区切り線
        ColorDrawable separate_line_color = new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.separate_line));
        getListView().setDivider(separate_line_color);
        getListView().setDividerHeight(5);

        //Listの描画
        cardAdapter = new ShowScheduleFragment.CardAdapter();
        updateList();
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
                view = (LayoutInflater.from(context)).inflate(R.layout.list_settingmodel, null);

            //宣言&初期化
            Format format = new DecimalFormat("00");
            int start = card.getStartTime();
            int finish;
            TextView textView1 = (TextView) view.findViewById(R.id.time);
            TextView textView2 = (TextView) view.findViewById(R.id.finish);
            TextView textView3 = (TextView) view.findViewById(R.id.content);
            TextView textView4 = (TextView) view.findViewById(R.id.place);

            //trueならば連結している
            if(card.getConnect()){
                if(cards.size() == (pos + 1)){
                    finish = 2400;
                }else{
                    finish = cards.get(pos + 1).getStartTime();
                }
            }
            else{
                finish = card.getOverTime();
            }

            //データの形式変更
            start = (start / 100) * 60 + (start % 100);
            finish = (finish / 100) * 60 + (finish % 100);

            //この処理がないとList更新時前のデータが残る
            textView1.setText("");
            textView2.setText("");
            textView3.setText("");
            textView4.setText("");

            //値のセット
            textView1.setText(format.format(start / 60) + ":" + format.format(start % 60));
            textView3.setText(card.getContent());
            textView4.setText(card.getPlace());

            //終了時刻
            if (start != finish)
                textView2.setText("～" + format.format(finish / 60) + ":" + format.format(finish % 60));

            return view;
        }
    }

    //Listの更新
    public void updateList(){
        setListAdapter(cardAdapter);
        //画面更新
        cardAdapter.notifyDataSetChanged();
    }
}
