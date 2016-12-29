package com.example.yukinaito.schedule_xp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerDialogfragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private int activity_check;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        activity_check = getArguments().getInt("activity");

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hourOfDay, minute, true);

        return timePickerDialog;
        }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        int time, buf_hour, buf_minute;
        String text = new String();

        time = hourOfDay * 100 + minute;
        buf_minute = time % 100;
        buf_hour = time / 100;

        if (buf_hour < 10)
            text += "0";
        text += String.valueOf(buf_hour) + "時";
        if (buf_minute < 10)
            text += "0";
        text += String.valueOf(buf_minute) + "分";

        if (activity_check == 1) {
            AddPlanActivity callingActivity = (AddPlanActivity) getActivity();
            callingActivity.onReturnValue(time, text, 2);
        }else if(activity_check == 2) {
            AddModelActivity callingActivity = (AddModelActivity) getActivity();
            callingActivity.onReturnValue(time, text, 2);
        }else if(activity_check == 3) {
            AddHavetoPlanActivity callingActivity = (AddHavetoPlanActivity) getActivity();
            callingActivity.onReturnValue(time, text, 2);
        }else if(activity_check == 4) {
            AddHavetoPlanActivity callingActivity = (AddHavetoPlanActivity) getActivity();
            callingActivity.onReturnValue(time, text, 4);
        }else{
            return;
        }
    }
}
