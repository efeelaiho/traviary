package cs371m.traviary.datastructures;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import cs371m.traviary.database.SQLiteHelper;

/**
 * Created by jhl2298 on 4/30/2016.
 */
public class Points {

    private SQLiteHelper db;
    private int statesPoints;
    private int countriesPoints;
    private int challengesPoints;

    /*
     * Points constructor
     * Param: context
     */
    protected Points(Context context) {
        db = new SQLiteHelper(context);
        this.statesPoints = calculateStatesPoints(db);
        this.countriesPoints = calculateCountriesPoints(db);
        this.challengesPoints = 0;
    }

    /*
     * Helper method to calculate user's countries points
     */
    private int calculateCountriesPoints(SQLiteHelper db) {
        return db.getNumCountries() * 1000; // countries are worth 1000 points each
    }

    /*
     * Helper method to calculate user's states points
     */
    private int calculateStatesPoints(SQLiteHelper db) {
        return db.getNumStates() * 100; // states are worth 100 points each
    }

    /*
     * Get states points
     * Each state is worth 100 points
     */
    protected int getStatesPoints() {
        return this.statesPoints;
    }

    /*
     * Get countries points
     * Each country is worth 1000 points
     */
    protected int getCountriesPoints() {
        return this.countriesPoints;
    }

    /*
     * Get challenges points
     * Each challenges are worth:
     *
     */
    protected int getChallengesPoints() {
        return this.challengesPoints;
    }

    /*
     * Get states points + countries points + challenges points
     */
    protected int getTotalPoints() {
        return this.statesPoints + this.countriesPoints + this.challengesPoints;
    }

}
