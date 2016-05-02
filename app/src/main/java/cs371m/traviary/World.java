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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import cs371m.traviary.database.SQLiteHelper;
import cs371m.traviary.datastructures.Country;
import cs371m.traviary.datastructures.WorldAdapter;

/**
 * Created by efosaelaiho on 3/25/16.
 */
public class World extends Fragment {

    private List<Country> countryTempList;

    private RecyclerView recyclerView;

    private Resources resource;
    private String[] countryNames;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.world, container, false);
        resource = getResources();
        countryNames = resource.getStringArray(R.array.world_names);

        recyclerView = (RecyclerView)v.findViewById(R.id.world_rv);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        initializeData();
        initializeAdapter();

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent countryIntent = new Intent(getContext(), CountryActivity.class);
                countryIntent.putExtra("name", countryTempList.get(position).name);
                startActivity(countryIntent);
            }
        });

        return v;
    }

    private String normalizeCountryName(String country) {
        country = country.replaceAll("\\s+",""); // remove all white spaces
        country = country.replaceAll("-", "_");
        country = country.replaceAll("ô", "o");
        country = country.replaceAll("é", "e");
        country = country.replaceAll("ã", "a");
        country = country.replaceAll("í", "i");
        return country;
    }

    private void initializeData() {
        countryTempList = new ArrayList<>();
        SQLiteHelper db = new SQLiteHelper(getContext());
        HashSet<String> visitedCountries = db.getVisitedCountries(); // get all countries that user has visited
        String countryWithSpace;
        for (String country : countryNames) {
            countryWithSpace = country;
            country = normalizeCountryName(countryWithSpace);
            String mDrawableName = "_" + country.toLowerCase();
            int resId = resource.getIdentifier(mDrawableName, "drawable", getActivity().getPackageName());
            boolean visited = false;
            if (visitedCountries.contains(countryWithSpace)) { // check whether countries exists in HashSet
                visited = true;
            }
            countryTempList.add(new Country(countryWithSpace, visited, resId));
        }
        Collections.sort(countryTempList);
        db.close();
    }

    private void initializeAdapter(){
        WorldAdapter adapter = new WorldAdapter(countryTempList, getContext());
        recyclerView.setAdapter(adapter);
    }

    /*
     * Refresh the RecyclerView after resuming fragment (after logging location)
     */
    @Override
    public void onResume() {
        super.onResume();
        initializeData();
        initializeAdapter();
    }
}
