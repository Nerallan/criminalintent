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
import android.widget.DatePicker;

import com.nerallan.android.criminalintent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.support.v4.os.LocaleListCompat.create;

/**
 * Created by Nerallan on 10/15/2018.
 */

public class DatePickerFragment extends DialogFragment {

    private static final String ARG_DATE = "date";
    private DatePicker mDatePicker;

    public static final String EXTRA_DATE = "com.nerallan.android.criminalintent.date";

    // FragmentManager instance of host-activity calls this method during DialogFragment output to the screen
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // get date from DatePickerFragment args package
        Date date = (Date) getArguments().getSerializable(ARG_DATE);

        // to initialize the datepicker, you must have integer values for month, day, and year.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // filling view dialog date picker
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null);

        mDatePicker = (DatePicker) view.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, null);

        return new AlertDialog.Builder(getActivity())
                // dialog box settings
                .setView(view)
                .setTitle(R.string.date_picker)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the application should get the date from the datepicker and
                        // send the result to the CrimeFragment ( calls sendResult() )
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        Date date = new GregorianCalendar(year, month, day).getTime();
                        sendResult(Activity.RESULT_OK, date);
                    }
                })
                // Create the AlertDialog object and return it
                .create();
    }




    public static DatePickerFragment newInstance(Date pDate){
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, pDate);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    // creates an intent, puts the date in it as an addition, and then calls the CrimeFragment.onActivityResult (...)
    private void sendResult(int pResultCode, Date pDate){
        if (getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, pDate);
        getTargetFragment().onActivityResult(getTargetRequestCode(), pResultCode, intent);
    }
}
