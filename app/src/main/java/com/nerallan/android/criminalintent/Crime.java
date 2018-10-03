package com.nerallan.android.criminalintent;

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

    public Crime(){
        mId = UUID.randomUUID();    // generate random identifiers
        mDate = new Date();         // initialize mDate with current date
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
}
