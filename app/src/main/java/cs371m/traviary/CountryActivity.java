package cs371m.traviary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country);

        // get current state name
        final String countryName;
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
                intent.putExtra("image", item.getImage());

                //Start details activity
                startActivity(intent);
            }
        });
    }

    // Prepare some dummy data for gridview
    private ArrayList<ImageItem> getData() {
        images = new ArrayList<>();
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
                images.add(new ImageItem(scaleDownBitmap(BitmapFactory.decodeFile(imgDecodableString), 100, getApplicationContext())));
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

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h = (int) (newHeight * densityMultiplier);
        int w = (int) (h * photo.getWidth() / ((double) photo.getHeight()));

        photo = Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }
}
