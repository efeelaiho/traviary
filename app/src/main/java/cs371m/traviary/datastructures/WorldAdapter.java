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
 * Created by jhl2298 on 4/28/2016.
 */
public class WorldAdapter extends RecyclerView.Adapter<WorldAdapter.WorldViewHolder> {

    public static class WorldViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView countryName;
        ImageView countryImage;
        ImageView countryCheck;

        public WorldViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.country_cv);
            countryName = (TextView)itemView.findViewById(R.id.country_name);
            countryImage = (ImageView)itemView.findViewById(R.id.country_photo);
            countryCheck = (ImageView)itemView.findViewById(R.id.country_check);}}

    List<Country> countries;
    Context context;
    int checkedResId;
    int uncheckedResId;

    public WorldAdapter(List<Country> countries, Context context) {
        this.countries = countries;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public WorldViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.country_cardview, viewGroup, false);
        WorldViewHolder worldViewHolder = new WorldViewHolder(v);
        return worldViewHolder;
    }

    @Override
    public void onBindViewHolder(WorldViewHolder worldViewHolder, int i) {
        Resources resource = context.getResources();
        worldViewHolder.countryName.setText(countries.get(i).name);
        int imageId = countries.get(i).photoId;
        worldViewHolder.countryImage.setImageResource(imageId);

        checkedResId = resource.getIdentifier("checked", "drawable", context.getPackageName());
        uncheckedResId = resource.getIdentifier("unchecked", "drawable", context.getPackageName());

        /* adding checkbox images */

        if (countries.get(i).visited) {
            worldViewHolder.countryCheck.setImageResource(checkedResId);
        } else {
            worldViewHolder.countryCheck.setImageResource(uncheckedResId);
        }
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

}
