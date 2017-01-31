package com.example.yukinaito.schedule_xp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private int activity_check;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfmonth = calendar.get(Calendar.DAY_OF_MONTH);
        activity_check = getArguments().getInt("activity");

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

        if (activity_check == 1) {
            AddPlanActivity callingActivity = (AddPlanActivity) getActivity();
            callingActivity.onReturnValue(date, text, 1);
        }else if(activity_check == 2) {
            AddMustPlanActivity callingActivity = (AddMustPlanActivity) getActivity();
            callingActivity.onReturnValue(date, text, 1);
        }else if(activity_check == 3) {
            AddMustPlanActivity callingActivity = (AddMustPlanActivity) getActivity();
            callingActivity.onReturnValue(date, text, 3);
        }else if(activity_check == 4) {
            AddEventPlanActivity callingActivity = (AddEventPlanActivity) getActivity();
            callingActivity.onReturnValue(date, text, 1);
        }{
            return;
        }
    }
}
