package cs371m.traviary.datastructures;

import java.util.Comparator;

/**
 * Created by jhl2298 on 4/28/2016.
 */
public class Country implements Comparator<Country>, Comparable<Country> {

    public String name;
    public boolean visited;
    public int photoId;

    public Country(String name, boolean visited, int photoId) {
        this.name = name;
        this.visited = visited;
        this.photoId = photoId;
    }

    @Override
    public int compareTo(Country another) {
        boolean c1Completed = this.visited;
        boolean c2Completed = another.visited;
        if (c1Completed && ! c2Completed)
            return -1;
        if (c2Completed && ! c1Completed)
            return +1;
        return 0;
    }

    @Override
    public int compare(Country c1, Country c2) {
        return c1.compareTo(c2);
    }
}
