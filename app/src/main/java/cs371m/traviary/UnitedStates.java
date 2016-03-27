package cs371m.traviary;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.TextView;

/**
 * Created by efosaelaiho on 3/25/16.
 */
public class UnitedStates extends Fragment {
    

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.usa,container,false);
        TextView textView = (TextView)v.findViewById(R.id.rex);
        textView.setText("Americccccaaaa!!!!!");

        return v;
    }
}
