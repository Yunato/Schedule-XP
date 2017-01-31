package com.example.yukinaito.schedule_xp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;

public class AddModelActivity extends AppCompatActivity
        implements TextWatcher    {
    private Card card;
    private int plan_Time = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmodel);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //region 前画面に戻るボタンの生成
        final Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.abc_ic_ab_back_material, null);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorsimbol), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        //endregion

        findViewById(R.id.button_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region 予定の開始時刻を選択 DatePickerの呼び出し
                TimePickerDialogFragment timePicker = new TimePickerDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("activity", 2);
                timePicker.setArguments(bundle);
                timePicker.show(getSupportFragmentManager(), "timePicker");
                //endregion
            }
        });
        findViewById(R.id.button_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region 情報をすべて入力し終わったあとの確認
                long calendar = (long)plan_Time;
                Card save = new Card();
                save.setInfo(card.getCalendar(), card.getLenTime(), card.getContent(), card.getPlace());

                card.setInfo(calendar,
                        Integer.parseInt(((EditText)findViewById(R.id.editText1)).getText().toString()),
                        ((EditText)findViewById(R.id.editText2)).getText().toString(),
                        ((EditText)findViewById(R.id.editText3)).getText().toString());

                int position = addCheck(card);
                if(position > -1) {
                    //追加する位置が正しいときは値を返す
                    Intent intent = new Intent();
                    intent.putExtra("Card", card);
                    intent.putExtra("Position", position);
                    intent.putExtra("EditPos", getIntent().getIntExtra("Position", -1));
                    intent.putExtra("Event_Index", getIntent().getIntExtra("Event_Index", -1));
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    //追加する位置が見つからなかった場合
                    position = (position + 1) * -1;
                    card = save;

                    //追加できないことをダイアログで知らせる
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddModelActivity.this);
                    builder.setTitle("警告");
                    builder.setMessage("他の予定と時間が重なっています。\n重なっている予定:" + ((ScheduleApplication)getApplication()).getModelSchedule().get(getIntent().getIntExtra("modelIndex", -1)).getCards().get(position).getContent());
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                //endregion
            }
        });
        findViewById(R.id.button_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region キャンセルボタンタップ時
                Intent intent = new Intent();
                intent.putExtra("Card", card);
                intent.putExtra("EditPos", getIntent().getIntExtra("Position", -1));
                intent.putExtra("Event_Index", getIntent().getIntExtra("Event_Index", -1));
                setResult(RESULT_OK, intent);
                finish();
                //endregion
            }
        });

        ((EditText)findViewById(R.id.editText1)).addTextChangedListener(this);
        ((EditText)findViewById(R.id.editText2)).addTextChangedListener(this);
        ((EditText)findViewById(R.id.editText3)).addTextChangedListener(this);
        (findViewById(R.id.button_2)).setEnabled(false);

        //region 分岐する起動処理
        if((getIntent().getSerializableExtra("EditingCard")) != null){
            //編集の場合 編集する予定の情報を反映させる
            setTitle("ひな形(モデル)の変更");
            card = ((Card)getIntent().getSerializableExtra("EditingCard"));
            Format format = new DecimalFormat("00");
            int start = (int)card.getCalendar();
            start = (start / 100) * 60 + (start % 100);
            ((Button)findViewById(R.id.button_1)).setText(format.format(start/60) + "時" + format.format(start%60) + "分(変更時はタップ)");
            ((Button)findViewById(R.id.button_2)).setText("更新");
            ((EditText)findViewById(R.id.editText1)).setText(Integer.toString(card.getLenTime()));
            ((EditText)findViewById(R.id.editText2)).setText(card.getContent());
            ((EditText)findViewById(R.id.editText3)).setText(card.getPlace());
            plan_Time = (int)card.getCalendar();
        }else {
            //新規作成の場合
            setTitle("ひな形(モデル)の追加");
            card = new Card();
        }
        //endregion
    }

    public void onReturnValue(int data, String text, int picker) {
        if(picker == 2) {
            plan_Time = data;
            Button button = (Button)findViewById(R.id.button_1);
            button.setText(text + "(変更時はタップ)");
        }
        inputCheck();
    }

    //region EditTextのリスナーに登録された3つのイベント inputCheck()のみ
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        inputCheck();
    }
    //endregion

    //入力チェック
    public void inputCheck(){
        if(plan_Time==-1||
                (((EditText)findViewById(R.id.editText1)).getText().toString()).equals("")||
                (((EditText)findViewById(R.id.editText2)).getText().toString()).equals("")||
                (((EditText)findViewById(R.id.editText3)).getText().toString()).equals(""))
            (findViewById(R.id.button_2)).setEnabled(false);
        if(plan_Time!=-1&&
                !(((EditText)findViewById(R.id.editText1)).getText().toString()).equals("")&&
                !(((EditText)findViewById(R.id.editText2)).getText().toString()).equals("")&&
                !(((EditText)findViewById(R.id.editText3)).getText().toString()).equals(""))
            (findViewById(R.id.button_2)).setEnabled(true);
    }

    //追加する予定の位置を取得
    public int addCheck(Card card){
        int position;
        long start1, start2, end1, end2;
        //全ての予定情報を取得
        ArrayList<Card> cards = ((ScheduleApplication)this.getApplication()).getModelSchedule().get(getIntent().getIntExtra("modelIndex", -1)).getCards();

        //予定情報がないか
        if (cards.size() == 0)
            return 0;
        //追加する予定が他の予定と重ならないか
        for(position = 0; position < cards.size(); position++){
            start1 = cards.get(position).getCalendar() / 10000;
            start2 = card.getCalendar() / 10000;
            if(start1 == start2) {
                //時間を秒に変換するための退避用変数
                long buffer = cards.get(position).getCalendar() % 10000;
                start1 = (buffer / 100) * 60 + (buffer % 100);
                end1 = start1 + cards.get(position).getLenTime();
                buffer = card.getCalendar() % 10000;
                start2 = (buffer / 100) * 60 + (buffer % 100);
                end2 = start2 + card.getLenTime();
                if (start1 >= start2) {
                    if (start1 < end2 && start2 < end1)
                        return -1 * position - 1;
                    else {
                        if (cards.size() > position + 1 && start1 == start2 && start1 == end1)
                            return positionDownCheck(cards, position + 1, start2, end2);
                        if (position > 0 && start1 == start2 && start1 == end1)
                            return positionUpCheck(cards, position - 1, start2, end2);
                        return position;
                    }
                }else
                if(start2 < end1)
                    return -1 * position -1;
            }
            if(start1 > start2)
                return position;
        }
        return cards.size();
    }

    public int positionUpCheck(ArrayList<Card> cards, int position, long start2, long end2){
        //時間を秒に変換するための退避用変数
        long buffer = cards.get(position).getCalendar() % 10000;
        long start1  = (buffer / 100) * 60 + (buffer % 100);
        long end1 = start1 + cards.get(position).getLenTime();
        if (start1 < end2 && start2 < end1)
            return -1 * (position - 1) - 1;
        else {
            if (start2 < end1)
                return positionUpCheck(cards, position - 1, start2, end2);
            else
                return position;
        }
    }

    public int positionDownCheck(ArrayList<Card> cards, int position, long start2, long end2) {
        //時間を秒に変換するための退避用変数
        long buffer = cards.get(position).getCalendar() % 10000;
        long start1  = (buffer / 100) * 60 + (buffer % 100);
        long end1 = start1 + cards.get(position).getLenTime();
        if(start1 != start2 || start1 != end1)
            return position - 1;
        if (start1 < end2 && start2 < end1)
            return -1 * (position - 1) - 1;
        else{
            if(cards.size() > position + 1)
                return positionDownCheck(cards, position + 1, start2, end2);
            else
                return position;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            //戻るボタンを押されたときの処理
            Intent intent = new Intent();
            intent.putExtra("Card", card);
            intent.putExtra("EditPos", getIntent().getIntExtra("Position", -1));
            intent.putExtra("Event_Index", getIntent().getIntExtra("Event_Index", -1));
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}