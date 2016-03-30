package cs371m.traviary.temp;

import android.provider.BaseColumns;

/**
 * Created by Jong Hoon Lim on 3/30/2016.
 */
public class FeedReaderContract {

    // empty constructor
    // do not instantiate this class
    // contract class
    public FeedReaderContract() {};

    /* Inner class that defines the table contents
     * Three tables:
     * "states" table: state_name, visited
     * "images" table: state_name, image_name, image_data
     * "blogs" table: state_name, image_name, image_data
     */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String STATES_TABLE_NAME = "states";
        public static final String STATES_COLUMN_STATE_NAME = "state_name";
        public static final String STATES_COLUMN_VISITED = "visited";
        public static final String IMAGES_TABLE_NAME = "images";
        public static final String IMAGES_COLUMN_IMAGE_NAME = "image_name";
        public static final String IMAGES_COLUMN_IMAGE_DATA = "image_data";
        public static final String BLOGS_TABLE_NAME = "blogs";
        public static final String BLOGS_COLUMN_BLOG_NAME = "blog_name";
        public static final String BLOGS_COLUMN_BLOG_CONTENT = "blog_content";
    }

}