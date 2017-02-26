package com.example.yukinaito.schedule_xp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

// http://htomiyama.blogspot.jp/2013/02/androidcalendarview.html を参考
public class CalendarView extends LinearLayout {
    private static final int WEEKDAYS = 7;
    private static final int MAX_WEEK = 6;

    private static final int BIGINNING_DAY_OF_WEEK = Calendar.SUNDAY;
    private static final int TODAY_COLOR = Color.RED;
    private static final int DEFAULT_COLOR = Color.DKGRAY;
    private static final int DEFAULT_BACKGROUND_COLOR = Color.TRANSPARENT;

    private TextView TitleView;

    private LinearLayout WeekLayout;
    private ArrayList<LinearLayout> Weeks = new ArrayList<>();

    //タップされているTextViewとその日のデータ
    private TextView TapTextView;
    private int TapYear = 0;
    private int TapMonth = 0;
    private int TapDay = 0;

    //データ
    private int drawYear;
    private int drawMonth;

    public CalendarView(Context context){
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        this.setOrientation(VERTICAL);

        createTitle(context);
        createWeekViews(context);
        createDayViews(context);

        //現在時刻を取得
        Calendar now = Calendar.getInstance();
        drawYear = now.get(Calendar.YEAR);
        drawMonth = now.get(Calendar.MONTH);
        set(drawYear, drawMonth);
    }

