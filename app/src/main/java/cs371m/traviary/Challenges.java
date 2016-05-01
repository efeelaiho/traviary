package cs371m.traviary;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cs371m.traviary.database.SQLiteHelper;
import cs371m.traviary.datastructures.Challenge;
import cs371m.traviary.datastructures.ChallengesAdapter;

/**
 * Created by efosaelaiho on 3/25/16.
 */
public class Challenges extends Fragment {

    private RecyclerView recyclerView;
    private List<Challenge> challengesTempList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.challenges,container,false);

        recyclerView = (RecyclerView) v.findViewById(R.id.challenge_rv);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        Resources resource = getResources();
        final String[] challengeNames = resource.getStringArray(R.array.challenge_strings);

        initializeData();
        initializeAdapter();

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent challengeIntent = new Intent(getContext(), ChallengeActivity.class);
                challengeIntent.putExtra("name", challengeNames[position]);
                challengeIntent.putExtra("description", challengesTempList.get(position).description);
                challengeIntent.putExtra("completed", String.valueOf(challengesTempList.get(position).completed));
                challengeIntent.putExtra("points", Integer.toString(challengesTempList.get(position).pointsWorth));
                startActivity(challengeIntent);
            }
        });

        return v;
    }

    private void initializeData() {
        SQLiteHelper db = new SQLiteHelper(getContext());
        HashSet<String> states = db.getVisitedStates();
        HashSet<String> countries = db.getVisitedCountries();
        // name, completed, description, points worth
        challengesTempList = new ArrayList<>();
        Resources resource = getResources();
        String[] challengeNames = resource.getStringArray(R.array.challenge_strings);
        String[] challengeDescriptions = resource.getStringArray(R.array.challenge_detail_strings);
        int[] challengePointsWorth = resource.getIntArray(R.array.challenge_worth);
        for (int index = 0; index < challengeNames.length; index++) {
            challengesTempList.add(new Challenge(index, challengeNames[index],
                    db.checkChallengeCompleted(index, states, countries),
                    challengeDescriptions[index], challengePointsWorth[index]));
        }
    }

    private void initializeAdapter() {
        ChallengesAdapter adapter = new ChallengesAdapter(challengesTempList, getContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeData();
        initializeAdapter();
    }

}
