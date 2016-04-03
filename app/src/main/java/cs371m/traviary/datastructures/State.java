package cs371m.traviary.datastructures;

/**
 * Created by Jong Hoon Lim on 3/29/2016.
 */
public class State {

    public String name;
    public boolean visited;
    public int photoId;

    public State(String name, boolean visited, int photoId) {
        this.name = name;
        this.visited = visited;
        this.photoId = photoId;

    }

}