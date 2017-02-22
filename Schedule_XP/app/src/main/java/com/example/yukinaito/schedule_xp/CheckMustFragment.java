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

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;

public class CheckMustFragment extends ListFragment {
    //requestCode
    private static final int ADD_PLAN = 1;
    private static final int EDIT_PLAN = 2;

    //データ
    private CheckMustFragment.CardAdapter cardAdapter;
    private ArrayList<MustPlanCard> cards;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_listandfbutton, container, false);
        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region すべきことの追加画面へ遷移
                Intent intent = new Intent(getActivity(), AddMustActivity.class);
                startActivityForResult(intent, ADD_PLAN);
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
        cards = new ArrayList<>();
        cardAdapter = new CheckMustFragment.CardAdapter();
        updateList();
    }

    @Override
    public void onListItemClick(ListView listView, View view, final int position, long id) {
        //ダイアログの生成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("すべきことの操作");
        builder.setMessage("すべきことの内容を編集、またはすべきことを削除しますか？");
        builder.setPositiveButton("編集", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //region すべきことの編集画面へ遷移
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
                //region すべきことを削除
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
            //ListViewの要素として表示する予定情報
            MustPlanCard card = cards.get(pos);

            //レイアウトの生成
            if(view == null)
                view = (LayoutInflater.from(context)).inflate(R.layout.list_checkmust, null);

            //宣言&初期化
            Format format = new DecimalFormat("00");
            String date = Integer.toString(card.getLimitDate());
            int finish = card.getLimitTime();
            TextView textView1 = (TextView) view.findViewById(R.id.content);
            TextView textView2 = (TextView) view.findViewById(R.id.limitTime);

            //データの形式変更
            finish = (finish / 100) * 60 + (finish % 100);
            date = date.substring(0, 4) + "/" + date.substring(4, 6) + "/" + date.substring(6, 8)
                    + " " + format.format(finish / 60) + ":" + format.format(finish % 60);

            //この処理がないとList更新時前のデータが残る
            textView1.setText("");
            textView2.setText("");

            //値のセット
            textView1.setText(card.getContent());
            textView2.setText(date);

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
        if(requestCode == ADD_PLAN){
            //region すべきことの追加時
            //endregion
        }else if(requestCode == EDIT_PLAN){
            //region すべきことの更新時
            //endregion
        }else {
            return;
        }
        updateList();
    }
}
