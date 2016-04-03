package cs371m.traviary.datastructures;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cs371m.traviary.R;

/**
 * Created by Jong Hoon Lim on 3/29/2016.
 */
public class UnitedStatesAdapter extends RecyclerView.Adapter<UnitedStatesAdapter.StateViewHolder> {

    public static class StateViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView stateName;
        ImageView stateImage;
        ImageView stateCheck;

        public StateViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.state_cv);
            stateName = (TextView)itemView.findViewById(R.id.state_name);
            stateImage = (ImageView)itemView.findViewById(R.id.state_photo);
            stateCheck = (ImageView)itemView.findViewById(R.id.state_check);
        }

    }

    List<State> states;
    Context context;
    int checkedResId;
    int uncheckedResId;

    public UnitedStatesAdapter(List<State> states, Context context) {
        this.states = states;
        this.context = context;
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
        Resources resource = context.getResources();
        stateViewHolder.stateName.setText(states.get(i).name);
        int imageId = states.get(i).photoId;
        stateViewHolder.stateImage.setImageResource(imageId);

        checkedResId = resource.getIdentifier("checked", "drawable", context.getPackageName());
        uncheckedResId = resource.getIdentifier("unchecked", "drawable", context.getPackageName());

        /* adding checkbox images */

        if (states.get(i).visited) {
            stateViewHolder.stateCheck.setImageResource(checkedResId);
        } else {
            stateViewHolder.stateCheck.setImageResource(uncheckedResId);
        }
    }

    @Override
    public int getItemCount() {
        return states.size();
    }

}
