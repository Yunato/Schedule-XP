package com.example.yukinaito.schedule_xp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
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

import java.util.ArrayList;

public class AddEventActivity extends AppCompatActivity {

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

        CalendarView calendarView = (CalendarView)findViewById(R.id.input_date);
        //編集のときはset()メソッドを呼ぶ
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
            EventPlanCard addCard = new EventPlanCard(((CalendarView)findViewById(R.id.input_date)).getInfo(),
                    ((EditText)findViewById(R.id.input_title)).getText().toString(),
                    ((Spinner)findViewById(R.id.input_model)).getSelectedItemPosition());
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
            }
        }
        return planCards.size();
    }
}
