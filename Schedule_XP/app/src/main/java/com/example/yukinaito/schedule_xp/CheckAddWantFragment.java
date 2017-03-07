package com.example.yukinaito.schedule_xp;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

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
    //TextVie
    private TextView investmentSum;
    private TextView wasteSum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_check_add_want, container, false);
        investmentWant = (ListView)view.findViewById(R.id.list_investment);
        wasteWant = (ListView)view.findViewById(R.id.list_waste);
        investmentSum = (TextView)view.findViewById(R.id.number_investment);
        wasteSum = (TextView)view.findViewById(R.id.number_waste);
        investmentCards = ((ScheduleApplication)getActivity().getApplication()).getInvestmentCards();
        wasteCards = ((ScheduleApplication)getActivity().getApplication()).getWasteCards();

        //region リスナーの登録
        view.findViewById(R.id.add_investment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog(0, false, null);
            }
        });
        view.findViewById(R.id.add_waste).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog(1, false, null);
            }
        });
        investmentWant.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                createDialog(0, true, (WantPlanCard)((ListView)parent).getItemAtPosition(position));
            }
        });
        wasteWant.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                createDialog(1, true, (WantPlanCard)((ListView)parent).getItemAtPosition(position));
            }
        });
        //endregion

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
        investmentAdapter = new CheckAddWantFragment.InvestmentAdapter();
        wasteAdapter = new CheckAddWantFragment.WasteAdapter();

        investmentWant.setAdapter(investmentAdapter);
        wasteWant.setAdapter(wasteAdapter);

        updateInvestmentList();
        updateWasteList();

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
            if(view == null){
                view = (LayoutInflater.from(context)).inflate(R.layout.list_checkwant, null);
                ((Switch)view.findViewById(R.id.Switch1)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        for(int i = 0; i < investmentCards.size(); i++){
                            if(investmentCards.get(i).getId().equals(compoundButton.getTag())){
                                investmentCards.get(i).setActive(b);
                                ((ScheduleApplication)getActivity().getApplication()).updateCard(investmentCards.get(i).getId(), investmentCards.get(i), false);
                                break;
                            }
                        }
                    }
                });
                view.findViewById(R.id.ratio).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for(int i = 0; i < investmentCards.size(); i++){
                            if(investmentCards.get(i).getId().equals(view.getTag())){
                                settingRatio(investmentCards.get(i));
                                break;
                            }
                        }
                    }
                });
            }

            //宣言&初期化
            Switch switch1 = (Switch) view.findViewById(R.id.Switch1);
            TextView textView1 = (TextView) view.findViewById(R.id.content);
            TextView textView2 = (TextView) view.findViewById(R.id.place);
            Button button = (Button) view.findViewById(R.id.ratio);

            //この処理がないとList更新時前のデータが残る
            textView1.setText("");
            textView2.setText("");
            button.setText("");

            //値のセット
            switch1.setChecked(card.getActive());
            textView1.setText(card.getContent());
            textView2.setText(card.getPlace());
            button.setText(Integer.toString(card.getRatio()));
            switch1.setTag(card.getId());
            button.setTag(card.getId());

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
            FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.frameLayout);

            view.findViewById(R.id.Switch1).setVisibility(View.GONE);
            frameLayout.setVisibility(View.GONE);

            //この処理がないとList更新時前のデータが残る
            textView1.setText("");
            textView2.setText("");

            //値のセット
            textView1.setText(card.getContent());
            textView2.setText(card.getPlace());

            return view;
        }
    }

    public void createDialog(final int id, boolean flag, final WantPlanCard editCard){
        //region ダイアログの生成
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.dialog_createwant, (ViewGroup)getActivity().findViewById(R.id.layout_root));
        String positive = "作成";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if(id == 0)
            builder.setTitle("新規作成(投資)");
        else if(id == 1)
            builder.setTitle("新規作成(浪費)");
        builder.setView(layout);
        if(flag){
            positive = "更新";
            ((EditText)layout.findViewById(R.id.input_content)).setText(editCard.getContent());
            ((EditText)layout.findViewById(R.id.input_place)).setText(editCard.getPlace());
            builder.setNeutralButton("削除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //region したいことを削除
                    ((ScheduleApplication)getActivity().getApplication()).deleteCard(editCard.getId());
                    if(id == 0){
                        for(int index = 0; index < investmentCards.size(); index++){
                            if(investmentCards.get(index) == editCard){
                                investmentCards.remove(index);
                                break;
                            }
                        }
                        updateInvestmentList();
                    }else{
                        for(int index = 0; index < wasteCards.size(); index++){
                            if(wasteCards.get(index) == editCard){
                                wasteCards.remove(index);
                                break;
                            }
                        }
                        updateWasteList();
                    }
                    //endregion
                }
            });
        }else{
        }
        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //region "作成"または"更新"のタップ時
                //変更必要
                WantPlanCard addCard;
                addCard = new WantPlanCard(((EditText)layout.findViewById(R.id.input_content)).getText().toString(),
                        false, 1, ((EditText)layout.findViewById(R.id.input_place)).getText().toString(), -1 * (id + 1));
                if(editCard == null){
                    ((ScheduleApplication)getActivity().getApplication()).saveCard(addCard);
                }else{
                    ((ScheduleApplication)getActivity().getApplication()).updateCard(editCard.getId(), addCard, true);
                    if(id == 0){
                        for(int index = 0; index < investmentCards.size(); index++){
                            if(investmentCards.get(index) == editCard){
                                investmentCards.remove(index);
                                break;
                            }
                        }
                        updateInvestmentList();
                    }else{
                        for(int index = 0; index < wasteCards.size(); index++){
                            if(wasteCards.get(index) == editCard){
                                wasteCards.remove(index);
                                break;
                            }
                        }
                        updateWasteList();
                    }
                }
                if(id == 0){
                    updateInvestmentList();
                }else{
                    updateWasteList();
                }
                //endregion
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //region "キャンセル"のタップ時
                //endregion
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        ((EditText)layout.findViewById(R.id.input_content)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //入力チェック 空欄でないかどうか
                if(((EditText)layout.findViewById(R.id.input_content)).getText().toString().trim().length() != 0
                        && ((EditText)layout.findViewById(R.id.input_place)).getText().toString().trim().length() != 0
                        || (editCard != null && ((EditText)layout.findViewById(R.id.input_content)).getText().toString().equals(editCard.getContent())
                        && ((EditText)layout.findViewById(R.id.input_place)).getText().toString().equals(editCard.getPlace())))
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                else
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
        });
        ((EditText)layout.findViewById(R.id.input_place)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //入力チェック 空欄でないかどうか
                if(((EditText)layout.findViewById(R.id.input_content)).getText().toString().trim().length() != 0
                        && ((EditText)layout.findViewById(R.id.input_place)).getText().toString().trim().length() != 0
                        || (editCard != null && ((EditText)layout.findViewById(R.id.input_content)).getText().toString().equals(editCard.getContent())
                        && ((EditText)layout.findViewById(R.id.input_place)).getText().toString().equals(editCard.getPlace())))
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                else
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
        });
        //endregion
    }

    public void settingRatio(final WantPlanCard editCard){
        //region ダイアログの生成
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.dialog_editratio, (ViewGroup)getActivity().findViewById(R.id.root));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //region "更新"のタップ時
                editCard.setRatio(Integer.parseInt(((EditText)layout.findViewById(R.id.input_ratio)).getText().toString()));
                ((ScheduleApplication)getActivity().getApplication()).updateCard(editCard.getId(), editCard, true);
                for(int index = 0; index < investmentCards.size(); index++){
                    if(investmentCards.get(index) == editCard){
                        investmentCards.remove(index);
                        break;
                    }
                }
                updateInvestmentList();
                //endregion
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //region "キャンセル"のタップ時
                //endregion
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        ((EditText)layout.findViewById(R.id.input_ratio)).setText(Integer.toString(editCard.getRatio()));
        ((EditText)layout.findViewById(R.id.input_ratio)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //入力チェック 空欄でないかどうか
                if(((EditText)layout.findViewById(R.id.input_ratio)).getText().toString().toString().equals("0") ||
                        ((EditText)layout.findViewById(R.id.input_ratio)).getText().toString().trim().length() == 0){
                    ((EditText)layout.findViewById(R.id.input_ratio)).setText("1");
                }
                Log.d("TEST", ((EditText)layout.findViewById(R.id.input_ratio)).getText().toString() + " " + Integer.toString(editCard.getRatio()));
                Log.d("TEST", Boolean.toString(((EditText)layout.findViewById(R.id.input_ratio)).getText().toString().equals(Integer.toString(editCard.getRatio()))));
                if(!((EditText)layout.findViewById(R.id.input_ratio)).getText().toString().equals(Integer.toString(editCard.getRatio()))){
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
                else{
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }
        });
        (layout.findViewById(R.id.down)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(((EditText)layout.findViewById(R.id.input_ratio)).getText().toString());
                if(count > 1){
                    ((EditText)layout.findViewById(R.id.input_ratio)).setText(Integer.toString(count - 1));
                }
            }
        });
        (layout.findViewById(R.id.up)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(((EditText)layout.findViewById(R.id.input_ratio)).getText().toString());
                ((EditText)layout.findViewById(R.id.input_ratio)).setText(Integer.toString(count + 1));
            }
        });
        //endregion
    }

    //Listの更新
    public void updateInvestmentList(){
        //setListAdapter(cardAdapter);
        //画面更新
        investmentSum.setText(investmentCards.size() + "個");
        investmentAdapter.notifyDataSetChanged();
    }

    //Listの更新
    public void updateWasteList(){
        //setListAdapter(cardAdapter);
        //画面更新
        wasteSum.setText(wasteCards.size() + "個");
        wasteAdapter.notifyDataSetChanged();
    }
}
