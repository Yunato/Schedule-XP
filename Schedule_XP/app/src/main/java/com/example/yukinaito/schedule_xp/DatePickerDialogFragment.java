package com.example.yukinaito.schedule_xp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private int check_Activity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        check_Activity = getArguments().getInt("Activity");

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, dayOfMonth);

        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        int date;

        date = year * 10000 + (month + 1) * 100 + day;
        String text = Integer.toString(year) + "年" +
                        Integer.toString(month + 1) + "月" +
                        Integer.toString(day) + "日";

        if(check_Activity == 1){
            AddPlanActivity callingActivity = (AddPlanActivity)getActivity();
            callingActivity.onReturnValue(date, text, 1);
        }else if(check_Activity == 2){
            AddMustActivity callingActivity = (AddMustActivity)getActivity();
            callingActivity.onReturnValue(date, text, 1);
        }
    }
}
