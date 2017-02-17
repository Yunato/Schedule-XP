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


        (findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Card card = new Card(1, 0, 730, true, "就寝", "部屋");
                card.setMemo("null");
                sqLiteApplication.saveCard(card);
            }
        });
        (findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Card card = new Card(101, 0, 730, true, "就寝", "部屋");
                card.setMemo("null");
                sqLiteApplication.saveCard(card);
            }
        });
        (findViewById(R.id.button3)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Card card = new Card(-1, -1, 50, true, "読書", "部屋");
                card.setMemo("null");
                sqLiteApplication.saveCard(card);
            }
        });
        (findViewById(R.id.button4)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Card card = new Card(1231, -3, 1200, true, "アプリ開発", "部屋");
                card.setMemo("null");
                sqLiteApplication.saveCard(card);
            }
        });
        (findViewById(R.id.button5)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Card card = new Card(525, -4, 8, false, "誕生日", "null");
                card.setMemo("null");
                sqLiteApplication.saveCard(card);
            }
        });
        (findViewById(R.id.button6)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sqLiteApplication.Test_getCards(null, null);
            }
        });
        (findViewById(R.id.button7)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), AddModelActivity.class);
                startActivity(intent);
            }
        });
    }
}
