package com.example.yukinaito.schedule_xp;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class SettingModelActivity extends AppCompatActivity {
    private boolean visible = false;
    private int index;
    private ScheduleApplication scheduleApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listfragment);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("ひな形(モデル)一覧");
        //region 前画面に戻るボタンの生成
        final Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.abc_ic_ab_back_material, null);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorsimbol), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        //endregion

        index = (int)getIntent().getSerializableExtra("Position");
        if(index > 6)
            visible = true;

        scheduleApplication = (ScheduleApplication)this.getApplication();
        SettingMainFragment fragment = new SettingMainFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //データを渡す
        Bundle bundle = new Bundle();
        bundle.putSerializable("Position", index);
        fragment.setArguments(bundle);
        transaction.add(R.id.activity_listfragment, fragment);
        transaction.commit();
        //画面遷移
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if(visible)
            getMenuInflater().inflate(R.menu.modeldelete,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.setting_delete){
            //イベント日の指定で本処理で消す固定スケジュールをもつものがあるか
            boolean check = false;
            for(int i = 0; i < scheduleApplication.getEventplancards().size(); i++){
                if(scheduleApplication.getEventplancards().get(i).getIndex() == index)
                    check = true;
            }
            if(check){
                //イベント日に指定されているため、固定スケジュールを別の場所へ移す
                scheduleApplication.getEventmodel().add(scheduleApplication.getModelSchedule().get(index));
                for(int i = 0; i < scheduleApplication.getEventplancards().size(); i++) {
                    if (scheduleApplication.getEventplancards().get(i).getIndex() == index)
                        scheduleApplication.getEventplancards().get(i).setIndex(scheduleApplication.getModelSchedule().size() + scheduleApplication.getEventmodel().size() - 1);
                }
            }
            for(int i = 0; i < scheduleApplication.getEventplancards().size(); i++) {
                if(scheduleApplication.getEventplancards().get(i).getIndex() > index) {
                    int buf = scheduleApplication.getEventplancards().get(i).getIndex();
                    scheduleApplication.getEventplancards().get(i).setIndex(buf - 1);
                }
            }
            scheduleApplication.getModelSchedule().remove(index);
            scheduleApplication.writeEventPlanFile();
            setResult(RESULT_OK);
            finish();
        }else if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
