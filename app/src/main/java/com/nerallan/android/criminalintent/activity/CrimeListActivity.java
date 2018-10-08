package com.nerallan.android.criminalintent.activity;

import android.support.v4.app.Fragment;

import com.nerallan.android.criminalintent.fragment.CrimeListFragment;

/**
 * Created by Nerallan on 10/8/2018.
 */

public class CrimeListActivity extends SingleFragmentActivity{
    // return an instance of the fragment whose host is the given activity
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