    public void createTitle(Context context){
        float scaleDensity = context.getResources().getDisplayMetrics().density;

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(HORIZONTAL);

        TitleView = new TextView(context);
        TitleView.setGravity(Gravity.CENTER);
        TitleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
        //TitleView.setTextSize((int)(scaleDensity * 5));
        TitleView.setTypeface(null, Typeface.BOLD);
        TitleView.setPadding(0, (int)(scaleDensity * 16), 0, (int)(scaleDensity * 16));

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        Button backButton = new Button(context);
        backButton.setGravity(Gravity.CENTER);
        backButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
        backButton.setTypeface(null, Typeface.BOLD);
        backButton.setText("<");
        backButton.setPadding((int)(scaleDensity * 16), (int)(scaleDensity * 16), (int)(scaleDensity * 16), (int)(scaleDensity * 16));
        backButton.setBackgroundColor(Color.TRANSPARENT);
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                drawMonth--;
                if(drawMonth < 0){
                    drawYear--;
                    drawMonth = 11;
                }
                set(drawYear, drawMonth);
            }
        });

        Button nextButton = new Button(context);
        nextButton.setGravity(Gravity.CENTER);
        nextButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
        nextButton.setTypeface(null, Typeface.BOLD);
        nextButton.setText(">");
        nextButton.setPadding((int)(scaleDensity * 16), (int)(scaleDensity * 16), (int)(scaleDensity * 16), (int)(scaleDensity * 16));
        nextButton.setBackgroundColor(Color.TRANSPARENT);
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                drawMonth++;
                if(drawMonth > 11){
                    drawYear++;
                    drawMonth = 0;
                }
                set(drawYear, drawMonth);
            }
        });

        llp.weight = 1;
        layout.addView(backButton, llp);
        layout.addView(TitleView, llp);
        layout.addView(nextButton, llp);

        addView(layout, new LinearLayoutCompat.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    public void createWeekViews(Context context){
        WeekLayout = new LinearLayout(context);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, BIGINNING_DAY_OF_WEEK);

        for(int i = 0; i < WEEKDAYS; i++){
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            llp.weight = 1;

            WeekLayout.addView(textView, llp);

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        addView(WeekLayout, new LinearLayoutCompat.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    public void createDayViews(Context context){
        for(int i = 0; i < MAX_WEEK; i++){
            LinearLayout weekLine = new LinearLayout(context);
            Weeks.add(weekLine);

            for(int j = 0; j < WEEKDAYS; j++){
                LinearLayout layout = new LinearLayout(context);
                layout.setGravity(Gravity.CENTER);
                TextView dayView = new TextView(context);
                dayView.setPadding(0, 0, 0, 0);
                dayView.setGravity(Gravity.CENTER);

                dayView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(TapTextView != null){
                            TapTextView.setTextColor(DEFAULT_COLOR);
                            TapTextView.setTypeface(null, Typeface.NORMAL);
                        }
                        TapTextView = (TextView) view;
                        TapTextView.setTextColor(TODAY_COLOR);
                        TapTextView.setTypeface(null, Typeface.BOLD);
                        TapYear = drawYear;
                        TapMonth = drawMonth;
                        TapDay = Integer.parseInt(TapTextView.getText().toString());
                    }
                });

                //dayView.setBackgroundResource(R.drawable.circle_view);
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                        50, ViewGroup.LayoutParams.MATCH_PARENT);
                llp.weight = 1;
                layout.addView(dayView, llp);
                weekLine.addView(layout, llp);
            }

            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            llp.weight = 1;
            this.addView(weekLine, llp);
        }
    }

    public void set(int year, int month, int day){
        TapYear = year;
        TapMonth = month;
        TapDay = day;
        set(year, month);
    }

    public void set(int year, int month){
        setTitle(year, month);
        setWeeks();
        setDays(year, month);
    }

    public int getInfo(){
        return TapYear * 10000 + (TapMonth + 1) * 100 + TapDay;
    }

    private void setTitle(int year, int month){
        Calendar targetCalendar = getTargetCalendar(year, month);

        String formatString = TitleView.getContext().getString(R.string.format_month_year);
        SimpleDateFormat formatter = new SimpleDateFormat(formatString);

        TitleView.setText(formatter.format(targetCalendar.getTime()));
    }

    private void setWeeks(){
        Calendar week = Calendar.getInstance();
        week.set(Calendar.DAY_OF_WEEK, BIGINNING_DAY_OF_WEEK);
        SimpleDateFormat weekFormatter = new SimpleDateFormat("E");
        for(int i = 0; i < WEEKDAYS; i++){
            TextView textView = (TextView)WeekLayout.getChildAt(i);
            textView.setText(weekFormatter.format(week.getTime()));
            week.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private void setDays(int year, int month){
        Calendar targetCalendar = getTargetCalendar(year, month);

        int skipCount = getSkipCount(targetCalendar);
        int lastDay = targetCalendar.getActualMaximum(Calendar.DATE);
        int dayCounter = 1;

        Calendar todayCalendar = Calendar.getInstance();
        int todayYear = todayCalendar.get(Calendar.YEAR);
        int todayMonth = todayCalendar.get(Calendar.MONTH);
        int todayDay = todayCalendar.get(Calendar.DAY_OF_MONTH);

        for(int i = 0; i < MAX_WEEK; i++){
            LinearLayout weekLayout = Weeks.get(i);
            weekLayout.setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
            for(int j = 0; j < WEEKDAYS; j++){
                TextView dayView = (TextView)((LinearLayout)weekLayout.getChildAt(j)).getChildAt(0);

                if(i == 0 && skipCount > 0){
                    dayView.setText(" ");
                    skipCount--;
                    continue;
                }
                if(lastDay < dayCounter){
                    dayView.setText(" ");
                    continue;
                }

                dayView.setText(String.valueOf(dayCounter));

                boolean isToday = todayYear == year &&
                                  todayMonth == month &&
                                  todayDay == dayCounter;

                if(TapYear != 0 && TapMonth != 0 && TapDay != 0){
                    isToday = TapYear == year &&
                              TapMonth == month &&
                              TapDay == dayCounter;
                }

                if(isToday) {
                    dayView.setTextColor(TODAY_COLOR);
                    dayView.setTypeface(null, Typeface.BOLD);
                    TapTextView = dayView;
                    TapYear = year;
                    TapMonth = month;
                    TapDay = dayCounter;
                }else{
                    dayView.setTextColor(DEFAULT_COLOR);
                    dayView.setTypeface(null, Typeface.NORMAL);
                }
                dayCounter++;
            }
        }
    }

    private int getSkipCount(Calendar targetCalendar){
        int skipCount;
        int firstDayOfWeekOfMonth = targetCalendar.get(Calendar.DAY_OF_WEEK);
        if(BIGINNING_DAY_OF_WEEK > firstDayOfWeekOfMonth){
            skipCount = firstDayOfWeekOfMonth - BIGINNING_DAY_OF_WEEK + WEEKDAYS;
        }else{
            skipCount = firstDayOfWeekOfMonth - BIGINNING_DAY_OF_WEEK;
        }
        return skipCount;
    }

    private Calendar getTargetCalendar(int year, int month){
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.clear();
        targetCalendar.set(Calendar.YEAR, year);
        targetCalendar.set(Calendar.MONTH, month);
        targetCalendar.set(Calendar.DAY_OF_MONTH, 1);
        return targetCalendar;
    }
}
