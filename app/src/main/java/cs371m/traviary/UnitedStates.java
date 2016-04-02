package cs371m.traviary;


import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import cs371m.traviary.datastructures.UnitedStatesAdapter;
import cs371m.traviary.datastructures.State;

/**
 * Created by efosaelaiho on 3/25/16.
 */
public class UnitedStates extends Fragment {

    private TreeMap<String,Boolean> states;

    // ***** remove when TreeMap and database is implemented *****
    private List<State> stateTempList;

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.usa,container,false);
        MainActivity mainActivity = (MainActivity)getActivity();
        states = mainActivity.getStates();

        recyclerView = (RecyclerView)v.findViewById(R.id.usa_rv);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                LocationViewer fragment = new LocationViewer();
                fragment.setLocationBeingViewed(getResources().getStringArray(R.array.state_names)[position]);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fl, fragment)
                        .commit();
            }
        });

        initializeData();
        initializeAdapter();

        return v;
    }

    // ***** remove when TreeMap and database is implemented *****
    // ***** DUMMY DATA INITALIZATION *****@
    private void initializeData() {
        stateTempList = new ArrayList<>();
        Resources resource = getResources();
        String[] stateNames = resource.getStringArray(R.array.state_names);
        String originalState;
        for (String state : stateNames) {
            originalState = state;
            state = state.replaceAll("\\s+",""); // remove all white spaces
            String mDrawableName = "_" + state.toLowerCase();
            int resId = resource.getIdentifier(mDrawableName, "drawable", getActivity().getPackageName());
            boolean visited = false;
            int checked_resId = resource.getIdentifier("checked", "drawable", getActivity().getPackageName());
            int unchecked_resId = resource.getIdentifier("unchecked", "drawable", getActivity().getPackageName());
            stateTempList.add(new State(originalState, visited, resId, checked_resId, unchecked_resId));
        }
    }

    private void initializeAdapter(){
        UnitedStatesAdapter adapter = new UnitedStatesAdapter(stateTempList);
        recyclerView.setAdapter(adapter);
    }

}
