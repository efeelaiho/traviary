package cs371m.traviary.datastructures;

import android.graphics.Bitmap;

/**
 * Created by jhl2298 on 5/1/2016.
 */
public class ImageItem {
    private Bitmap image;
    private long id;

    public ImageItem(Bitmap image, long id) {
        this.image = image;
        this.id = id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

}