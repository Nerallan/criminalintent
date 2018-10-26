package com.nerallan.android.criminalintent.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import database.CrimeBaseHelper;
import database.CrimeCursorWrapper;
import database.CrimeDbSchema;
import database.CrimeDbSchema.CrimeTable;

/**
 * Created by Nerallan on 10/6/2018.
 */
// An instance of the singleton class exists as long as the application remains in memory,
// so when the list is stored in the singleton data object,
// it remains available no matter what happens with activities, fragments and their life cycles.
public class CrimeLab {

    private static CrimeLab sCrimelab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private CrimeLab(Context pContext){
        mContext = pContext.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext)
                // in that moment class CrimeBaseHelper:
                // 1. opens /data/data/com.nerallan.android.criminalintent/databases/crimeBase.db.
                //    If the database file does not exist, then it is created;
                // 2. if the database is opened for the first time, calls the onCreate
                //    method (SQLiteDatabase) and then save the latest version number
                // 3. if the database is not open for the first time, check its version number. If
                //    the database version in CrimeOpenHelper is higher, then calls onUpgrade method (SQLiteDatabase, int, int)
                .getWritableDatabase();
    }


    // the nullColumnHack will be a name of one of the columns in database that can contain a null value
    public void addCrime(Crime pCrime){
        ContentValues values = getContentValues(pCrime);
        mDatabase.insert(CrimeTable.NAME, null, values);
    }


    public List<Crime> getCrimes() {
        // snapshot of crime objects at particular moment of time
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursorWrapper = queryCrimes(null, null);

        try {
            cursorWrapper.moveToFirst();
            while(!cursorWrapper.isAfterLast()){
                crimes.add(cursorWrapper.getCrime());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }
        return crimes;
    }


    // method extracts only the first element
    public Crime getCrime(UUID pId){
        CrimeCursorWrapper cursorWrapper = queryCrimes(CrimeTable.Cols.UUID + " = ?",
                new String[] {pId.toString()});
        try{
            if (cursorWrapper.getCount() == 0){
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getCrime();
        } finally {
            cursorWrapper.close();
        }
    }


    public static CrimeLab get(Context pContext){
        if (sCrimelab == null){
            sCrimelab = new CrimeLab(pContext);
        }
        return sCrimelab;
    }


    // reading database data
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        // The cursor received at the request is packed into the CrimeCursorWrapper,
        // after which its contents are sorted by the getCrime () method to get the Crime objects.
        Cursor cursor = mDatabase.query(CrimeTable.NAME,
                null, // culumns - null, select all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null,   // having
                null    // orderBy
        );
        return new CrimeCursorWrapper(cursor);
    }


    public void updateCrime(Crime pCrime){
        String uuidString = pCrime.getId().toString();
        ContentValues values = getContentValues(pCrime);

        // ContentValues object should be assigned to each updating record
        //
        mDatabase.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID + " = ?",
                new String[] {uuidString});
    }


    // Database recording and updating is done using the ContentValues class
    // this method convert the Crime object to ContentValues.
    private static ContentValues getContentValues(Crime pCrime){
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, pCrime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, pCrime.getTitle());
        values.put(CrimeTable.Cols.DATE, pCrime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, pCrime.isSolved() ? 1 : 0);
        return values;
    }
}

