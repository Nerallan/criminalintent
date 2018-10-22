package com.nerallan.android.criminalintent.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.nerallan.android.criminalintent.R;

/**
 * Created by Nerallan on 10/8/2018.
 */
// class AppCompatActivity is a subclass of FragmentActivity
public abstract class SingleFragmentActivity extends AppCompatActivity{

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        // getting instance of CrimeFragment from FragmentManager by container id
        // if such container identifier already exist in FragmentManager
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = createFragment();
            // create and return instance FragmentTransaction
            // methods that customize FragmentTransaction returns FragmentTransaction
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
