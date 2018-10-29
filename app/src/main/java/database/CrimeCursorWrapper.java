package database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.nerallan.android.criminalintent.model.Crime;

import java.util.Date;
import java.util.UUID;

import database.CrimeDbSchema.CrimeTable;

/**
 * Created by Nerallan on 10/25/2018.
 */

// this class can be complete cursor class received from the outside by new methods
// It contains the same methods as the encapsulated Cursor class,
// calling these methods leads to exactly the same consequences.
public class CrimeCursorWrapper extends CursorWrapper {

    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    // to extract the data of columns
    public Crime getCrime(){
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        String suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);
        crime.setSuspect(suspect);

        return crime;
    }
}
