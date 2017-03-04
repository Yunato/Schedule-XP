package com.example.yukinaito.schedule_xp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class TestActivity extends AppCompatActivity {
    private ScheduleApplication sqLiteApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        sqLiteApplication = (ScheduleApplication)this.getApplication();

        (findViewById(R.id.button6)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sqLiteApplication.Test_getCards(null, null);
            }
        });
    }
}
