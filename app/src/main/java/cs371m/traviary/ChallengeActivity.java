package cs371m.traviary;

import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by jhl2298 on 4/30/2016.
 */
public class ChallengeActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private TextView challengeDescription;
    private TextView challengeCompleted;
    private TextView challengePoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challenge_page);

        // get current state name
        final String challengeName;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null)
                challengeName = null;
            else
                challengeName = extras.getString("name");
        }
        else {
            challengeName = (String) savedInstanceState.getSerializable("name");
        }

        /* Display the state name on the toolbar */
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(challengeName);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        challengeDescription = (TextView)findViewById(R.id.challenge_description);
        challengeCompleted = (TextView)findViewById(R.id.challenge_completed);
        challengePoints = (TextView) findViewById(R.id.challenge_points);
        challengeDescription.setText(getIntent().getExtras().getString("description"));
        String completed;
        if (getIntent().getExtras().getString("completed").equals("true"))
            completed = "Yes";
        else
            completed = "No";
        challengeCompleted.setText(completed);
        challengePoints.setText(getIntent().getExtras().getString("points"));
    }
}
