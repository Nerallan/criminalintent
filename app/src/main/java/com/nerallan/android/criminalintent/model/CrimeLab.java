package com.nerallan.android.criminalintent.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Nerallan on 10/6/2018.
 */
// An instance of the singleton class exists as long as the application remains in memory,
// so when the list is stored in the singleton data object,
// it remains available no matter what happens with activities, fragments and their life cycles.
public class CrimeLab {

    private static CrimeLab sCrimelab;

    private List<Crime> mCrimes;

    private CrimeLab(Context pContext){
        mCrimes = new ArrayList<>();
    }

    public void addCrime(Crime pCrime){
        mCrimes.add(pCrime);
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID pId){
        for (Crime crime: mCrimes){
            if (crime.getId().equals(pId)){
                return crime;
            }
        }
        return null;
    }

    public static CrimeLab get(Context pContext){
        if (sCrimelab == null){
            sCrimelab = new CrimeLab(pContext);
        }
        return sCrimelab;
    }
}

