package com.example.yukinaito.schedule_xp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class CheckAddWantFragment extends Fragment {
    //データ
    private CheckAddWantFragment.InvestmentAdapter investmentAdapter;
    private CheckAddWantFragment.WasteAdapter wasteAdapter;
    private ArrayList<WantPlanCard> investmentCards;
    private ArrayList<WantPlanCard> wasteCards;

    //ListView
    private ListView investmentWant;
    private ListView wasteWant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_check_add_want, container, false);
        investmentWant = (ListView)view.findViewById(R.id.list_investment);
        wasteWant = (ListView)view.findViewById(R.id.list_waste);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //ListViewの区切り線
        ColorDrawable separate_line_color = new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.separate_line));
        investmentWant.setDivider(separate_line_color);
        investmentWant.setDividerHeight(5);
        wasteWant.setDivider(separate_line_color);
        wasteWant.setDividerHeight(5);

        //Listの描画
        investmentCards = new ArrayList<>();
        wasteCards = new ArrayList<>();
        investmentAdapter = new CheckAddWantFragment.InvestmentAdapter();
        wasteAdapter = new CheckAddWantFragment.WasteAdapter();

        investmentWant.setAdapter(investmentAdapter);
        wasteWant.setAdapter(wasteAdapter);

        WantPlanCard test1 = new WantPlanCard("読書", false, 37, "部屋");
        investmentCards.add(test1);
        WantPlanCard test2 = new WantPlanCard("ゲーム作り", false, 37, "部屋");
        investmentCards.add(test2);
        WantPlanCard test3 = new WantPlanCard("プログラミング学習", false, 37, "部屋");
        investmentCards.add(test3);
        WantPlanCard test4 = new WantPlanCard("ポケモン", false, 37, "部屋");
        wasteCards.add(test4);
        WantPlanCard test5 = new WantPlanCard("GTA", false, 37, "部屋");
        wasteCards.add(test5);

        updateInvestmentList();
        updateWasteList();
    }

    public void onListItemClick(ListView listView, View view, final int position, long id) {
        //ダイアログの生成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("したいことの操作");
        builder.setMessage("したいことの内容を編集、またはしたいことを削除しますか？");
        builder.setPositiveButton("編集", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //region したいことの編集ダイアログ描画
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
                //region したいことを削除
                //endregion
            }
        });
        builder.create().show();
    }

    private class InvestmentAdapter extends BaseAdapter {
        @Override
        public int getCount(){
            return investmentCards.size();
        }

        @Override
        public WantPlanCard getItem(int pos){
            return investmentCards.get(pos);
        }

        @Override
        public long getItemId(int pos){
            return pos;
        }

        @Override
        public View getView(int pos, View view, ViewGroup parent){
            Context context = getActivity().getApplication();
            //ListViewの要素として表示する予定情報
            WantPlanCard card = investmentCards.get(pos);

            //レイアウトの生成
            if(view == null)
                view = (LayoutInflater.from(context)).inflate(R.layout.list_checkwant, null);

            //宣言&初期化
            TextView textView1 = (TextView) view.findViewById(R.id.content);
            TextView textView2 = (TextView) view.findViewById(R.id.place);
            TextView textView3 = (TextView) view.findViewById(R.id.ratio);

            //この処理がないとList更新時前のデータが残る
            textView1.setText("");
            textView2.setText("");
            textView3.setText("");

            //値のセット
            textView1.setText(card.getContent());
            textView2.setText(card.getPlace());
            textView3.setText(Integer.toString(card.getRatio())+"%");

            return view;
        }
    }

    private class WasteAdapter extends BaseAdapter {
        @Override
        public int getCount(){
            return wasteCards.size();
        }

        @Override
        public WantPlanCard getItem(int pos){
            return wasteCards.get(pos);
        }

        @Override
        public long getItemId(int pos){
            return pos;
        }

        @Override
        public View getView(int pos, View view, ViewGroup parent){
            Context context = getActivity().getApplication();
            //ListViewの要素として表示する予定情報
            WantPlanCard card = wasteCards.get(pos);

            //レイアウトの生成
            if(view == null)
                view = (LayoutInflater.from(context)).inflate(R.layout.list_checkwant, null);

            //宣言&初期化
            TextView textView1 = (TextView) view.findViewById(R.id.content);
            TextView textView2 = (TextView) view.findViewById(R.id.place);
            TextView textView3 = (TextView) view.findViewById(R.id.ratio);

            textView3.setVisibility(View.GONE);

            //この処理がないとList更新時前のデータが残る
            textView1.setText("");
            textView2.setText("");

            //値のセット
            textView1.setText(card.getContent());
            textView2.setText(card.getPlace());

            return view;
        }
    }

    //Listの更新
    public void updateWasteList(){
        //setListAdapter(cardAdapter);
        //画面更新
        wasteAdapter.notifyDataSetChanged();
    }
    //Listの更新
    public void updateInvestmentList(){
        //setListAdapter(cardAdapter);
        //画面更新
        investmentAdapter.notifyDataSetChanged();
    }
}
