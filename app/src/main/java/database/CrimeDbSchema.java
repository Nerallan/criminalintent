package database;

/**
 * Created by Nerallan on 10/24/2018.
 */

public class CrimeDbSchema {
    // The CrimeTable class exists only to define string constants
    // necessary to describe the main parts of a table definition.
    public static final class CrimeTable {
        public static final String NAME = "crimes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
        }
    }
}
