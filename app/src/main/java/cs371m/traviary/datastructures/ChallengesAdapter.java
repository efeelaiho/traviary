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
 * Created by Jong Hoon Lim on 4/1/2016.
 */
public class ChallengesAdapter extends RecyclerView.Adapter<ChallengesAdapter.StateViewHolder> {

    public static class StateViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView challengeName;

        public StateViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.challenge_cv);
            challengeName = (TextView)itemView.findViewById(R.id.challenge_name);
        }

    }

    List<Challenge> challenges;

    public ChallengesAdapter(List<Challenge> challenges) {
        this.challenges = challenges;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public StateViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.challenge_cardview, viewGroup, false);
        StateViewHolder stateViewHolder = new StateViewHolder(v);
        return stateViewHolder;
    }

    @Override
    public void onBindViewHolder(StateViewHolder stateViewHolder, int i) {
        stateViewHolder.challengeName.setText(challenges.get(i).name);
    }

    @Override
    public int getItemCount() {
        return challenges.size();
    }
}
