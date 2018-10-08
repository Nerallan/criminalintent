package com.nerallan.android.criminalintent.activity;

import android.support.v4.app.Fragment;

import com.nerallan.android.criminalintent.fragment.CrimeFragment;

public class CrimeActivity extends SingleFragmentActivity{


    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }
}
