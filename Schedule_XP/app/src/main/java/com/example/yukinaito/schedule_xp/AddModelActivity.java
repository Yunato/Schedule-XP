package com.example.yukinaito.schedule_xp;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AddModelActivity extends AppCompatActivity {
    //描画するActivityの土台となるlayout
    private LinearLayout layout;
    private Button addButton;
    //生成したモデルの個数 削除でデクリメントしない
    private int planCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_model);

        //レイアウトの生成
        layout = (LinearLayout)findViewById(R.id.activity_add_model);
        addButton = (Button)findViewById(R.id.AddButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createObject();
            }
        });
        createObject();
    }

    //初回処理・最下にあるボタンがタップされたときの処理 入力欄の追加 / count 追加する回数
    public void createObject(){
        //region 1つの固まった入力欄の生成
        LinearLayout blockLayout = new LinearLayout(this);
        blockLayout.setTag(Integer.toString(planCount));
        blockLayout.setOrientation(LinearLayout.VERTICAL);

        //region 削除バーの生成
        if(planCount!=0) {
            LinearLayout optionBar = new LinearLayout(this);
            optionBar.setOrientation(LinearLayout.VERTICAL);
            optionBar.setBackgroundColor(Color.parseColor("#A5D6AC"));
            optionBar.setGravity(Gravity.RIGHT);
            optionBar.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            final TextView deleteView = new TextView(this);
            deleteView.setText("消去");
            deleteView.setTextSize(18.0f);
            deleteView.setPadding(10, 10, 10, 10);
            deleteView.setTextColor(Color.WHITE);
            deleteView.setId(planCount);

            deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout deleteLayout = (LinearLayout)layout.findViewWithTag(Integer.toString(view.getId()));
                    layout.removeView(deleteLayout);
                }
            });

            optionBar.addView(deleteView, new LinearLayoutCompat.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            blockLayout.addView(optionBar);
        }
        //endregion

        LinearLayout tableLayout = new LinearLayout(this);
        tableLayout.setOrientation(LinearLayout.HORIZONTAL);
        tableLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        //region 左列の項目名の生成
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.VERTICAL);
        itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        int id = this.getResources().getIdentifier("enclosure_item", "drawable", this.getPackageName());
        Drawable back = ResourcesCompat.getDrawable(getResources(), id, null);
        TextView textView1 = new TextView(this);
        textView1.setText("開始時刻");
        textView1.setTextSize(22.0f);
        textView1.setGravity(Gravity.CENTER);
        textView1.setPadding(15, 30, 15, 30);
        textView1.setTextColor(Color.WHITE);
        textView1.setBackground(back);
        TextView textView2 = new TextView(this);
        textView2.setText("制限(終了)時刻");
        textView2.setTextSize(22.0f);
        textView2.setGravity(Gravity.CENTER);
        textView2.setPadding(15, 30, 15, 30);
        textView2.setTextColor(Color.WHITE);
        textView2.setBackground(back);
        TextView textView3 = new TextView(this);
        textView3.setText("内容");
        textView3.setTextSize(22.0f);
        textView3.setGravity(Gravity.CENTER);
        textView3.setPadding(15, 30, 15, 30);
        textView3.setTextColor(Color.WHITE);
        textView3.setBackground(back);
        TextView textView4 = new TextView(this);
        textView4.setText("場所");
        textView4.setTextSize(22.0f);
        textView4.setGravity(Gravity.CENTER);
        textView4.setPadding(15, 30, 15, 30);
        textView4.setTextColor(Color.WHITE);
        textView4.setBackground(back);

        itemLayout.addView(textView1, new LinearLayoutCompat.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        itemLayout.addView(textView2, new LinearLayoutCompat.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        itemLayout.addView(textView3, new LinearLayoutCompat.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        itemLayout.addView(textView4, new LinearLayoutCompat.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        tableLayout.addView(itemLayout);
        //endregion

        //region 右列の項目名の生成
        LinearLayout inputLayout = new LinearLayout(this);
        inputLayout.setOrientation(LinearLayout.VERTICAL);
        inputLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        id = this.getResources().getIdentifier("enclosure_input", "drawable", this.getPackageName());
        back = ResourcesCompat.getDrawable(getResources(), id, null);
        Button button1 = new Button(this);
        button1.setText("時刻の指定[タップ]");
        button1.setTextSize(22.0f);
        button1.setGravity(Gravity.CENTER);
        button1.setBackground(back);
        Button button2 = new Button(this);
        button2.setText("時刻の指定[タップ]");
        button2.setTextSize(22.0f);
        button2.setGravity(Gravity.CENTER);
        button2.setBackground(back);
        EditText editText1 = new EditText(this);
        editText1.setTextSize(22.0f);
        editText1.setInputType(InputType.TYPE_CLASS_TEXT);
        editText1.setPadding(15, 30, 15, 30);
        editText1.setBackground(back);
        EditText editText2 = new EditText(this);
        editText2.setTextSize(22.0f);
        editText2.setInputType(InputType.TYPE_CLASS_TEXT);
        editText2.setPadding(15, 30, 15, 30);
        editText2.setBackground(back);

        inputLayout.addView(button1, new LinearLayoutCompat.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        inputLayout.addView(button2, new LinearLayoutCompat.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        inputLayout.addView(editText1, new LinearLayoutCompat.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        inputLayout.addView(editText2, new LinearLayoutCompat.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        tableLayout.addView(inputLayout);
        //endregion
        //endregion
        blockLayout.addView(tableLayout);
        layout.addView(blockLayout);
        //連結する予定を増やすボタンを最下に置く
        layout.removeView(addButton);
        layout.addView(addButton);
        planCount++;
    }
}
