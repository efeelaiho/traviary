package cs371m.traviary.datastructures;

/**
 * Created by Jong Hoon Lim on 3/29/2016.
 */
public class State {

    String name;
    boolean visited;
    int photoId;
    int checkedId;
    int unchecked_Id;

    public State(String name, boolean visited, int photoId, int checkedId, int unchecked_Id) {
        this.name = name;
        this.visited = visited;
        this.photoId = photoId;
        this.unchecked_Id = unchecked_Id;
        this.checkedId = checkedId;
    }

}