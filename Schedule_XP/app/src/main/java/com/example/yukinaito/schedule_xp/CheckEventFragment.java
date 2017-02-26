package com.example.yukinaito.schedule_xp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class CheckEventFragment extends ListFragment {
    //requestCode
    private static final int ADD_EDIT_PLAN = 1;

    //データ
    private CheckEventFragment.CardAdapter cardAdapter;
    private ArrayList<EventPlanCard> cards;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_listandfbutton, container, false);
        cards = ((ScheduleApplication)getActivity().getApplication()).getEventCards();
        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region イベント日の追加画面へ遷移
                Intent intent = new Intent(getActivity(), AddEventActivity.class);
                startActivityForResult(intent, ADD_EDIT_PLAN);
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

        //Listの描画
        cardAdapter = new CheckEventFragment.CardAdapter();
        updateList();
    }

    @Override
    public void onListItemClick(ListView listView, View view, final int position, long id) {
        //ダイアログの生成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("イベント日の操作");
        builder.setMessage("イベント日の内容を編集、またはイベント日を削除しますか？");
        builder.setPositiveButton("編集", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //region イベント日の編集画面へ遷移
                EventPlanCard editCard = new EventPlanCard(cards.get(position).getDate(),
                        cards.get(position).getTitle(),
                        cards.get(position).getIndex());
                cards.remove(position);

                //生成した予定をAddPlanActivityへ渡す
                Intent intent = new Intent(getActivity(), AddEventActivity.class);
                intent.putExtra("EditCard", editCard);
                intent.putExtra("Index", position);
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
                //region イベント日を削除
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
        public EventPlanCard getItem(int pos){
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
            EventPlanCard card = cards.get(pos);

            //レイアウトの生成
            if(view == null)
                view = (LayoutInflater.from(context)).inflate(R.layout.list_checkevent, null);

            //宣言&初期化
            String date = Integer.toString(card.getDate());
            TextView textView1 = (TextView) view.findViewById(R.id.date);
            TextView textView2 = (TextView) view.findViewById(R.id.title);
            TextView textView3 = (TextView) view.findViewById(R.id.modelIndex);

            //この処理がないとList更新時前のデータが残る
            textView1.setText("");
            textView2.setText("");
            textView3.setText("");

            //値のセット
            textView1.setText(date.substring(0, 4) + "/" + date.substring(4, 6) + "/" + date.substring(6, 8));
            textView2.setText(card.getTitle());
            textView3.setText("ソースコード変更必須");

            return view;
        }
    }

    //Listの更新
    public void updateList(){
        setListAdapter(cardAdapter);
        //画面更新
        cardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if(resultCode == RESULT_OK){
            if(requestCode == ADD_EDIT_PLAN){
                //region イベント日の追加|更新時
                int index = intent.getIntExtra("Index", -1);
                if(index == cards.size()){
                    cards.add((EventPlanCard) intent.getSerializableExtra("AddEditCard"));
                }else{
                    cards.add(index, (EventPlanCard)intent.getSerializableExtra("AddEditCard"));
                }
                //endregion
            }else{
                return;
            }
            updateList();
        }
    }
}
