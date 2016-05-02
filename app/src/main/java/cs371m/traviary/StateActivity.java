package cs371m.traviary;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import cs371m.traviary.database.SQLiteHelper;
import cs371m.traviary.datastructures.GridViewAdapter;
import cs371m.traviary.datastructures.ImageItem;

/**
 * Created by jhl2298 on 4/3/2016.
 */
public class StateActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private Button wikipedia;

    private GridView gridView;
    private GridViewAdapter gridViewAdapter;

    private Button cameraButton;

    private static int RESULT_LOAD_IMAGE = 1;

    String imgDecodableString;

    ArrayList<ImageItem> images;

    ProgressDialog pDialog;
    String stateName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.state);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null)
                stateName = null;
            else
                stateName = extras.getString("name");
        } else {
            stateName = (String) savedInstanceState.getSerializable("name");
        }

        /* Display the state name on the toolbar */
        toolbar = (Toolbar) findViewById(R.id.state_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(stateName);

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

        wikipedia = (Button) toolbar.findViewById(R.id.wikipedia_button);
        wikipedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wikipediaIntent = new Intent(StateActivity.this, LocationViewer.class);
                wikipediaIntent.putExtra("name", stateName);
                startActivity(wikipediaIntent);
            }
        });

        gridView = (GridView) findViewById(R.id.grid_view);
        gridViewAdapter = new GridViewAdapter(this, R.layout.grid_state_item, getData());
        gridView.setAdapter(gridViewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                //Create intent
                Intent intent = new Intent(StateActivity.this, DetailsActivity.class);
                intent.putExtra("image", scaleDownBitmap(item.getImage(), 100, StateActivity.this));

                //Start details activity
                startActivity(intent);
            }
        });
    }

    // Prepare some dummy data for gridview
    private ArrayList<ImageItem> getData() {
        SQLiteHelper db = new SQLiteHelper(this);
        images = db.getLocationPhotos(stateName);
        // connect to SQL here
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
                        bitmap = scaleDownBitmap(bitmap, 50, StateActivity.this);
                        byteArray = getBytes(bitmap);
                    } while (byteArray.length > 104000);
                new InsertImage(StateActivity.this, bitmap, stateName, true).execute();
                images.add(new ImageItem(bitmap));
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
            pDialog = new ProgressDialog(StateActivity.this);
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