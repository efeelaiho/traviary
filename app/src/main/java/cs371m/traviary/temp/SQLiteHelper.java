package cs371m.traviary.temp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jong Hoon Lim on 3/30/2016.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA = ",";
    public static final String DATABASE_NAME = "traviary.db";
    // create table "states"
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.STATES_TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.STATES_COLUMN_STATE_NAME + TEXT_TYPE + COMMA +
                    FeedReaderContract.FeedEntry.STATES_COLUMN_VISITED + TEXT_TYPE +COMMA +
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
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedReaderContract.FeedEntry.STATES_COLUMN_STATE_NAME, stateName);
        contentValues.put(FeedReaderContract.FeedEntry.STATES_COLUMN_VISITED, "Y");
        long rowInserted = db.insert(FeedReaderContract.FeedEntry.STATES_TABLE_NAME, null, contentValues);
        // close the database
        return rowInserted;
    }

}
