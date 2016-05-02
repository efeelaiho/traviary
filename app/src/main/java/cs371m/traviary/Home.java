package cs371m.traviary;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashSet;

import cs371m.traviary.datastructures.Points;


/**
 * Created by efosaelaiho on 3/25/16.
 */
public class Home extends Fragment {

    private Button mLocationButton;
    private Button mAttractionButton;
    private ImageButton info_button;

    private TextView score;
    private TextView statesStat;
    private TextView countriesStats;
    Bundle savedInstanceState;

    Point p;

    HashSet<String> states;
    HashSet<String> countries;
    int statesPoints;
    int countriesPoints;
    int challengesPoints;
    int totalPoints;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home, container, false);
        final MainActivity mainActivity = (MainActivity)getActivity();
        mLocationButton = (Button)v.findViewById(R.id.location_button);
        //mAttractionButton = (Button)v.findViewById(R.id.attraction_button);
        score = (TextView) v.findViewById(R.id.score);
        statesStat = (TextView) v.findViewById(R.id.states_stats);
        countriesStats = (TextView) v.findViewById(R.id.country_stats);
        info_button = (ImageButton)v.findViewById(R.id.info_button);

        mLocationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // go to MapsActivity
                mainActivity.getLocation();
            }
        });


        // update points here
        updateStats();

        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String intro = "Your score is comprised of:";
                String statesAlert;
                if (states.size() > 1)
                    statesAlert = states.size() + " states times 100 equals " + statesPoints + ".";
                else
                    statesAlert = states.size() + " state times 100 equals " + statesPoints + ".";
                String countriesAlert;
                if (countries.size() > 1)
                    countriesAlert = countries.size() + " countries times 1000 points equals " + countriesPoints + " points.";
                else
                    countriesAlert = countries.size() + " country times 1000 equals " + countriesPoints + " points.";
                String challengesAlert = "You have " + challengesPoints + " points from challenges.";
                String totalAlert = "You have a total of " + totalPoints + " points.";
                new AlertDialog.Builder(getContext())
                        .setTitle("Your Score")
                        .setMessage(intro + "\n" + statesAlert + "\n" + countriesAlert + "\n" +
                                challengesAlert + "\n" + totalAlert).
                        setNeutralButton("Close", null).show();
            }
        });


        info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Welcome to Traviary")
                        .setNeutralButton("Close", null).show();
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
        states = points.getStates();
        countries = points.getCountries();
        statesPoints = points.getStatesPoints();
        countriesPoints = points.getCountriesPoints();
        challengesPoints = points.getChallengesPoints();
        totalPoints = points.getTotalPoints();
        score.setText(Integer.toString(points.getTotalPoints()));
        statesStat.setText("States Visited: " + points.getStates().size() + " out of 50");
        countriesStats.setText("Countries Visited: " + points.getCountries().size() + " out of 280");
    }
}
