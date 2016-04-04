package cs371m.traviary;


import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import cs371m.traviary.temp.SQLiteHelper;

/**
 * Created by efosaelaiho on 3/25/16.
 */
public class UnitedStates extends Fragment {

    // ***** remove when TreeMap and database is implemented *****
    private List<State> stateTempList;

    private RecyclerView recyclerView;

    private Resources resource;
    private String[] stateNames;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.usa,container,false);

        resource = getResources();
        stateNames = resource.getStringArray(R.array.state_names);

        recyclerView = (RecyclerView)v.findViewById(R.id.usa_rv);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent stateIntent = new Intent(getContext(), StateActivity.class);
                stateIntent.putExtra("name", stateNames[position]);
                startActivity(stateIntent);
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
        String originalState;
        for (String state : stateNames) {
            originalState = state;
            state = state.replaceAll("\\s+",""); // remove all white spaces
            String mDrawableName = "_" + state.toLowerCase();
            int resId = resource.getIdentifier(mDrawableName, "drawable", getActivity().getPackageName());
            boolean visited = false;
            stateTempList.add(new State(originalState, visited, resId));
        }
        SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext()); // ERROR CHECKING
        System.out.println(sqLiteHelper.getAllStatesRecords()); // DEBUG PRINT ALL RECORDS

    }

    public void p() {
        Log.d("d", "onH");
    }

    private void initializeAdapter(){
        UnitedStatesAdapter adapter = new UnitedStatesAdapter(stateTempList, getContext());
        recyclerView.setAdapter(adapter);
    }

}
