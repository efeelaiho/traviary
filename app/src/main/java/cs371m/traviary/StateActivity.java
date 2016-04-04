package cs371m.traviary;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by jhl2298 on 4/3/2016.
 */
public class StateActivity extends ActionBarActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.state);
        String stateName;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null)
                stateName = null;
            else
                stateName = extras.getString("name");
        }
        else {
            stateName= (String) savedInstanceState.getSerializable("name");
        }

        /* Attaching the layout to the toolbar object */
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(stateName);
    }

}
