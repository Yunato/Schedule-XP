package com.example.yukinaito.schedule_xp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CheckWantPlanFragment extends ListFragment {
    private ScheduleApplication scheduleApplication;
    private CheckWantPlanFragment.CardAdapter cardAdapter;
    private ArrayList<WantPlanCard> cards;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        scheduleApplication = (ScheduleApplication)getActivity().getApplication();
        //したいことをすべて取得
        cards = scheduleApplication.getWantplancards();
        View view = inflater.inflate(R.layout.fragment_listmain, container, false);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region ダイアログの描画
                //ダイアログ上に描画するオブジェクト
                LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.dialog_createwanplan, (ViewGroup) getActivity().findViewById(R.id.layout_root));
                ((TextView)layout.findViewById(R.id.input_textview1)).setText("新たなやりたいことの名前を入力してください");
                ((EditText)layout.findViewById(R.id.input_pattern1)).setHint("名前を入力");
                ((TextView)layout.findViewById(R.id.input_textview2)).setText("行う場所を入力してください");
                ((EditText)layout.findViewById(R.id.input_pattern2)).setHint("場所を入力");

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                builder.setTitle("新規作成");
                builder.setView(layout);
                builder.setPositiveButton("作成", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        WantPlanCard card = new WantPlanCard();
                        card.setInfo(((EditText)layout.findViewById(R.id.input_pattern1)).getText().toString(),
                                true, 0,
                                ((EditText)layout.findViewById(R.id.input_pattern2)).getText().toString());
                        scheduleApplication.getWantplancards().add(card);
                        updateListFragment();
                    }
                });
                builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                ((EditText)layout.findViewById(R.id.input_pattern1)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if(inputCheck(layout, "", ""))
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        else
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                });
                ((EditText)layout.findViewById(R.id.input_pattern2)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if(inputCheck(layout, "", ""))
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        else
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                });
                //endregion
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        ColorDrawable separate_line_color = new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.separate_line));
        getListView().setDivider(separate_line_color);
        getListView().setDividerHeight(5);

        //ListViewの値セット
        cardAdapter = new CheckWantPlanFragment.CardAdapter();
        setListAdapter(cardAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, final int pos, long id){
        final int position = pos;
        //ダイアログ上に描画するオブジェクト
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.dialog_createwanplan, (ViewGroup) getActivity().findViewById(R.id.layout_root));
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        ((TextView)layout.findViewById(R.id.input_textview1)).setText("変更したい名前を入力してください");
        ((EditText)layout.findViewById(R.id.input_pattern1)).setText(cards.get(position).getName());
        ((TextView)layout.findViewById(R.id.input_textview2)).setText("行う場所を入力してください");
        ((EditText)layout.findViewById(R.id.input_pattern2)).setText(cards.get(position).getPlace());

        builder.setTitle("編集");
        builder.setView(layout);
        builder.setPositiveButton("削除", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //region リストから削除
                cards.remove(position);
                updateListFragment();
                //endregion
            }
        });
        builder.setNegativeButton("更新", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //region 入力した内容に更新する
                cards.get(position).setInfo(((EditText)layout.findViewById(R.id.input_pattern1)).getText().toString(),
                        true, 0, ((EditText)layout.findViewById(R.id.input_pattern2)).getText().toString());
                updateListFragment();
                //endregion
            }
        });
        builder.setNeutralButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
        ((EditText)layout.findViewById(R.id.input_pattern1)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(inputCheck(layout, cards.get(position).getName(), cards.get(position).getPlace()))
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(true);
                else
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
            }
        });
        ((EditText)layout.findViewById(R.id.input_pattern2)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(inputCheck(layout, cards.get(position).getName(), cards.get(position).getPlace()))
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(true);
                else
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
            }
        });
    }

    private class CardAdapter extends BaseAdapter {
        @Override
        public int getCount(){
            return cards.size();
        }

        @Override
        public WantPlanCard getItem(int pos){
            return cards.get(pos);
        }

        @Override
        public long getItemId(int pos){
            return pos;
        }

        @Override
        public View getView(int pos, View view, ViewGroup parent){
            Context context = getActivity().getApplication();
            final WantPlanCard card = cards.get(pos);

            //レイアウトの生成
            if(view == null){
                view = (LayoutInflater.from(context)).inflate(R.layout.list_checkwantplanfragment, null);
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
            Switch bool = (Switch)view.findViewById(R.id.Switch1);
            TextView textView1 = (TextView)view.findViewById(R.id.TextView1);
            TextView textView2 = (TextView)view.findViewById(R.id.TextView2);

            bool.setChecked(card.getWant());
            textView1.setText(card.getName());
            textView2.setText(Integer.toString(card.getHow()));
            return view;
        }
    }

    //入力情報のチェック trueならbuttonをenableに
    public boolean inputCheck(View layout, String b_name, String b_place){
        String a_name = ((EditText)layout.findViewById(R.id.input_pattern1)).getText().toString();
        String a_place = ((EditText)layout.findViewById(R.id.input_pattern2)).getText().toString();

        if(!(a_name.equals(b_name))&&!(a_place.equals(b_place)))
            return true;
        return false;
    }

    //Listの要素更新
    public void updateListFragment(){
        scheduleApplication.writeWantPlanFile();
        cardAdapter = new CheckWantPlanFragment.CardAdapter();
        setListAdapter(cardAdapter);
        //画面更新
        cardAdapter.notifyDataSetChanged();
    }
}

