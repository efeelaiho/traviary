package cs371m.traviary;


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
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import cs371m.traviary.datastructures.CustomAdapter;
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.usa,container,false);
        MainActivity mainActivity = (MainActivity)getActivity();
        states = mainActivity.getStates();

        recyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        initializeData();
        initializeAdapter();

        return v;
    }

    // ***** remove when TreeMap and database is implemented *****
    private void initializeData() {
        stateTempList = new ArrayList<>();
        stateTempList.add(new State("Alaska"));
        stateTempList.add(new State("Colorado"));
        stateTempList.add(new State("Florida"));
        stateTempList.add(new State("Hawaii"));
        stateTempList.add(new State("Massachusetts"));
        stateTempList.add(new State("Nevada"));
        stateTempList.add(new State("New York"));
        stateTempList.add(new State("Texas"));
    }

    private void initializeAdapter(){
        CustomAdapter adapter = new CustomAdapter(stateTempList);
        recyclerView.setAdapter(adapter);
    }

}
