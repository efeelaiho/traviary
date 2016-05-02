package cs371m.traviary.datastructures;

import android.graphics.Bitmap;

/**
 * Created by jhl2298 on 5/1/2016.
 */
public class ImageItem {
    private Bitmap image;

    public ImageItem(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

}