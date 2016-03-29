package cs371m.traviary.datastructures;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cs371m.traviary.R;

/**
 * Created by Jong Hoon Lim on 3/29/2016.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.StateViewHolder> {

    public static class StateViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView stateName;

        public StateViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.cv);
            stateName = (TextView)itemView.findViewById(R.id.state_name);
        }

    }

    List<State> states;

    public CustomAdapter(List<State> states) {
        this.states = states;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public StateViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.state_cardview, viewGroup, false);
        StateViewHolder stateViewHolder = new StateViewHolder(v);
        return stateViewHolder;
    }

    @Override
    public void onBindViewHolder(StateViewHolder stateViewHolder, int i) {
        stateViewHolder.stateName.setText(states.get(i).name);
    }

    @Override
    public int getItemCount() {
        return states.size();
    }

}
