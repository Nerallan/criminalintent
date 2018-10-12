package com.nerallan.android.criminalintent.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.nerallan.android.criminalintent.fragment.CrimeFragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity{

    private static final String EXTRA_CRIME_ID = "com.nerallan.android.criminalintent.crime_id";

    @Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        // call static method newiInstance and pass necessary parameter to create args for fragment
        return CrimeFragment.newInstance(crimeId);
    }

    public static Intent newIntent(Context packageContext, UUID crimeId){
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }
}
