package com.nerallan.android.criminalintent.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.nerallan.android.criminalintent.R;

import static android.support.v4.os.LocaleListCompat.create;

/**
 * Created by Nerallan on 10/15/2018.
 */

public class DatePickerFragment extends DialogFragment {

    // FragmentManager instance of host-activity calls this method during DialogFragment output to the screen
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // filling view dialog date picker
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null);

        return new AlertDialog.Builder(getActivity())
                // dialog box settings
                .setView(view)
                .setTitle(R.string.date_picker)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
