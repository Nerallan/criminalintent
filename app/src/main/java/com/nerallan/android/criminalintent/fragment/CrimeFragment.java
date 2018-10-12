package com.nerallan.android.criminalintent.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.nerallan.android.criminalintent.R;
import com.nerallan.android.criminalintent.activity.CrimeActivity;
import com.nerallan.android.criminalintent.model.Crime;
import com.nerallan.android.criminalintent.model.CrimeLab;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import static android.widget.CompoundButton.*;

/**
 * Created by Nerallan on 10/2/2018.
 */

// A Bundle object can be attached to each fragment instance.
// This object contains key-value pairs that work in the same way as intent extras in Activity.
// Each such pair is called an argument.

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";

    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Crime mCrime;

    // customize the fragment instance
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get access to fragment arguments
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }


    // creating and customizing the fragment view
    // fill the layout of fragment view
    // the filled View object is returned to the host activity
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateButton = (Button)v.findViewById(R.id.crime_date);

        mDateButton.setText(formatDate(mCrime.getDate()));
        // button is blocked
        mDateButton.setEnabled(false);

        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                // set flag of crime solution
                mCrime.setSolved(isChecked);
            }
        });
        return v;
    }


    // This method creates an instance of the fragment, packages it, and sets its arguments to the fragment
    public static CrimeFragment newInstance(UUID pCrimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, pCrimeId);

        CrimeFragment fragment = new CrimeFragment();
        // To attach a packet of arguments to a fragment, call the Fragment method. setArguments (Bundle).
        // The attachment must be done after creating the fragment, but before it is added to the activity
        fragment.setArguments(args);
        return fragment;
    }

    private String formatDate(Date pDate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM d, y", Locale.getDefault());
        return simpleDateFormat.format(pDate);
    }

}
