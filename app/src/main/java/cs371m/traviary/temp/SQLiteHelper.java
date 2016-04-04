package cs371m.traviary.temp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Jong Hoon Lim on 3/30/2016.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA = ", ";
    public static final String DATABASE_NAME = "traviary.db";
    // create table "states"
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.STATES_TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.STATES_COLUMN_STATE_NAME + TEXT_TYPE + COMMA +
                    FeedReaderContract.FeedEntry.STATES_COLUMN_VISITED + TEXT_TYPE +
                    ")";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.STATES_TABLE_NAME;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
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

}
