package cs371m.traviary;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

/**
 * Created by jhl2298 on 4/28/2016.
 */
public class CountryActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private Button wikipedia;

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
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(countryName);
    }
}
