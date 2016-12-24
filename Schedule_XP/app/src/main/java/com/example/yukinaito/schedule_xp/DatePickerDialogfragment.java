package com.example.yukinaito.schedule_xp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerDialogfragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfmonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, dayOfmonth);

        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        int date, buf_year, buf_month, buf_day;

        date = year * 10000 + (month + 1) * 100 + day;
        buf_day = date % 100;
        buf_month = (date / 100) % 100;
        buf_year = date / 10000;
        String text = String.valueOf(buf_year)+"年"+String.valueOf(buf_month)+"月"+String.valueOf(buf_day)+"日";

        AddAtTimePlan callingActivity = (AddAtTimePlan) getActivity();
        callingActivity.onReturnValue(date, text, 1);
    }
}
