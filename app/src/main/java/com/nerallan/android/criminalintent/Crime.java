package com.nerallan.android.criminalintent;

import java.util.UUID;

/**
 * Created by Nerallan on 10/1/2018.
 */

public class Crime {

    private UUID mId;
    private String mTitle;

    public Crime(){
        // generate random identifiers
        mId = UUID.randomUUID();
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
