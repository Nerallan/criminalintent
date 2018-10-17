package com.nerallan.android.criminalintent.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.nerallan.android.criminalintent.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nerallan on 10/17/2018.
 */

public class TimePickerFragment extends DialogFragment {

    private static final String ARG_TIME = "time";
    private TimePicker mTimePicker;

    public static final String EXTRA_TIME = "com.nerallan.android.criminalintent.time";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // get date from DatePickerFragment args package
        Date date = (Date) getArguments().getSerializable(ARG_TIME);

        // to initialize the datepicker, you must have integer values for month, day, and year.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        // filling view dialog date picker
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time, null);

        mTimePicker = (TimePicker) view.findViewById(R.id.dialog_time_picker);
        mTimePicker.setCurrentHour(hours);
        mTimePicker.setCurrentMinute(minutes);

        return new AlertDialog.Builder(getActivity())
                // dialog box settings
                .setView(view)
                .setTitle(R.string.time_picker)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the application should get the date from the datepicker and
                        // send the result to the CrimeFragment ( calls sendResult() )
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .create();
    }

    public static TimePickerFragment newInstance(Date pDate){
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, pDate);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    // creates an intent, puts the date in it as an addition, and then calls the CrimeFragment.onActivityResult (...)
    private void sendResult(int pResultCode){
        if (getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, getTimeFromTimePicker(mTimePicker));
        getTargetFragment().onActivityResult(getTargetRequestCode(), pResultCode, intent);
    }


    private Date getTimeFromTimePicker(TimePicker pTimePicker) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, pTimePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, pTimePicker.getCurrentMinute());

        return calendar.getTime();
    }

}
