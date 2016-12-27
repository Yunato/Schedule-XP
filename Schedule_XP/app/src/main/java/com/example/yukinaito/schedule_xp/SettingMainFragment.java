package com.example.yukinaito.schedule_xp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class SettingMainFragment extends ListFragment {
    private SchedlueApplication schedlueApplication;
    static public final String DATE_PATTERN = "HH:mm";
    private static final int ADD_CODE = 1;
    private static final int UPDATE_CODE = 2;
    private CardAdapter cardAdapter;
    private ArrayList<Card> cards;
    private static int arraypos;
    private static int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Bundle bundle = getArguments();
        schedlueApplication = (SchedlueApplication)getActivity().getApplication();
        this.arraypos = (int)bundle.getSerializable("position");
        cards = schedlueApplication.getModelSchedule().get(this.arraypos).getCards();
        View view = inflater.inflate(R.layout.fragment_settingmain, container, false);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddModelActivity.class);
                startActivityForResult(intent, ADD_CODE);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        cardAdapter = new CardAdapter();
        setListAdapter(cardAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id){
        this.position = pos;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("モデルの操作");
        builder.setMessage("モデルの内容を編集、またはモデルを削除しますか？");
        builder.setPositiveButton("削除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cards.remove(position);
                updateListfragment();
            }
        });
        builder.setNegativeButton("編集", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getActivity(), AddModelActivity.class);
                Card card = new Card();
                card.setInfo(schedlueApplication.getModelSchedule().get(arraypos).getCards().get(position).getCalendar(),
                        schedlueApplication.getModelSchedule().get(arraypos).getCards().get(position).getLentime(),
                        schedlueApplication.getModelSchedule().get(arraypos).getCards().get(position).getContent(),
                        schedlueApplication.getModelSchedule().get(arraypos).getCards().get(position).getPlace());
                intent.putExtra("EditingCard", card);
                startActivityForResult(intent,UPDATE_CODE);
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
            //Context context = getActivity();
            Context context = getActivity().getApplication();
            Card card = cards.get(pos);

            //レイアウトの生成
            if(view == null){
                LinearLayout layout = new LinearLayout(context);
                layout.setBackgroundColor(Color.WHITE);
                layout.setPadding(10, 10, 10, 10);
                layout.setOrientation(LinearLayout.HORIZONTAL);

                view = layout;

                TextView textView1 = new TextView(context);
                textView1.setTag("time");
                textView1.setTextColor(Color.BLACK);
                textView1.setPadding(30, 10, 10, 10);
                textView1.setTextSize(60.0f);
                layout.addView(textView1);

                LinearLayout layout2 = new LinearLayout(context);
                layout2.setBackgroundColor(Color.WHITE);
                layout2.setPadding(0, 0, 0, 0);
                layout2.setOrientation(LinearLayout.VERTICAL);

                TextView textView2 = new TextView(context);
                textView2.setTag("content");
                textView2.setTextColor(Color.BLACK);
                textView2.setPadding(10,10, 10, 10);
                textView2.setTextSize(30.0f);
                layout2.addView(textView2);

                TextView textView3 = new TextView(context);
                textView3.setTag("place");
                textView3.setTextColor(Color.BLACK);
                textView3.setPadding(10, 10, 10, 10);
                textView3.setTextSize(30.0f);
                layout2.addView(textView3);

                layout.addView(layout2);
            }

            TextView textView1 = (TextView)view.findViewWithTag("time");
            textView1.setText(convertDate2String(card.getCalendar().getTime()));
            TextView textView2 = (TextView)view.findViewWithTag("content");
            textView2.setText(card.getContent());
            TextView textView3 = (TextView)view.findViewWithTag("place");
            textView3.setText(card.getPlace());
            return view;
        }
    }

    public String convertDate2String(java.util.Date date) {
        return (new SimpleDateFormat(DATE_PATTERN)).format(date);
    }

    public void updateListfragment(){
        cardAdapter = new CardAdapter();
        setListAdapter(cardAdapter);
        cardAdapter.notifyDataSetChanged();
        schedlueApplication.writeModelFile();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_CODE){
            if(resultCode == RESULT_OK) {
                cards.add((Card) data.getSerializableExtra("Card"));
                updateListfragment();
            }
        }else if(requestCode == UPDATE_CODE){
            if(resultCode == RESULT_OK){
                cards.get(position).setUpdate((Card)data.getSerializableExtra("Card"));
                updateListfragment();
            }
        }
    }
}
