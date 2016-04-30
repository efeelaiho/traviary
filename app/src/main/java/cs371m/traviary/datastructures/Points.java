package cs371m.traviary.datastructures;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashSet;

import cs371m.traviary.R;
import cs371m.traviary.database.SQLiteHelper;

/**
 * Created by jhl2298 on 4/30/2016.
 */
public class Points {

    private SQLiteHelper db;
    private int statesPoints;
    private int countriesPoints;
    private int challengesPoints;
    private Context context;

    /*
     * Points constructor
     * Param: context
     */
    public Points(Context context) {
        this.context = context;
        db = new SQLiteHelper(this.context);
        this.statesPoints = calculateStatesPoints(db);
        this.countriesPoints = calculateCountriesPoints(db);
        this.challengesPoints = calculateChallengesPoints(db);
    }

    /*
     * Helper method to calculate user's challenges points
     */
    public int calculateChallengesPoints(SQLiteHelper db) {
        Resources resource = this.context.getResources();
        int[] challengePointsWorth = resource.getIntArray(R.array.challenge_worth);
        int total = 0;
        HashSet<String> states = db.getVisitedStates();
        HashSet<String> countries = db.getVisitedCountries();
        for (int caseNumber = 0; caseNumber < challengePointsWorth.length; caseNumber++) {
            if (db.checkChallengeCompleted(caseNumber, states, countries)) {
                total += challengePointsWorth[caseNumber];
            }
        }
        return total;
    }

    /*
     * Helper method to calculate user's countries points
     */
    public int calculateCountriesPoints(SQLiteHelper db) {
        return db.getNumCountries() * 1000; // countries are worth 1000 points each
    }

    /*
     * Helper method to calculate user's states points
     */
    public int calculateStatesPoints(SQLiteHelper db) {
        return db.getNumStates() * 100; // states are worth 100 points each
    }

    /*
     * Get states points
     * Each state is worth 100 points
     */
    public int getStatesPoints() {
        return this.statesPoints;
    }

    /*
     * Get countries points
     * Each country is worth 1000 points
     */
    public int getCountriesPoints() {
        return this.countriesPoints;
    }

    /*
     * Get challenges points
     * Each challenges are worth:
     *
     */
    public int getChallengesPoints() {
        return this.challengesPoints;
    }

    /*
     * Get states points + countries points + challenges points
     */
    public int getTotalPoints() {
        return this.statesPoints + this.countriesPoints + this.challengesPoints;
    }

}
