package com.nerallan.android.criminalintent.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.nerallan.android.criminalintent.BuildConfig;
import com.nerallan.android.criminalintent.PictureUtils;
import com.nerallan.android.criminalintent.R;
import com.nerallan.android.criminalintent.model.Crime;
import com.nerallan.android.criminalintent.model.CrimeLab;

import java.io.File;
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
    private static final String DIALOG_PHOTO = "DialogPhoto";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_CONTACT = 2;
    private static final int REQUEST_PHONE_CALL = 3;
    private static final int REQUEST_PHOTO = 4;

    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private Button mReportButton;
    private Button mSuspectButton;
    private Button mCallButton;
    private CheckBox mSolvedCheckBox;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Crime mCrime;
    private File mPhotoFile;
    private Callbacks mCallbacks;

    // Required interface for host activity
    // this interface should be implemented in all activities that act as host for the CrimeFragment
    public interface Callbacks {
        void onCrimeUpdated(Crime pCrime);
    }


    @Override
    public void onAttach(Context pContext) {
        super.onAttach(pContext);
        Activity activity = null;
        // Activity is a context so if you can simply check the context is an Activity and cast it if necessary.
        if (pContext instanceof Activity){
            activity = (Activity) pContext;
        }
        mCallbacks = (Callbacks) pContext;
    }


    // customize the fragment instance
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get access to fragment arguments
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
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
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_camera);
        mPhotoView = (ImageView) v.findViewById(R.id.crime_photo);
        mPhotoView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPhotoFile == null || !mPhotoFile.exists()) return;
                FragmentManager manager = getFragmentManager();
                CrimePhotoDialogFragment dialogFragment = CrimePhotoDialogFragment.newInstance(mPhotoFile);
                dialogFragment.show(manager, DIALOG_PHOTO);
            }
        });

        // MediaStore class defines the open interfaces used in Android when working with
        // the main audiovisual materials - images, videos and music.
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // packageManager knows all about the components installed on an Android device, including all its activities
        PackageManager packageManager = getActivity().getPackageManager();
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        if(canTakePhoto){
            // To get the output image in high resolution, you must tell where to store the image in the file system.
            // This task is solved by passing the URI for the place where the file should be stored in the MediaStore. EXTRA_OUTPUT.
            //Uri uri = Uri.fromFile(mPhotoFile);
            Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mPhotoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mReportButton = (Button) v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                  // create implicit intent
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("text/plain");
//                // report text and subject line are included in extras
//                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
//                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
//                // create a list that will be displayed each time when using an implicit intent to start the activity
//                intent = Intent.createChooser(intent, getString(R.string.send_report));
//                startActivity(intent);

                String mimeType = "text/plain";
                Intent shareIntent = ShareCompat.IntentBuilder.from(getActivity())
                        .setChooserTitle(R.string.send_report)
                        .setText(getCrimeReport())
                        .setType(mimeType)
                        .getIntent();
                startActivity(shareIntent);
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
                updateCrime();
            }
        });

        // create implicit intent for your Contacts book
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openContactBook();
//                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        mCallButton = (Button) v.findViewById(R.id.crime_suspect_call);
        mCallButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialPhoneNumber(mCrime.getSuspectNumber());
            }
        });

        if (mCrime.getSuspect() != null){
            // set title for suspect button
            mSuspectButton.setText(mCrime.getSuspect());
        }


        // calling resolveActivity (Intent, int), you order to find the activity corresponding to the transferred intent.
        // The MATCH_DEFAULT_ONLY flag limits search to activities with the CATEGORY_ DEFAULT flag
        // If the search is successful, a ResolveInfo instance is returned,
        // which reports full information about the found activity.
        if(packageManager.resolveActivity(pickContact, packageManager.MATCH_DEFAULT_ONLY) == null){
            mSuspectButton.setEnabled(false);
        }
        updatePhotoView();
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
            updateCrime();
        } else if (requestCode == REQUEST_TIME){
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setDate(date);
            updateTime();
            updateCrime();
        } else if (requestCode == REQUEST_CONTACT && data != null){
            String contactLookupKey;
            Uri contactUri = data.getData();
            // defining fields whose values should be returned by the query
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.LOOKUP_KEY,
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            // ContentProvider class instances encapsulate databases and provide access to them for other applications.
            // accessing the ContentProvider is through the ContentResolver.
            // requests all the display names of the contacts from the returned data
            Cursor cursor = getActivity().getContentResolver()
                    // execution of the request, contactUri performs the function of the conditions 'where'
                    .query(contactUri, queryFields, null, null, null);


            try {
                // validation of results
                if(cursor.getCount() == 0){
                    return;
                }
                cursor.moveToFirst();
                // fetching the first column of data - the name of the suspect
                int lookupColumn = cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY);
                int nameColumn = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                contactLookupKey = cursor.getString(lookupColumn);
                String suspect = cursor.getString(nameColumn);

                mCrime.setSuspect(suspect);
                updateCrime();
                mSuspectButton.setText(suspect);
            } finally {
                cursor.close();
            }

            contactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            // find phone number
            queryFields = new String[]{
                    Phone.NUMBER
            };
            cursor = getActivity().getContentResolver()
                    .query(contactUri,
                            queryFields,
                            ContactsContract.Contacts.LOOKUP_KEY + " = ?" ,
                            new String[] {contactLookupKey},
                            null
                    );

            try {
                if(cursor.getCount() == 0){
                    return;
                }
                // pull out the data of the first row that is phone number
                cursor.moveToFirst();
                String number = cursor.getString(0);

                mCallButton.setEnabled(true);
                mCrime.setSuspectNumber(number);
            } finally {
                cursor.close();
            }
        } else if (requestCode == REQUEST_PHOTO){
            updateCrime();
            updatePhotoView();
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_PHONE_CALL){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                callPhoneNumber();
            }
            else{
                Toast.makeText(getActivity(), getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CONTACT){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openContactBook();
            }
            else{
                Toast.makeText(getActivity(), getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 1. save mCrime into CrimeLab
    // 2. call mCallbacks.onCrimeUpdated (Crime)
    private void updateCrime(){
        CrimeLab.get(getActivity()).updateCrime(mCrime);
        mCallbacks.onCrimeUpdated(mCrime);
    }

    // check user permissions to open contacts
    private void openContactBook(){
        try{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACT);
                    return;
                }
            }
            // create implicit intent for your Contacts book
            final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(pickContact, REQUEST_CONTACT);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    // check user permissions for calling
    private void callPhoneNumber() {
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                    return;
                }
            }
            dialPhoneNumber(mCrime.getSuspectNumber());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    // make implicit intent with suspect phone number
    public void dialPhoneNumber(String pPhoneNumber){
        Intent callSuspect = new Intent(Intent.ACTION_DIAL);
        callSuspect.setData(Uri.parse("tel:" + pPhoneNumber));
        PackageManager packageManager = getActivity().getPackageManager();
        // PackageManager knows all about the components installed on an Android device, including all its activities.
        // By calling resolveActivity (Intent, int), you are ordered to find an activity that matches the transmitted intent.
        if (callSuspect.resolveActivity(packageManager) != null){
            startActivityForResult(callSuspect, REQUEST_CONTACT);
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


    // to upload a Bitmap object to ImageView
    private void updatePhotoView(){
        if(mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
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
