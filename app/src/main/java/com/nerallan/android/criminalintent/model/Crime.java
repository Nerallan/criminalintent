package com.nerallan.android.criminalintent.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Nerallan on 10/1/2018.
 */

public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;        //whether the crime was solved
    private String mSuspect;
    private String mSuspectNumber;


    public Crime(){
        this(UUID.randomUUID());
    }

    public Crime(UUID pId){
        mId = pId;
        mDate = new Date();         // initialize mDate with current date
    }


    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }


    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String pSuspect) {
        mSuspect = pSuspect;
    }

    public void setSuspectNumber(String pSuspectNumber) {
        mSuspectNumber = pSuspectNumber;
    }

    public String getSuspectNumber() {
        return mSuspectNumber;
    }

    public String getPhotoFileName(){
        return "ING_" + getId().toString() + ".jpg";
    }
}
