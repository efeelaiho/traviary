package cs371m.traviary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

/**
 * Created by jhl2298 on 4/3/2016.
 */
public class StateActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private Button wikipedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.state);

        // get current state name
        final String stateName;
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

        /* Display the state name on the toolbar */
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
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

        wikipedia = (Button) findViewById(R.id.wikipedia);
        wikipedia.setText(stateName + " Wikipedia");
        wikipedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wikipediaIntent = new Intent(StateActivity.this, LocationViewer.class);
                wikipediaIntent.putExtra("name", stateName);
                startActivity(wikipediaIntent);
            }
        });

    }

}
