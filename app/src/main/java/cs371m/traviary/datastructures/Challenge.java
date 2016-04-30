package cs371m.traviary.datastructures;

/**
 * Created by jhl2298 on 4/1/2016.
 */
public class Challenge {

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

}
