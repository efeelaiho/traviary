package cs371m.traviary;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Button;


/**
 * Created by efosaelaiho on 3/25/16.
 */
public class Home extends Fragment {

    private Button mLocationButton;
    private Button mAttractionButton;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home,container,false);
        mLocationButton = (Button)v.findViewById(R.id.location_button);
        mAttractionButton = (Button)v.findViewById(R.id.attraction_button);

        mLocationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // go to MapsActivity
            }
        });

        mAttractionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                // I guess it will launch Google places API
            }
        });
        return v;
    }
}
