package com.nerallan.android.criminalintent.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.nerallan.android.criminalintent.R;
import com.nerallan.android.criminalintent.fragment.CrimeFragment;
import com.nerallan.android.criminalintent.fragment.CrimeListFragment;
import com.nerallan.android.criminalintent.model.Crime;

/**
 * Created by Nerallan on 10/8/2018.
 */

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks{
    // return an instance of the fragment whose host is the given activity
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    // performs one of two operations depending on the type of device
    @Override
    public void onCrimeSelected(Crime pCrime) {
        // If you used the phone - run a new instance of CrimePagerActivity
        if (findViewById(R.id.detail_fragment_container) == null){
            Intent intent = CrimePagerActivity.newIntent(this, pCrime.getId());
            startActivity(intent);
        } else {
            // if you are using a tablet interface - put CrimeFragment in detail_fragment_container
            Fragment newDetail = CrimeFragment.newInstance(pCrime.getId());
            getSupportFragmentManager().beginTransaction()
                    // removes the existing CrimeFragment instance from the detail_ fragment_container (if it exists)
                    // and adds the CrimeFragment instance that we want to see there.
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    // to reload the CrimeListFragment list, which retrieves the updated information from the database and displays it.
    @Override
    public void onCrimeUpdated(Crime pCrime) {
        CrimeListFragment listFragment = (CrimeListFragment)getSupportFragmentManager()
                                            .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
