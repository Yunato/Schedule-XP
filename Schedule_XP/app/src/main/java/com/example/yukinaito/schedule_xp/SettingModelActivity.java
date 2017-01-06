package com.example.yukinaito.schedule_xp;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class SettingModelActivity extends AppCompatActivity {
    private boolean visible = false;
    private int arraypos;
    private SchedlueApplication schedlueApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listfragment);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("ひな形(モデル)一覧");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.colorsimbol), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        if((int)getIntent().getSerializableExtra("position") > 6)
            visible = true;

        schedlueApplication = (SchedlueApplication)this.getApplication();
        SettingMainFragment fragment = new SettingMainFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        arraypos = (int)getIntent().getSerializableExtra("position");
        bundle.putSerializable("position", (int)getIntent().getSerializableExtra("position"));
        fragment.setArguments(bundle);
        transaction.add(R.id.activity_listfragment, fragment);
        transaction.commit();
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
            boolean check = false;
            for(int i = 0; i < schedlueApplication.getEventplancards().size(); i++){
                if(schedlueApplication.getEventplancards().get(i).getIndex() == arraypos)
                    check = true;
            }
            if(check){
                ModelSchedule modelSchedule = new ModelSchedule();
                modelSchedule.setName(schedlueApplication.getModelSchedule().get(this.arraypos).getName());
                for(int i = 0; i < schedlueApplication.getModelSchedule().get(this.arraypos).getCards().size(); i++){
                    Card card = new Card();
                    card.setInfo(schedlueApplication.getModelSchedule().get(this.arraypos).getCards().get(i).getCalendar(),
                            schedlueApplication.getModelSchedule().get(this.arraypos).getCards().get(i).getLentime(),
                            schedlueApplication.getModelSchedule().get(this.arraypos).getCards().get(i).getContent(),
                            schedlueApplication.getModelSchedule().get(this.arraypos).getCards().get(i).getPlace());
                    modelSchedule.getCards().add(card);
                }
                schedlueApplication.getEventmodel().add(modelSchedule);
                for(int i = 0; i < schedlueApplication.getEventplancards().size(); i++)
                    schedlueApplication.getEventplancards().get(i).setIndex(schedlueApplication.getModelSchedule().size() + schedlueApplication.getEventmodel().size() - 1);
            }
            for(int i = 0; i < schedlueApplication.getEventplancards().size(); i++) {
                if(schedlueApplication.getEventplancards().get(i).getIndex() > (schedlueApplication.getModelSchedule().size() - 1)) {
                    int buf = schedlueApplication.getEventplancards().get(i).getIndex();
                    schedlueApplication.getEventplancards().get(i).setIndex(buf - 1);
                }
            }
            schedlueApplication.getModelSchedule().remove((int)getIntent().getSerializableExtra("position"));
            schedlueApplication.writeEventPlanFile();
            setResult(RESULT_OK);
            finish();
        }else if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
