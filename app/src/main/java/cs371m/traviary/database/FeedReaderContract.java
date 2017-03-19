package cs371m.traviary.database;

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
        /*
         * Cities SQL schema
         */
        public static final String CITIES_TABLE_NAME = "cities";
        public static final String CITIES_COLUMN_CITY_NAME = "city_name";
        public static final String CITIES_COLUMN_STATE_NAME = "state_name";

        /*
         * States SQL schema
         */
        public static final String STATES_TABLE_NAME = "states";
        public static final String STATES_COLUMN_STATE_NAME = "state_name";
        public static final String STATES_COLUMN_VISITED = "visited";

        /*
         * Countries SQL schema
         */
        public static final String COUNTRIES_TABLE_NAME = "countries";
        public static final String COUNTRIES_COLUMN_COUNTRY_NAME = "countries_name";
        public static final String COUNTRIES_COLUMN_VISITED = "visited";

        /*
         * Images SQL schema
         */
        public static final String IMAGES_TABLE_NAME = "images";
        public static final String IMAGES_COLUMN_IMAGE_DATA = "image_data";
        public static final String IMAGES_COLUMN_LOCATION = "image_location";
        public static final String IMAGES_COLUMN_US = "image_is_us";

    }

}