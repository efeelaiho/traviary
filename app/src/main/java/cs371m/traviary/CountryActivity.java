package cs371m.traviary;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import cs371m.traviary.database.SQLiteHelper;
import cs371m.traviary.datastructures.GridViewAdapter;
import cs371m.traviary.datastructures.ImageItem;

/**
 * Created by jhl2298 on 4/28/2016.
 */
public class CountryActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private Button cameraButton;

    private GridView gridView;
    private GridViewAdapter gridViewAdapter;

    private static int RESULT_LOAD_IMAGE = 1;

    String imgDecodableString;

    ArrayList<ImageItem> images;

    ProgressDialog pDialog;
    String countryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country);

        // get current state name
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null)
                countryName = null;
            else
                countryName = extras.getString("name");
        } else {
            countryName = (String) savedInstanceState.getSerializable("name");
        }

        /* Display the state name on the toolbar */
        toolbar = (Toolbar) findViewById(R.id.country_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(countryName);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cameraButton = (Button) toolbar.findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        gridView = (GridView) findViewById(R.id.grid_view);
        gridViewAdapter = new GridViewAdapter(this, R.layout.grid_state_item, getData());
        gridView.setAdapter(gridViewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                //Create intent
                Intent intent = new Intent(CountryActivity.this, DetailsActivity.class);
                intent.putExtra("image", scaleDownBitmap(item.getImage(), 100, CountryActivity.this));

                //Start details activity
                startActivity(intent);
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                new AlertDialog.Builder(CountryActivity.this)
                        .setMessage("Delete image?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SQLiteHelper db = new SQLiteHelper(CountryActivity.this);
                                long result = db.deleteImage(item.getId());
                                if (result == -1)
                                    new AlertDialog.Builder(CountryActivity.this).
                                            setMessage("Could not delete your image.").
                                            setNeutralButton("Close", null).show();
                                else {
                                    new AlertDialog.Builder(CountryActivity.this).
                                            setMessage("Successfully deleted your image.").
                                            setNeutralButton("Close", null).show();
                                    images.remove(item);
                                    gridViewAdapter.notifyDataSetChanged();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // DO NOTHING
                            }
                        }).show();
                return true;
            }
        });
    }

    // Prepare some dummy data for gridview
    private ArrayList<ImageItem> getData() {
        SQLiteHelper db = new SQLiteHelper(this);
        // connect to SQL here
        images = db.getLocationPhotos(countryName);
        if (countryName.equals("United States"))
            images.addAll(db.getUsaPhotos());
        return images;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                Bitmap bitmap = BitmapFactory.decodeFile(imgDecodableString);
                byte[] byteArray = getBytes(bitmap);
                if (byteArray.length > 1040000)
                    do {
                        bitmap = scaleDownBitmap(bitmap, 50, CountryActivity.this);
                        byteArray = getBytes(bitmap);
                    } while (byteArray.length > 1040000);
                new InsertImage(CountryActivity.this, bitmap, countryName, false).execute();
                images.add(new ImageItem(bitmap, -2));
                gridViewAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "You haven't picked an image.",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG)
                    .show();
        }
    }

    // convert from bitmap to byte array
    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h = (int) (newHeight * densityMultiplier);
        int w = (int) (h * photo.getWidth() / ((double) photo.getHeight()));

        photo = Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }

    private class InsertImage extends AsyncTask<String, String, Long> {

        Context context;
        Bitmap imageData;
        String location;
        boolean isUs;

        public InsertImage(Context context, Bitmap imageData, String location, boolean isUs) {
            this.context = context;
            this.imageData = imageData;
            this.location = location;
            this.isUs = isUs;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CountryActivity.this);
            StringBuilder message = new StringBuilder("Attempting to save your image...");
            pDialog.setMessage(message);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Long doInBackground(String... params) {
            SQLiteHelper db = new SQLiteHelper(context);
            return db.insertPhoto(this.imageData, this.location, this.isUs);
        }

        @Override
        protected void onPostExecute(Long success) {
            super.onPostExecute(success);
            pDialog.dismiss();
            if (success == -1) {
                new AlertDialog.Builder(this.context)
                        .setTitle("")
                        .setMessage("We could not save your image.")
                        .setNeutralButton("Close", null)
                        .show();
            } else {
                new AlertDialog.Builder(this.context)
                        .setTitle("")
                        .setMessage("You have successfully saved an image for " + location + ".")
                        .setNeutralButton("Close", null)
                        .show();
            }
        }

    }
}
