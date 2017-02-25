package com.example.yukinaito.schedule_xp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class AddPlanActivity extends AppCompatActivity {
    //データ
    private int date = 0;
    private int startTime = 0;
    private int overTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //region 前画面に戻るボタンの生成
        final Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.abc_ic_ab_back_material, null);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.input), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        //endregion

        //region リスナーの登録
        findViewById(R.id.input_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region 予定の日時を選択 DatePickerの呼び出し
                DatePickerDialogFragment datePickerDialog = new DatePickerDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("Activity", 1);
                datePickerDialog.setArguments(bundle);
                datePickerDialog.show(getSupportFragmentManager(), "datePicker");
                //endregion
            }
        });
        findViewById(R.id.input_startTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region 予定の開始時刻を選択 TimePickerの呼び出し
                TimePickerDialogFragment timePickerDialog = new TimePickerDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("Activity", 1);
                timePickerDialog.setArguments(bundle);
                timePickerDialog.show(getSupportFragmentManager(), "timePicker");
                //endregion
            }
        });
        findViewById(R.id.input_overTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region 予定の制限(終了)時刻を選択 TimePickerの呼び出し
                TimePickerDialogFragment timePickerDialog = new TimePickerDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("Activity", 2);
                timePickerDialog.setArguments(bundle);
                timePickerDialog.show(getSupportFragmentManager(), "timePicker");
                //endregion
            }
        });
        //endregion
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        //if(update_model != null)
        //inflater.inflate(R.menu.edit_menu, menu);
        //else
        inflater.inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Pickerによるデータの反映処理
    public void onReturnValue(int data, String text, int picker) {
        if(picker == 1) {
            date = data;
            Button button = (Button) findViewById(R.id.input_date);
            button.setText(text);
        }else if(picker == 2){
            startTime = data;
            Button button = (Button) findViewById(R.id.input_startTime);
            button.setText(text);
        }else if(picker == 3){
            overTime = data;
            Button button = (Button) findViewById(R.id.input_overTime);
            button.setText(text);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //戻るキーを押されたときの処理
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    //タイトルバーのオブジェクトが選択されたとき
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }else if (id == R.id.add_action) {
            //入力チェック
            int index = addCheck();
            if(!inputCheck() && index > -1){
                //ダイアログの生成(失敗)
                return super.onOptionsItemSelected(item);
            }
            //追加するオブジェクトを作成
            Card addCard = new Card(date, startTime, overTime, false,
                    ((EditText)findViewById(R.id.input_content)).getText().toString(),
                    ((EditText)findViewById(R.id.input_place)).getText().toString());
            //intent作成
            Intent intent = new Intent();
            intent.putExtra("AddCard", addCard);
            intent.putExtra("Index", index);
            setResult(RESULT_OK, intent);
            finish();
        }else if (id == R.id.update_action) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //入力チェック 未選択|空欄でないかどうか
    public boolean inputCheck(){
        if(!((Button)findViewById(R.id.input_date)).getText().toString().equals("日付の指定[タップ]") &&
                !((Button)findViewById(R.id.input_startTime)).getText().toString().equals("時刻の指定[タップ]") &&
                !((Button)findViewById(R.id.input_overTime)).getText().toString().equals("時刻の指定[タップ]") &&
                ((EditText)findViewById(R.id.input_content)).getText().toString().trim().length() != 0 &&
                ((EditText)findViewById(R.id.input_place)).getText().toString().trim().length() != 0)
            return true;
        return false;
    }

    //追加する予定が他の予定と重なっていないかチェック&追加位置を返す
    public int addCheck(){
        //後で変更 アプリケーションから取得
        ArrayList<Card> planCards = ((ScheduleApplication)getApplication()).getPlanCards();

        //予定が空のとき
        if(planCards.size() == 0)
            return 0;

        //確認処理
        for(int index = 0; index < planCards.size(); index++) {
            int originalDate = planCards.get(index).getDate();
            if (originalDate > date) {
                return index;
            } else if (originalDate == date) {
                //同日付の予定の重なり|前後を調べる
                int originalStart = planCards.get(index).getStartTime();
                int originalOver = planCards.get(index).getOverTime();
                if(originalStart >= startTime){
                    if(originalStart < overTime) {
                        return -1 * index - 1;
                    }else{
                        if(planCards.size() > index + 1 && originalStart == startTime && originalStart == overTime){
                            return indexDownCheck(planCards, index + 1);
                        }
                        return index;
                    }
                }else if(startTime < originalOver){
                    return -1 * index - 1;
                }
            }
        }
        return planCards.size();
    }

    public int indexDownCheck(ArrayList<Card> planCards, int index){
        int originalStart = planCards.get(index).getStartTime();
        int originalOver = planCards.get(index).getOverTime();
        if(originalStart < overTime && startTime < originalOver){
            return -1 * (index - 1) - 1;
        }else{
            if(planCards.size() > index + 1 && originalStart == startTime && originalStart == overTime){
                return indexDownCheck(planCards, index + 1);
            }else{
                return index;
            }
        }
    }
}
