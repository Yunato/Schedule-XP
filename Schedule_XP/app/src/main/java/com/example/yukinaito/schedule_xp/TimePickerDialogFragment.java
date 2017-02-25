package com.example.yukinaito.schedule_xp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private int check_Activity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        check_Activity = getArguments().getInt("Activity");

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hourOfDay, minute, true);

        return timePickerDialog;
        }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        int time;
        String text = "";

        time = hourOfDay * 100 + minute;

        if (hourOfDay < 10)
            text += "0";
        text += Integer.toString(hourOfDay) + "時";
        if (minute < 10)
            text += "0";
        text += Integer.toString(minute) + "分";

        if(check_Activity == 1){
            AddPlanActivity callingActivity = (AddPlanActivity)getActivity();
            callingActivity.onReturnValue(time, text, 2);
        }else if(check_Activity == 2){
            AddPlanActivity callingActivity = (AddPlanActivity)getActivity();
            callingActivity.onReturnValue(time, text, 3);
        }else if(check_Activity == 3){
            AddMustActivity callingActivity = (AddMustActivity)getActivity();
            callingActivity.onReturnValue(time, text, 2);
        }else if(check_Activity == 4){
            AddModelActivity callingActivity = (AddModelActivity)getActivity();
            callingActivity.onReturnValue(text);
        }
    }
}
