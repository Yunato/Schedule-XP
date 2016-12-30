package com.example.yukinaito.schedule_xp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CheckWantPlanFragment extends ListFragment {
    private SchedlueApplication schedlueApplication;
    private static final int ADD_CODE = 1;
    private static final int UPDATE_CODE = 2;
    private CheckWantPlanFragment.CardAdapter cardAdapter;
    private ArrayList<WantPlanCard> cards;
    private static int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        schedlueApplication = (SchedlueApplication)getActivity().getApplication();
        cards = schedlueApplication.getWantplancards();
        View view = inflater.inflate(R.layout.fragment_listmain, container, false);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final WantPlanCard card = new WantPlanCard();
                card.setInfo("",false,0,"");
                LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(
                        LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.dialog_createwanplan,
                        (ViewGroup) getActivity().findViewById(R.id.layout_root));
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                ((TextView)layout.findViewById(R.id.input_textview1)).setText("新たなやりたいことの名前を入力してください");
                ((EditText)layout.findViewById(R.id.input_pattern1)).setHint("名前を入力");
                ((TextView)layout.findViewById(R.id.input_textview2)).setText("行う場所を入力してください");
                ((EditText)layout.findViewById(R.id.input_pattern2)).setHint("場所を入力");
                builder.setTitle("新規作成");
                builder.setView(layout);
                builder.setPositiveButton("作成", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        card.setInfo(((EditText)layout.findViewById(R.id.input_pattern1)).getText().toString(),
                                true, 0,
                                ((EditText)layout.findViewById(R.id.input_pattern2)).getText().toString());
                        schedlueApplication.getWantplancards().add(card);
                        updateListfragment();
                    }
                });
                builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON1).setEnabled(false);
                ((EditText)layout.findViewById(R.id.input_pattern1)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if(inputCheck(layout, card))
                            dialog.getButton(AlertDialog.BUTTON1).setEnabled(true);
                        else
                            dialog.getButton(AlertDialog.BUTTON1).setEnabled(false);
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
                        if(inputCheck(layout, card))
                            dialog.getButton(AlertDialog.BUTTON1).setEnabled(true);
                        else
                            dialog.getButton(AlertDialog.BUTTON1).setEnabled(false);
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        ColorDrawable separate_line_color = new ColorDrawable(this.getResources().getColor(R.color.separate_line));
        getListView().setDivider(separate_line_color);
        getListView().setDividerHeight(5);
        cardAdapter = new CheckWantPlanFragment.CardAdapter();
        setListAdapter(cardAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id){
        this.position = pos;
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(
                LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.dialog_createwanplan,
                (ViewGroup) getActivity().findViewById(R.id.layout_root));
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        ((TextView)layout.findViewById(R.id.input_textview1)).setText("変更したい名前を入力してください");
        ((EditText)layout.findViewById(R.id.input_pattern1)).setText(cards.get(position).getName());
        ((TextView)layout.findViewById(R.id.input_textview2)).setText("行う場所を入力してください");
        ((EditText)layout.findViewById(R.id.input_pattern2)).setText(cards.get(position).getPlace());
        builder.setTitle("編集");
        builder.setView(layout);
        builder.setPositiveButton("削除", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                cards.remove(position);
                updateListfragment();
            }
        });
        builder.setNegativeButton("更新", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                cards.get(position).setInfo(((EditText)layout.findViewById(R.id.input_pattern1)).getText().toString(),
                        true, 0,
                        ((EditText)layout.findViewById(R.id.input_pattern2)).getText().toString());
                updateListfragment();
            }
        });
        builder.setNeutralButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON2).setEnabled(false);
        ((EditText)layout.findViewById(R.id.input_pattern1)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editCheck(layout, cards.get(position)))
                    dialog.getButton(AlertDialog.BUTTON2).setEnabled(true);
                else
                    dialog.getButton(AlertDialog.BUTTON2).setEnabled(false);
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
                if(editCheck(layout, cards.get(position)))
                    dialog.getButton(AlertDialog.BUTTON2).setEnabled(true);;
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
            //Context context = getActivity();
            Context context = getActivity().getApplication();
            final WantPlanCard card = cards.get(pos);

            //レイアウトの生成
            if(view == null){
                LinearLayout layout = new LinearLayout(context);
                layout.setBackgroundColor(Color.WHITE);
                layout.setOrientation(LinearLayout.HORIZONTAL);

                view = layout;

                RelativeLayout layout1 = new RelativeLayout(context);
                layout1.setBackgroundColor(Color.WHITE);
                layout.addView(layout1,  new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT));

                Switch switch1 = new Switch(context);
                switch1.setChecked(card.getWant());
                switch1.setPadding(10, 10, 10, 10);
                switch1.setFocusableInTouchMode(false);
                switch1.setFocusable(false);
                switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked == true) {
                            card.setWant(true);
                            buttonView.setChecked(true);
                        } else {
                            card.setWant(false);
                            buttonView.setChecked(false);
                        }
                        updateListfragment();
                    }
                });

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
                lp.addRule(RelativeLayout.ALIGN_BOTTOM);
                layout1.addView(switch1, lp);

                LinearLayout layout2 = new LinearLayout(context);
                layout2.setBackgroundColor(Color.WHITE);
                layout2.setOrientation(LinearLayout.VERTICAL);
                int id = getContext().getResources().getIdentifier("dotted_line4", "drawable", getContext().getPackageName());
                Drawable back = getContext().getResources().getDrawable(id);
                layout2.setBackground(back);
                layout.addView(layout2, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                TextView textView1 = new TextView(context);
                textView1.setTextColor(Color.parseColor("#424242"));
                textView1.setPadding(10,10, 10, 10);
                textView1.setTextSize(45.0f);
                id = getContext().getResources().getIdentifier("dotted_line1", "drawable", getContext().getPackageName());
                back = getContext().getResources().getDrawable(id);
                textView1.setBackground(back);
                textView1.setText(card.getName());
                layout2.addView(textView1);

                LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

                TextView textView2 = new TextView(context);
                textView2.setTextColor(Color.parseColor("#424242"));
                textView2.setPadding(10, 10, 10, 10);
                textView2.setTextSize(20.0f);
                textView2.setGravity(Gravity.RIGHT);
                textView2.setText(Integer.toString(card.getHow()));
                layout2.addView(textView2, lp1);
            }
            return view;
        }
    }

    public boolean inputCheck(View layout, WantPlanCard card){
        String start1, start2, end1, end2;
        start1 = ((EditText)layout.findViewById(R.id.input_pattern1)).getText().toString();
        end1 = card.getName();
        start2 = ((EditText)layout.findViewById(R.id.input_pattern2)).getText().toString();
        end2 = card.getPlace();
        if(!(start1.equals(end1))&& !(start2.equals(end2))) {
            return true;
        }
        return false;
    }

    public boolean editCheck(View layout, WantPlanCard card){
        String start1, start2, end1, end2;
        start1 = ((EditText)layout.findViewById(R.id.input_pattern1)).getText().toString();
        end1 = card.getName();
        start2 = ((EditText)layout.findViewById(R.id.input_pattern2)).getText().toString();
        end2 = card.getPlace();
        if(!(start1.equals(end1))|| !(start2.equals(end2))) {
            return true;
        }
        return false;
    }

    public void updateListfragment(){
        schedlueApplication.writeWantPlanFile();
        cardAdapter = new CheckWantPlanFragment.CardAdapter();
        setListAdapter(cardAdapter);
        cardAdapter.notifyDataSetChanged();
    }
}

