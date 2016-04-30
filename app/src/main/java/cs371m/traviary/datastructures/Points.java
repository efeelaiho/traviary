package cs371m.traviary.datastructures;

import android.content.Context;

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
        this.statesPoints = 0;
        this.countriesPoints = 0;
        this.challengesPoints = 0;
        db = new SQLiteHelper(context);
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
