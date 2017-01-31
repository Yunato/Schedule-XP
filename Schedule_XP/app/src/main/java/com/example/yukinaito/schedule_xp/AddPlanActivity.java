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

import java.util.ArrayList;
import java.util.Calendar;

public class AddPlanActivity extends AppCompatActivity
        implements TextWatcher {
    private Card card;
    private int plan_Day = -1;
    private int plan_Time = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addattime);
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
                //region 予定の開始の日付を選択 DatePickerの呼び出し
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("activity", 1);
                datePicker.setArguments(bundle);
                datePicker.show(getSupportFragmentManager(), "datePicker");
                //endregion
            }
        });
        findViewById(R.id.button_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region 予定の開始時刻を選択 DatePickerの呼び出し
                TimePickerDialogFragment timePicker = new TimePickerDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("activity", 1);
                timePicker.setArguments(bundle);
                timePicker.show(getSupportFragmentManager(), "timePicker");
                //endregion
            }
        });
        findViewById(R.id.button_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region 情報をすべて入力し終わったあとの確認
                long calendar = (long)plan_Day * 10000 + (long)plan_Time;
                //追加できなかったとき用にバックアップ
                Card save = new Card();
                save.setInfo(card.getCalendar(), card.getLenTime(), card.getContent(), card.getPlace());

                card.setInfo(calendar,
                        Integer.parseInt(((EditText)findViewById(R.id.editText1)).getText().toString()),
                        ((EditText)findViewById(R.id.editText2)).getText().toString(),
                        ((EditText)findViewById(R.id.editText3)).getText().toString());

                //現在時刻を取得
                Calendar now = Calendar.getInstance();
                long time = now.get(Calendar.YEAR);
                time = time * 100 + now.get(Calendar.MONTH) + 1;
                time = time * 100 + now.get(Calendar.DATE);
                time = time * 100 + now.get(Calendar.HOUR_OF_DAY);
                time = time * 100 + now.get(Calendar.MINUTE);

                //追加する予定が現時刻より未来を示すか
                if(time <= calendar){
                    //現時刻より未来を示すとき
                    int position = addCheck(card);
                    if(position > -1) {
                        //追加する位置が正しいときは値を返す
                        Intent intent = new Intent();
                        intent.putExtra("Card", card);
                        intent.putExtra("Position", position);
                        setResult(RESULT_OK, intent);
                        finish();
                    }else{
                        //追加する位置が見つからなかった場合
                        position = (position + 1) * -1;
                        card = save;

                        //追加できないことをダイアログで知らせる
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddPlanActivity.this);
                        builder.setTitle("警告");
                        builder.setMessage("他の予定と時間が重なっています。\n重なっている予定:" + ((ScheduleApplication) getApplication()).getPlanCard().get(position).getContent());
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }else {
                    //現時刻より過去を示すとき
                    //予定開始時刻が正しくないことをダイアログで知らせる
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddPlanActivity.this);
                    builder.setTitle("警告");
                    builder.setMessage("入力情報が不適切です。現時刻より前の時刻を指定しています。");
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
        findViewById(R.id.button_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region キャンセルボタンタップ時
                Intent intent = new Intent();
                intent.putExtra("Card", card);
                intent.putExtra("EditPos", getIntent().getIntExtra("Position", -1));
                setResult(RESULT_OK, intent);
                finish();
                //endregion
            }
        });

        ((EditText)findViewById(R.id.editText1)).addTextChangedListener(this);
        ((EditText)findViewById(R.id.editText2)).addTextChangedListener(this);
        ((EditText)findViewById(R.id.editText3)).addTextChangedListener(this);
        (findViewById(R.id.button_3)).setEnabled(false);

        //region 分岐する起動処理
        if((getIntent().getSerializableExtra("EditingCard")) != null){
            //編集の場合 編集する予定の情報を反映させる
            setTitle("日時・行動の変更");
            card = ((Card)getIntent().getSerializableExtra("EditingCard"));
            String start = Long.toString(card.getCalendar());
            ((Button)findViewById(R.id.button_1)).setText(start.substring(0, 4) + "年" + start.substring(4, 6) + "月" + start.substring(6, 8) + "日(変更時はタップ");
            ((Button)findViewById(R.id.button_2)).setText(start.substring(8, 10) + "時" + start.substring(10, 12) + "分(変更時はタップ)");
            ((Button)findViewById(R.id.button_3)).setText("更新");
            ((EditText)findViewById(R.id.editText1)).setText(Integer.toString(card.getLenTime()));
            ((EditText)findViewById(R.id.editText2)).setText(card.getContent());
            ((EditText)findViewById(R.id.editText3)).setText(card.getPlace());
            plan_Day = (int)(card.getCalendar() / 10000);
            plan_Time = (int)(card.getCalendar() % 10000);
        }else {
            //新規作成の場合
            setTitle("日時・行動の追加");
            card = new Card();
        }
        //endregion
    }

    //Pickerによるデータの反映処理
    public void onReturnValue(int data, String text, int picker) {
        if(picker == 1) {
            plan_Day = data;
            Button button = (Button) findViewById(R.id.button_1);
            button.setText(text + "(変更時はタップ)");
        }else if(picker == 2){
            plan_Time = data;
            Button button = (Button) findViewById(R.id.button_2);
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
        if(plan_Day!=-1&&plan_Time!=-1&&
                !(((EditText)findViewById(R.id.editText1)).getText().toString()).equals("")&&
                !(((EditText)findViewById(R.id.editText2)).getText().toString()).equals("")&&
                !(((EditText)findViewById(R.id.editText3)).getText().toString()).equals(""))
            (findViewById(R.id.button_3)).setEnabled(true);
        else
            (findViewById(R.id.button_3)).setEnabled(false);
    }

    //追加する予定の位置を取得
    public int addCheck(Card card){
        int position;
        long start1, start2, end1, end2;
        //全ての予定情報を取得
        ArrayList<Card> cards = ((ScheduleApplication)this.getApplication()).getPlanCard();

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
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}