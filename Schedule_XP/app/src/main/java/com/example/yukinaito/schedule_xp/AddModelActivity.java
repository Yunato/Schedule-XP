package com.example.yukinaito.schedule_xp;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.sql.BatchUpdateException;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;

public class AddModelActivity extends AppCompatActivity {
    //描画するActivityの土台となるlayout
    private LinearLayout layout;
    private Button addButton;
    //生成したモデルの個数 削除でデクリメントしない
    private int planCount = 0;
    //ダイアログを表示させるボタンのタグを保持
    private String Tag;
    //データ
    private int startTime;
    private int overTime;
    private boolean editFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_model);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //region 前画面に戻るボタンの生成
        final Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.abc_ic_ab_back_material, null);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.input), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        //endregion

        Log.d("TEST ac", "OK");
        int count = 1;
        //region 編集かどうか
        ArrayList<Card> editCards;
        if((editCards = (ArrayList<Card>) getIntent().getSerializableExtra("EditCard")) != null){
            editFlag = true;
            count = editCards.size();
            Log.d("TEST", count + " ");
        }
        //endregion

        //レイアウトの生成
        layout = (LinearLayout) findViewById(R.id.activity_add_model);
        addButton = (Button) findViewById(R.id.AddButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createObject(1, null);
            }
        });
        createObject(count, editCards);
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

    //初回処理・最下にあるボタンがタップされたときの処理 入力欄の追加 / count 追加する回数
    public void createObject(int count, ArrayList<Card> editCards) {
        String viewTag1, viewTag2, viewTag3, viewTag4;
        for(int i = 0; i < count; i++, planCount++){
            //region 1つの固まった入力欄の生成
            LinearLayout blockLayout = new LinearLayout(this);
            blockLayout.setTag(Integer.toString(planCount));
            blockLayout.setOrientation(LinearLayout.VERTICAL);
            blockLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            //region 削除バーの生成
            if (planCount != 0) {
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
                        LinearLayout deleteLayout = (LinearLayout) layout.findViewWithTag(Integer.toString(view.getId()));
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

            viewTag1 = "startTime" + planCount;
            viewTag2 = "overTime" + planCount;
            viewTag3 = "content" + planCount;
            viewTag4 = "place" + planCount;

            id = this.getResources().getIdentifier("enclosure_input", "drawable", this.getPackageName());
            back = ResourcesCompat.getDrawable(getResources(), id, null);
            Button button1 = new Button(this);
            button1.setTag(viewTag1);
            button1.setTextSize(22.0f);
            button1.setGravity(Gravity.CENTER);
            button1.setBackground(back);
            Button button2 = new Button(this);
            button2.setTag(viewTag2);
            button2.setTextSize(22.0f);
            button2.setGravity(Gravity.CENTER);
            button2.setBackground(back);
            EditText editText1 = new EditText(this);
            editText1.setTag(viewTag3);
            editText1.setTextSize(22.0f);
            editText1.setInputType(InputType.TYPE_CLASS_TEXT);
            editText1.setPadding(15, 30, 15, 30);
            editText1.setBackground(back);
            EditText editText2 = new EditText(this);
            editText2.setTag(viewTag4);
            editText2.setTextSize(22.0f);
            editText2.setInputType(InputType.TYPE_CLASS_TEXT);
            editText2.setPadding(15, 30, 15, 30);
            editText2.setBackground(back);

            //region リスナーの登録
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //region 予定の開始時刻を選択 TimePickerの呼び出し
                    Tag = (String)view.getTag();
                    TimePickerDialogFragment timePickerDialog = new TimePickerDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("Activity", 4);
                    timePickerDialog.setArguments(bundle);
                    timePickerDialog.show(getSupportFragmentManager(), "timePicker");
                    //endregion
                }
            });
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //region 予定の制限(終了)時刻を選択 TimePickerの呼び出し
                    Tag = (String)view.getTag();
                    TimePickerDialogFragment timePickerDialog = new TimePickerDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("Activity", 4);
                    timePickerDialog.setArguments(bundle);
                    timePickerDialog.show(getSupportFragmentManager(), "timePicker");
                    //endregion
                }
            });
            //endregion

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

            //編集のとき値を格納する
            if(editCards != null){
                Format format = new DecimalFormat("00");
                startTime = editCards.get(i).getStartTime();
                overTime = editCards.get(i).getOverTime();
                button1.setText(format.format(startTime / 100) + "時"
                        + format.format(startTime % 100) + "分");
                editText1.setText(editCards.get(i).getContent());
                editText2.setText(editCards.get(i).getPlace());
                if(overTime != -1){
                    button2.setText(format.format(overTime / 100) + "時"
                            + format.format(overTime % 100) + "分");
                }else{
                    button2.setText("時刻の指定[タップ]");
                }
            }else{
                button1.setText("時刻の指定[タップ]");
                button2.setText("時刻の指定[タップ]");
            }

            //endregion
            //endregion
            blockLayout.addView(tableLayout);
            layout.addView(blockLayout);
            //連結する予定を増やすボタンを最下に置く
            layout.removeView(addButton);
            layout.addView(addButton);
        }
    }

    //Pickerによるデータの反映処理
    public void onReturnValue(String text) {
        Button button = (Button)layout.findViewWithTag(Tag);
        button.setText(text);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //戻るキーを押されたときの処理
            if(editFlag){
                Intent intent = new Intent();
                intent.putExtra("AddEditCards", getIntent().getSerializableExtra("EditCard"));
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
            ArrayList<Card> editCards;
            if((editCards = (ArrayList<Card>) getIntent().getSerializableExtra("EditCard")) != null){
                Intent intent = new Intent();
                intent.putExtra("AddEditCards", editCards);
                intent.putExtra("Index", getIntent().getIntExtra("Index", -1));
                setResult(RESULT_OK, intent);
            }
            finish();
            //endregion
        }else if (id == R.id.add_action || id == R.id.update_action) {
            //入力チェック
            boolean check = inputCheck();
            int index= addCheck();
            if(!check || index < 0){
                //ダイアログの生成
                String message = "追加できませんでした。以下の項目を確認してください。\n";
                if(!check){
                    message += "\n・表示している全ての予定の入力欄の開始時刻、内容、場所が入力されているか" +
                              "\n・最下位の予定に制限(終了)時刻が入力されているか";
                }
                if(index < 0){
                    index = (index + 1) * -1;
                    ArrayList<Card> cards = ((ScheduleApplication) getApplication()).getModelCards();
                    Card overlapCard = cards.get(index);
                    Format format = new DecimalFormat("00");
                    int start = overlapCard.getStartTime();
                    int finish;
                    if(overlapCard.getConnect()){
                        if(cards.size() == (index + 1)){
                            finish = 2400;
                        }else{
                            finish = cards.get(index + 1).getStartTime();
                        }
                    }
                    else{
                        finish = overlapCard.getOverTime();
                    }
                    start = (start / 100) * 60 + (start % 100);
                    finish = (finish / 100) * 60 + (finish % 100);

                    message += "\n・次の予定と時間が重なっています。\n　　"
                                + "時刻 : " + format.format(start / 60) + ":" + format.format(start % 60) +
                                    " - " + format.format(finish / 60) + ":" + format.format(finish % 60) + "\n　　"
                                + "内容 : " + overlapCard.getContent() + "\n　　"
                                + "場所 : " + overlapCard.getPlace();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(message);
                builder.setPositiveButton("OK", null);
                builder.show();
                return super.onOptionsItemSelected(item);
            }
            //追加するオブジェクトを作成
            ArrayList<Card> addCards = createAddCards();
            //intent作成
            Intent intent = new Intent();
            intent.putExtra("AddEditCards", addCards);
            intent.putExtra("Index", index);
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //入力チェック 未選択|空欄でないかどうか
    public boolean inputCheck() {
        int index;
        //一番下のブロックを見つける＆入力チェック
        for (index = planCount - 1; index >= 0; index--) {
            if (layout.findViewWithTag("startTime" + index) != null) {
                if (((Button) layout.findViewWithTag("startTime" + index)).getText().toString().equals("時刻の指定[タップ]") ||
                        ((Button) layout.findViewWithTag("overTime" + index)).getText().toString().equals("時刻の指定[タップ]") ||
                        ((EditText) layout.findViewWithTag("content" + index)).getText().toString().trim().length() == 0 ||
                        ((EditText) layout.findViewWithTag("place" + index)).getText().toString().trim().length() == 0) {
                    return false;
                } else {
                    this.overTime = Integer.parseInt(((Button)layout.findViewWithTag("overTime" + index)).getText().toString().substring(0, 2)) * 100
                            + Integer.parseInt(((Button)layout.findViewWithTag("overTime" + index)).getText().toString().substring(3, 5));
                    break;
                }
            }
        }
        //一番下のブロック以外のブロックの入力チェック
        for (; index >= 0; index--) {
            if (layout.findViewWithTag("startTime" + index) != null) {
                if (((Button) layout.findViewWithTag("startTime" + index)).getText().toString().equals("時刻の指定[タップ]") ||
                        ((EditText) layout.findViewWithTag("content" + index)).getText().toString().trim().length() == 0 ||
                        ((EditText) layout.findViewWithTag("place" + index)).getText().toString().trim().length() == 0)
                    return false;
            }
        }
        this.startTime = Integer.parseInt(((Button)layout.findViewWithTag("startTime0")).getText().toString().substring(0, 2)) * 100
                + Integer.parseInt(((Button)layout.findViewWithTag("startTime0")).getText().toString().substring(3, 5));
        return true;
    }

    //追加する予定が他の予定と重なっていないかチェック&追加位置を返す
    public int addCheck(){
        //後で変更 アプリケーションから取得
        ArrayList<Card> planCards = ((ScheduleApplication)getApplication()).getModelCards();

        //予定が空のとき
        if(planCards.size() == 0)
            return 0;

        //確認処理
        for(int index = 0; index < planCards.size(); index++) {
            int originalStart = planCards.get(index).getStartTime();
            int originalOver = planCards.get(index).getOverTime();
            if(originalStart >= startTime){
                Log.d("TEST", originalStart + " " + overTime);
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

    public ArrayList<Card> createAddCards(){
        ArrayList<Card> addCards = new ArrayList<>();
        int startTime, overTime = -1;
        String string = "\"";

        for(int index = 0; index < planCount; index++){
            if (layout.findViewWithTag("startTime" + index) != null) {
                startTime = Integer.parseInt(((Button)layout.findViewWithTag("startTime" + index)).getText().toString().substring(0, 2)) * 100
                            + Integer.parseInt(((Button)layout.findViewWithTag("startTime" + index)).getText().toString().substring(3, 5));
                if(!((Button) layout.findViewWithTag("overTime" + index)).getText().toString().equals("時刻の指定[タップ]"))
                    overTime = Integer.parseInt(((Button)layout.findViewWithTag("overTime" + index)).getText().toString().substring(0, 2)) * 100
                            + Integer.parseInt(((Button)layout.findViewWithTag("overTime" + index)).getText().toString().substring(3, 5));
                Card addCard = new Card(-1, startTime, overTime, true,
                        ((EditText)layout.findViewWithTag("content" + index)).getText().toString(),
                        ((EditText)layout.findViewWithTag("place" + index)).getText().toString());
                addCards.add(addCard);
            }
        }
        addCards.get(addCards.size()-1).setConnect(false);
        return addCards;
    }
}
