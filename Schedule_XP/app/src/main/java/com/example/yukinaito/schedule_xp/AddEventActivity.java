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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;

public class AddEventActivity extends AppCompatActivity {
    private boolean editFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //region 前画面に戻るボタンの生成
        final Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.abc_ic_ab_back_material, null);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.input), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        //endregion

        //編集のときはset()メソッドを呼ぶ

        //region 編集かどうか
        EventPlanCard editCard;
        if((editCard = (EventPlanCard) getIntent().getSerializableExtra("EditCard")) != null){
            editFlag = true;
            String date = Integer.toString(editCard.getDate());
            CalendarView calendarView = (CalendarView)findViewById(R.id.input_date);
            calendarView.set(Integer.parseInt(date.substring(0, 4)),
                    Integer.parseInt(date.substring(4, 6)),
                    Integer.parseInt(date.substring(6, 8)));
            ((EditText)findViewById(R.id.input_title)).setText(editCard.getTitle());
            ((Spinner)findViewById(R.id.input_model)).setSelection(editCard.getIndex());
        }
        //endregion
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        if(editFlag)
            inflater.inflate(R.menu.edit_menu, menu);
        else
            inflater.inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //戻るキーを押されたときの処理
            if(editFlag){
                Intent intent = new Intent();
                intent.putExtra("AddEditCard", getIntent().getSerializableExtra("EditCard"));
                intent.putExtra("Index", getIntent().getIntExtra("Index", -1));
                setResult(RESULT_OK, intent);
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    //タイトルバーのオブジェクトが選択されたとき
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //region 前画面に戻るボタンタップ時
            EventPlanCard editCard;
            if((editCard = (EventPlanCard) getIntent().getSerializableExtra("EditCard")) != null){
                Intent intent = new Intent();
                intent.putExtra("AddEditCard", editCard);
                intent.putExtra("Index", getIntent().getIntExtra("Index", -1));
                setResult(RESULT_OK, intent);
            }
            finish();
            //endregion
        }else if (id == R.id.add_action || id == R.id.update_action) {
            //region 追加|更新ボタンタップ時
            //入力チェック
            boolean check = inputCheck();
            int index= addCheck();
            if(!check || index < 0){
                //ダイアログの生成
                String message = "追加できませんでした。以下の項目を確認してください。\n";
                if(!check){
                    message += "\n・イベント名の入力とモデルの選択ができているか";
                }
                if(index < 0){
                    index = (index + 1) * -1;
                    ArrayList<EventPlanCard> cards = ((ScheduleApplication) getApplication()).getEventCards();
                    EventPlanCard overlapCard = cards.get(index);
                    String date = Integer.toString(overlapCard.getDate());

                    message += "\n・次の予定と日付が重なっています。\n　　"
                            + "日付 : " + date.substring(0, 4) + "/" + date.substring(4, 6) + "/" + date.substring(6, 8) + "\n　　"
                            + "イベント名 : " + overlapCard.getTitle();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(message);
                builder.setPositiveButton("OK", null);
                builder.show();
                return super.onOptionsItemSelected(item);
            }
            //追加するオブジェクトを作成
            EventPlanCard addCard = new EventPlanCard(((CalendarView)findViewById(R.id.input_date)).getInfo(),
                    ((EditText)findViewById(R.id.input_title)).getText().toString(),
                    ((Spinner)findViewById(R.id.input_model)).getSelectedItemPosition());
            //intent作成
            Intent intent = new Intent();
            intent.putExtra("AddEditCard", addCard);
            intent.putExtra("Index", index);
            setResult(RESULT_OK, intent);
            finish();
            //endregion
        }
        return super.onOptionsItemSelected(item);
    }

    //入力チェック 未選択|空欄でないかどうか
    public boolean inputCheck(){
        if(((Spinner)findViewById(R.id.input_model)).getSelectedItemPosition() != 0 &&
                ((EditText)findViewById(R.id.input_title)).getText().toString().trim().length() != 0)
            return true;
        return false;
    }

    //追加する予定が他の予定と重なっていないかチェック&追加位置を返す
    public int addCheck(){
        //後で変更 アプリケーションから取得
        ArrayList<EventPlanCard> planCards = ((ScheduleApplication)getApplication()).getEventCards();
        int date = ((CalendarView)findViewById(R.id.input_date)).getInfo();

        //予定が空のとき
        if(planCards.size() == 0)
            return 0;

        //確認処理
        for(int index = 0; index < planCards.size(); index++) {
            int originalDate = planCards.get(index).getDate();
            if (originalDate > date) {
                return index;
            }else if(originalDate == date){
                return -1 * index - 1;
            }
        }
        return planCards.size();
    }
}
