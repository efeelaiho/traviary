package cs371m.traviary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;

import cs371m.traviary.datastructures.ImageItem;

/**
 * Created by Jong Hoon Lim on 3/30/2016.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA = ", ";
    public static final String DATABASE_NAME = "traviary.db";

    // create table "cities"
    private static final String CREATE_CITIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.CITIES_TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.CITIES_COLUMN_CITY_NAME + TEXT_TYPE + COMMA +
                    FeedReaderContract.FeedEntry.CITIES_COLUMN_STATE_NAME + TEXT_TYPE + COMMA +
                    FeedReaderContract.FeedEntry.CITIES_COLUMN_COUNTRY_NAME + TEXT_TYPE + COMMA +
                    FeedReaderContract.FeedEntry.CITIES_COLUMN_FOREIGN + TEXT_TYPE +
                    ")";

    private static final String DELETE_CITIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.CITIES_TABLE_NAME;

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

    // create table "images"
    private static final String CREATE_IMAGES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.IMAGES_TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.IMAGES_COLUMN_LOCATION + TEXT_TYPE + COMMA +
                    FeedReaderContract.FeedEntry.IMAGES_COLUMN_US + TEXT_TYPE +
                    COMMA +FeedReaderContract.FeedEntry.IMAGES_COLUMN_IMAGE_DATA + " BLOB" +
                    ")";

    private static final String DELETE_IMAGES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.IMAGES_TABLE_NAME;

    private static final String SELECT_CITIES =
            "SELECT * FROM " + FeedReaderContract.FeedEntry.CITIES_TABLE_NAME;

    private static final String SELECT_STATES =
            "SELECT * FROM " + FeedReaderContract.FeedEntry.STATES_TABLE_NAME;

    private static final String SELECT_COUNTRIES =
            "SELECT * FROM " + FeedReaderContract.FeedEntry.COUNTRIES_TABLE_NAME;

    private static final String SELECT_IMAGES =
            "SELECT * FROM " + FeedReaderContract.FeedEntry.IMAGES_TABLE_NAME;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CITIES);
        db.execSQL(CREATE_STATES);
        db.execSQL(CREATE_COUNTRIES);
        db.execSQL(CREATE_IMAGES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DELETE_CITIES);
        db.execSQL(DELETE_STATES);
        db.execSQL(DELETE_COUNTRIES);
        db.execSQL(DELETE_IMAGES);
        onCreate(db);
    }

    /*
     * INSERT a new city onto the SQLite database file / device
     * This will happen when a new city has been logged
     */
    public long insertCity(String cityName, String stateName, String countryName) {
        if (stateName != null) {
            if (!checkCityUSA(cityName, stateName)) {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(FeedReaderContract.FeedEntry.CITIES_COLUMN_CITY_NAME, cityName);
                contentValues.put(FeedReaderContract.FeedEntry.CITIES_COLUMN_STATE_NAME, stateName);
                contentValues.put(FeedReaderContract.FeedEntry.CITIES_COLUMN_COUNTRY_NAME, countryName);
                contentValues.put(FeedReaderContract.FeedEntry.CITIES_COLUMN_FOREIGN, "N");
                long rowInserted = db.insert(FeedReaderContract.FeedEntry.CITIES_TABLE_NAME, null, contentValues);
                // close the database
                db.close();
                return rowInserted;
            }
        } else {
            if (!checkCityForeign(cityName, countryName)) {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(FeedReaderContract.FeedEntry.CITIES_COLUMN_CITY_NAME, cityName);
                contentValues.put(FeedReaderContract.FeedEntry.CITIES_COLUMN_STATE_NAME, "");
                contentValues.put(FeedReaderContract.FeedEntry.CITIES_COLUMN_COUNTRY_NAME, countryName);
                contentValues.put(FeedReaderContract.FeedEntry.CITIES_COLUMN_FOREIGN, "Y");
                long rowInserted = db.insert(FeedReaderContract.FeedEntry.CITIES_TABLE_NAME, null, contentValues);
                // close the database
                db.close();
                return rowInserted;
            }
        }
        return -1;
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

    public long insertPhoto(Bitmap imageData, String imageLocation, boolean imageIsUs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedReaderContract.FeedEntry.IMAGES_COLUMN_LOCATION, imageLocation);
        String isUs;
        if (imageIsUs)
            isUs = "Y";
        else
            isUs = "N";
        contentValues.put(FeedReaderContract.FeedEntry.IMAGES_COLUMN_US, isUs);
        contentValues.put(FeedReaderContract.FeedEntry.IMAGES_COLUMN_IMAGE_DATA, getBytes(imageData));
        long rowInserted = db.insert(FeedReaderContract.FeedEntry.IMAGES_TABLE_NAME, null, contentValues);
        return rowInserted;
    }


    public long deleteImage(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(FeedReaderContract.FeedEntry.IMAGES_TABLE_NAME, FeedReaderContract.FeedEntry._ID + "="+id, null);
        return result;
    }


    public ArrayList<ImageItem> getLocationPhotos(String location) {
        ArrayList<ImageItem> images = new ArrayList<>();
        Cursor c = null;
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            String query = "select * from " + FeedReaderContract.FeedEntry.IMAGES_TABLE_NAME +
                    " where " + FeedReaderContract.FeedEntry.IMAGES_COLUMN_LOCATION + " = ?";
            c = db.rawQuery(query, new String[] {location});
            if (c.moveToFirst() ){
                do {
                    byte[] bytes = c.getBlob(3);
                    images.add(new ImageItem(getImage(bytes), c.getLong(0)));
                } while (c.moveToNext());
            }
        }
        finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return images;
    }

    public ArrayList<ImageItem> getUsaPhotos() {
        ArrayList<ImageItem> images = new ArrayList<>();
        Cursor c = null;
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            String query = "select * from " + FeedReaderContract.FeedEntry.IMAGES_TABLE_NAME +
                    " where " + FeedReaderContract.FeedEntry.IMAGES_COLUMN_US + " = ?";
            c = db.rawQuery(query, new String[] {"Y"});
            if (c.moveToFirst() ){
                do {
                    byte[] bytes = c.getBlob(3);
                    images.add(new ImageItem(getImage(bytes), c.getLong(0)));
                } while (c.moveToNext());
            }
        }
        finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return images;
    }

    // convert from bitmap to byte array
    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public boolean checkCityUSA(String currentCity, String currentState) {
        Cursor c = null;
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            String query = "select * from " + FeedReaderContract.FeedEntry.CITIES_TABLE_NAME +
                    " where " + FeedReaderContract.FeedEntry.CITIES_COLUMN_CITY_NAME + " = ?" + " AND "
                    + FeedReaderContract.FeedEntry.CITIES_COLUMN_STATE_NAME + " = ?";
            c = db.rawQuery(query, new String[] {currentCity, currentState});
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

    public boolean checkCityForeign(String currentCity, String currentCountry) {
        Cursor c = null;
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            String query = "select * from " + FeedReaderContract.FeedEntry.CITIES_TABLE_NAME +
                    " where " + FeedReaderContract.FeedEntry.CITIES_COLUMN_CITY_NAME + " = ?" + " AND "
                    + FeedReaderContract.FeedEntry.CITIES_COLUMN_COUNTRY_NAME + " = ?";
            c = db.rawQuery(query, new String[] {currentCity, currentCountry});
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

    public HashSet<String> getVisitedCitiesForState() {
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
    public boolean checkChallengeCompleted(int caseNumber, HashSet<String> states, HashSet<String> countries) {
        switch(caseNumber) {
            case 0: // Columbia
                return countries.contains("Columbia");
            case 1: // All 50 states
                return states.size() == 50;
            case 2: // Secret: North Korea
                return countries.contains("North Korea");
            case 4: // South Korea
                return countries.contains("South Korea");
            case 5: // Hello World
                return countries.size() == 1 || states.size() == 1;
            case 6: // Ireland
                return countries.contains("Ireland");
            case 7: // Texas
                return states.contains("Texas");
            case 8: // California
                return states.contains("California");
            case 9: // Australia
                return countries.contains("Australia");
            case 10: // New York
                return states.contains("New York");
            case 11: // Japan
                return countries.contains("Japan");
            case 12: // France
                return countries.contains("France");
            case 13: // China
                return countries.contains("China");
            case 14: // MOTHER RUSSIA
                return countries.contains("Russia");
        }
        return false;
    }

}
