package cs371m.traviary.datastructures;

import java.util.Comparator;

/**
 * Created by jhl2298 on 4/1/2016.
 */
public class Challenge implements Comparator<Challenge> {

    public int challengeNumber;
    public String name;
    public boolean completed;
    public String description;
    public int pointsWorth;

    public Challenge(int challengeNumber, String name, boolean completed, String description, int pointsWorth) {

        this.challengeNumber = challengeNumber;
        this.name = name;
        this.completed = completed;
        this.description = description;
        this.pointsWorth = pointsWorth;

    }

    @Override
    public int compare(Challenge c1, Challenge c2) {
        boolean c1Completed = c1.completed;
        boolean c2Completed = c2.completed;
        if (c1Completed && ! c2Completed)
            return +1;
        if (c2Completed && ! c1Completed)
            return -1;
        return 0;
    }
}
