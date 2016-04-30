package cs371m.traviary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashSet;

/**
 * Created by Jong Hoon Lim on 3/30/2016.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA = ", ";
    public static final String DATABASE_NAME = "traviary.db";
    // create table "states"
    private static final String CREATE_STATES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.STATES_TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.STATES_COLUMN_STATE_NAME + TEXT_TYPE + COMMA +
                    FeedReaderContract.FeedEntry.STATES_COLUMN_VISITED + TEXT_TYPE +
                    ")";
    private static final String DELETE_STATES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.STATES_TABLE_NAME;

    // create table "countries"
    private static final String CREATE_COUNTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.COUNTRIES_TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.COUNTRIES_COLUMN_COUNTRY_NAME + TEXT_TYPE + COMMA +
                    FeedReaderContract.FeedEntry.COUNTRIES_COLUMN_VISITED + TEXT_TYPE +
                    ")";
    private static final String DELETE_COUNTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.COUNTRIES_TABLE_NAME;

    private static final String SELECT_STATES =
            "SELECT * FROM " + FeedReaderContract.FeedEntry.STATES_TABLE_NAME;
    private static final String SELECT_COUNTRIES =
            "SELECT * FROM " + FeedReaderContract.FeedEntry.COUNTRIES_TABLE_NAME;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STATES);
        db.execSQL(CREATE_COUNTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DELETE_STATES);
        db.execSQL(DELETE_COUNTRIES);
        onCreate(db);
    }

    /*
     * Insert a new state onto the SQLite database file / device
     * This will happen when a new state's location has been logged
     */
    public long insertState(String stateName) {
        if (!checkState(stateName)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(FeedReaderContract.FeedEntry.STATES_COLUMN_STATE_NAME, stateName);
            contentValues.put(FeedReaderContract.FeedEntry.STATES_COLUMN_VISITED, "Y");
            long rowInserted = db.insert(FeedReaderContract.FeedEntry.STATES_TABLE_NAME, null, contentValues);
            // close the database
            db.close();
            return rowInserted;
        }
        return -1;
    }

    public long insertCountry(String countryName) {
        if (!checkCountry(countryName)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(FeedReaderContract.FeedEntry.COUNTRIES_COLUMN_COUNTRY_NAME, countryName);
            contentValues.put(FeedReaderContract.FeedEntry.COUNTRIES_COLUMN_VISITED, "Y");
            long rowInserted = db.insert(FeedReaderContract.FeedEntry.COUNTRIES_TABLE_NAME, null, contentValues);
            // close the database
            db.close();
            return rowInserted;
        }
        return -1;
    }

    public boolean checkState(String currentState) {
        Cursor c = null;
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            String query = "select * from " + FeedReaderContract.FeedEntry.STATES_TABLE_NAME +
                    " where " + FeedReaderContract.FeedEntry.STATES_COLUMN_STATE_NAME + " = ?";
            c = db.rawQuery(query, new String[] {currentState});
            if (c.moveToFirst()) {
                return true;
            }
            return false;
        }
        finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    public boolean checkCountry(String currentCountry) {
        Cursor c = null;
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            String query = "select * from " + FeedReaderContract.FeedEntry.COUNTRIES_TABLE_NAME +
                    " where " + FeedReaderContract.FeedEntry.COUNTRIES_COLUMN_COUNTRY_NAME + " = ?";
            c = db.rawQuery(query, new String[] {currentCountry});
            if (c.moveToFirst()) {
                return true;
            }
            return false;
        }
        finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    public String getAllStatesRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        String tableString = String.format("Table %s:\n", FeedReaderContract.FeedEntry.STATES_TABLE_NAME);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + FeedReaderContract.FeedEntry.STATES_TABLE_NAME, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }
        db.close();

        return tableString;
    }

    public HashSet<String> getVisitedStates() {
        HashSet<String> visitedStates = new HashSet<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor allRows  = db.rawQuery("SELECT * FROM " + FeedReaderContract.FeedEntry.STATES_TABLE_NAME, null);
        if (allRows.moveToFirst() ){ // will return true if query returned at least 1 row
            do {
                visitedStates.add(allRows.getString(1)); // get state name for current row
            } while (allRows.moveToNext());
        }
        db.close();
        return visitedStates;
    }

    public HashSet<String> getVisitedCountries() {
        HashSet<String> visitedCountries = new HashSet<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor allRows  = db.rawQuery("SELECT * FROM " + FeedReaderContract.FeedEntry.COUNTRIES_TABLE_NAME, null);
        if (allRows.moveToFirst() ){ // will return true if query returned at least 1 row
            do {
                visitedCountries.add(allRows.getString(1)); // get state name for current row
            } while (allRows.moveToNext());
        }
        db.close();
        return visitedCountries;
    }

    public int getNumStates() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_STATES, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getNumCountries() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_COUNTRIES, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    /*
     * Since we can access the challenges points worth by int array, check challenges via a switch case
     */
    private boolean checkChallengeCompleted(int caseNumber) {
        switch(caseNumber) {
            case 0: // Columbia
                break;
            case 1: // All 50 states
                break;
            case 2: // Secret: North Korea
                break;
            case 4: // South Korea
                break;
            case 5: // Hello World
                break;
            case 6: // Ireland
                break;
            case 7: // Texas
                break;
            case 8: // California
                break;
            case 9: // Australia
                break;
            case 10: // New York
                break;
            case 11: // Japan
                break;
            case 12: // France
                break;
            case 13: // China
                break;
            case 14: // MOTHER RUSSIA
                break;
        }
        return false;
    }

}
