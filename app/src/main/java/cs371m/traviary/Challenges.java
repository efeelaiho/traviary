package cs371m.traviary;

import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import cs371m.traviary.datastructures.Challenge;
import cs371m.traviary.datastructures.ChallengesAdapter;

/**
 * Created by efosaelaiho on 3/25/16.
 */
public class Challenges extends Fragment {

    private TreeMap<String,Boolean> challenges;
    private RecyclerView recyclerView;
    private List<Challenge> challengesTempList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.challenges,container,false);
        MainActivity mainActivity = (MainActivity)getActivity();

        recyclerView = (RecyclerView) v.findViewById(R.id.challenge_rv);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        initializeData();
        initializeAdapter();

        return v;
    }

    private void initializeData() {
        challengesTempList = new ArrayList<Challenge>();
        Resources resource = getResources();
        String[] challengeNames = resource.getStringArray(R.array.challenge_strings);
        for (String challenge : challengeNames) {
            challengesTempList.add(new Challenge(challenge, false, ""));
        }
    }

    private void initializeAdapter() {
        ChallengesAdapter adapter = new ChallengesAdapter(challengesTempList, getContext());
        recyclerView.setAdapter(adapter);
    }

}
