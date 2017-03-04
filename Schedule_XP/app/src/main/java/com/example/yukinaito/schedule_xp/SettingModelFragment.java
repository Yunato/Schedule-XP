package com.example.yukinaito.schedule_xp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class SettingModelFragment extends ListFragment {
    //requestCode
    private static final int ADD_EDIT_PLAN = 1;

    //データ
    private CardAdapter cardAdapter;
    private ArrayList<Card> cards;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listandfbutton, container, false);
        cards = ((ScheduleApplication)getActivity().getApplication()).getModelCards();
        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region 予定の追加画面へ遷移
                Intent intent = new Intent(getActivity(), AddModelActivity.class);
                startActivityForResult(intent, ADD_EDIT_PLAN);
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
        cardAdapter = new CardAdapter();
        updateList();
    }

    @Override
    public void onListItemClick(ListView listView, View view, final int position, long id) {
        //ダイアログの生成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("予定の操作");
        builder.setMessage("予定の内容を編集、または予定を削除しますか？");
        builder.setPositiveButton("編集", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //region 予定の編集画面へ遷移
                ArrayList<Card> editCards = new ArrayList<>();

                //region 基準となる(タップされた)予定の生成
                Card editCard = new Card(cards.get(position).getId(),
                        cards.get(position).getDate(),
                        cards.get(position).getStartTime(),
                        cards.get(position).getOverTime(),
                        cards.get(position).getConnect(),
                        cards.get(position).getContent(),
                        cards.get(position).getPlace());
                editCard.setMemo(cards.get(position).getMemo());
                cards.remove(position);
                editCards.add(editCard);
                //endregion

                int index = position;
                //region 基準の予定より後の連結予定を抽出
                while(editCard.getConnect() && index < cards.size()){
                    editCard = new Card(cards.get(index).getId(),
                            cards.get(index).getDate(),
                            cards.get(index).getStartTime(),
                            cards.get(index).getOverTime(),
                            cards.get(index).getConnect(),
                            cards.get(index).getContent(),
                            cards.get(index).getPlace());
                    editCard.setMemo(cards.get(index).getMemo());
                    cards.remove(index);
                    editCards.add(editCard);
                }
                //endregion
                index = position - 1;
                //region 基準の予定より前の連結予定を抽出
                while(index >= 0 && cards.get(index).getConnect()){
                    editCard = new Card(cards.get(index).getId(),
                            cards.get(index).getDate(),
                            cards.get(index).getStartTime(),
                            cards.get(index).getOverTime(),
                            cards.get(index).getConnect(),
                            cards.get(index).getContent(),
                            cards.get(index).getPlace());
                    editCard.setMemo(cards.get(index).getMemo());
                    cards.remove(index);
                    editCards.add(0, editCard);
                    index--;
                }
                //endregion

                for(int j = 0; j < editCards.size(); j++){
                    Log.d("TEST",editCards.get(j).getDate()+"");
                }

                //生成した予定をAddPlanActivityへ渡す
                Intent intent = new Intent(getActivity(), AddModelActivity.class);
                intent.putExtra("EditCard", editCards);
                intent.putExtra("Index", ++index);
                startActivityForResult(intent, ADD_EDIT_PLAN);
                //endregion
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //region キャンセル
                //endregion
            }
        });
        builder.setNeutralButton("削除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //region 予定を削除
                ((ScheduleApplication)getActivity().getApplication()).deleteCard(cards.get(position).getId());
                cards.remove(position);
                updateList();
                //endregion
            }
        });
        builder.create().show();
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

    //更新したときイベント日を指定したときと情報が異なる
    //その場合の処理が必要

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if(resultCode == RESULT_OK){
            if(requestCode == ADD_EDIT_PLAN){
                //region 予定の追加時
                int index = intent.getIntExtra("Index", -1);
                ((ScheduleApplication)getActivity().getApplication()).saveCard(((ArrayList<Card>) intent.getSerializableExtra("AddEditCards")), index);
                //endregion
            }else {
                return;
            }
            updateList();
        }
    }
}
