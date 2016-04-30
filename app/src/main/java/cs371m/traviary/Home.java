package cs371m.traviary;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.TextView;

import cs371m.traviary.datastructures.Points;


/**
 * Created by efosaelaiho on 3/25/16.
 */
public class Home extends Fragment {

    private Button mLocationButton;
    private Button mAttractionButton;

    private TextView score;
    private TextView statesStat;
    private TextView countriesStats;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home,container,false);
        final MainActivity mainActivity = (MainActivity)getActivity();
        mLocationButton = (Button)v.findViewById(R.id.location_button);
        mAttractionButton = (Button)v.findViewById(R.id.attraction_button);
        score = (TextView) v.findViewById(R.id.score);
        statesStat = (TextView) v.findViewById(R.id.states_stats);
        countriesStats = (TextView) v.findViewById(R.id.country_stats);

        mLocationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // go to MapsActivity
                mainActivity.getLocation();
            }
        });

        mAttractionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                // I guess it will launch Google places API
            }
        });

        // update points here
        updateStats();

        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        // make sure to recalculate points onResume
        updateStats();
    }

    public void updateStats() {
        Points points = new Points(getContext());
        System.out.println("STATES POINTS: " + points.getStatesPoints());
        System.out.println("COUNTRIES POINTS: " + points.getCountriesPoints());
        System.out.println("CHALLENGES POINTS: " + points.getChallengesPoints());
        score.setText(Integer.toString(points.getTotalPoints()));
        statesStat.setText("States Visited: " + points.getStates().size() + " out of 50");
        countriesStats.setText("Countries Visited: " + points.getCountries().size() + " out of 280");
    }
}
