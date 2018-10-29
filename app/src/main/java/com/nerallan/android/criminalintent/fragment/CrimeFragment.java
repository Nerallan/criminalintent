package com.nerallan.android.criminalintent.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.nerallan.android.criminalintent.model.Crime;
import com.nerallan.android.criminalintent.model.CrimeLab;

import java.text.DateFormat;
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
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_CONTACT = 2;

    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private Button mReportButton;
    private Button mSuspectButton;
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

        mReportButton = (Button) v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // create implicit intent
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                // report text and subject line are included in extras
                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                // create a list that will be displayed each time when using an implicit intent to start the activity
                intent = Intent.createChooser(intent, getString(R.string.send_report));
                startActivity(intent);
            }
        });

        mDateButton = (Button)v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dateDialog = DatePickerFragment.newInstance(mCrime.getDate());
                // CrimeFragment is assigned as a target fragment for DatePickerFragment
                // Using the request code, the target fragment can later determine
                // which fragment returns information.
                // FragmentManager saves the target fragment and the request code
                dateDialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                // The string parameter uniquely identifies the DialogFragment in the FragmentManager list.
                dateDialog.show(manager, DIALOG_DATE);
            }
        });

        mTimeButton = (Button)v.findViewById(R.id.crime_time);
        updateTime();
        mTimeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment timeDialog = TimePickerFragment.newInstance(mCrime.getDate());
                timeDialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                timeDialog.show(manager, DIALOG_TIME);
            }
        });

        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                // set flag of crime solution
                mCrime.setSolved(isChecked);
            }
        });

        // create implicit intent for your Contacts book
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if (mCrime.getSuspect() != null){
            // set title for suspect button
            mSuspectButton.setText(mCrime.getSuspect());
        }

        // packageManager knows all about the components installed on an Android device, including all its activities
        PackageManager packageManager = getActivity().getPackageManager();
        // calling resolveActivity (Intent, int), you order to find the activity corresponding to the transferred intent.
        // The MATCH_DEFAULT_ONLY flag limits search to activities with the CATEGORY_ DEFAULT flag
        // If the search is successful, a ResolveInfo instance is returned,
        // which reports full information about the found activity.
        if(packageManager.resolveActivity(pickContact, packageManager.MATCH_DEFAULT_ONLY) == null){
            mSuspectButton.setEnabled(false);
        }

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_TIME){
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setDate(date);
            updateTime();
        } else if (requestCode == REQUEST_CONTACT && data != null){
            Uri contactUri = data.getData();
            // defining fields whose values should be returned by the query
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            // ContentProvider class instances encapsulate databases and provide access to them for other applications.
            // accessing the ContentProvider is through the ContentResolver.
            // requests all the display names of the contacts from the returned data
            Cursor cursor = getActivity().getContentResolver()
                    // execution of the request, contactUri performs the function of the conditions 'where'
                    .query(contactUri, queryFields, null, null, null);
            try{
                // validation of results
                if(cursor.getCount() == 0){
                    return;
                }
                cursor.moveToFirst();
                // fetching the first column of data - the name of the suspect
                String suspect = cursor.getString(0);
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            } finally {
                cursor.close();
            }
        }
    }

    private String getCrimeReport(){
        String solvedString = null;
        if (mCrime.isSolved()){
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM, dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
        String dateString = simpleDateFormat.format(mCrime.getDate());
        String suspect = mCrime.getSuspect();
        if (suspect == null){
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);
        return report;
    }

    // This method creates an instance of the fragment, packages it, and sets its arguments to the fragment
    // to fragment was not tied to a specific host activity
    public static CrimeFragment newInstance(UUID pCrimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, pCrimeId);

        CrimeFragment fragment = new CrimeFragment();
        // To attach a packet of arguments to a fragment, call the Fragment method. setArguments (Bundle).
        // The attachment must be done after creating the fragment, but before it is added to the activity
        fragment.setArguments(args);
        return fragment;
    }


    private void updateDate() {
        mDateButton.setText(formatDate(mCrime.getDate()));
    }

    private void updateTime(){
        mTimeButton.setText(formatTime(mCrime.getDate()));
    }

    private String formatDate(Date pDate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM d, y", Locale.getDefault());
        return simpleDateFormat.format(pDate);
    }

    private String formatTime(Date pDate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return simpleDateFormat.format(pDate);
    }

}
