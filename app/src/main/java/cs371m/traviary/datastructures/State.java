package cs371m.traviary.datastructures;

import java.util.Comparator;

/**
 * Created by Jong Hoon Lim on 3/29/2016.
 */
public class State implements Comparator<State>, Comparable<State> {

    public String name;
    public boolean visited;
    public int photoId;

    public State(String name, boolean visited, int photoId) {
        this.name = name;
        this.visited = visited;
        this.photoId = photoId;
    }

    @Override
    public int compareTo(State another) {
        boolean s1Completed = this.visited;
        boolean s2Completed = another.visited;
        if (s1Completed && ! s2Completed)
            return -1;
        if (s2Completed && ! s1Completed)
            return +1;
        return 0;
    }

    @Override
    public int compare(State s1, State s2) {
        return s1.compareTo(s2);
    }
}