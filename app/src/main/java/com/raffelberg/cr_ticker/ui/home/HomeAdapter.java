package com.raffelberg.cr_ticker.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raffelberg.cr_ticker.ImageOperations.ImageLoader;
import com.raffelberg.cr_ticker.R;
import com.raffelberg.cr_ticker.persistence.Match;
import com.raffelberg.cr_ticker.persistence.MatchViewModel;
import com.raffelberg.cr_ticker.persistence.MatchViewModelFactory;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private Context context;
    private String[] teamIDList;
    private HashMap<String, Match> matchMap;

    public HomeAdapter(String[] teamIDList){
        matchMap = new HashMap<>();
        this.teamIDList = teamIDList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_item, parent, false);

        context = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Match currentMatch = matchMap.get(teamIDList[position]);

        holder.getTextViewPlace().setText(currentMatch.getPlace());
        holder.getTextViewDate().setText(currentMatch.getDate());
        holder.getTextViewTeamName1().setText(currentMatch.getTeam1().getTeamName());
        holder.getTextViewTeamScore1().setText(currentMatch.getTeam1().getTeamScore());
        holder.getTextViewTeamName2().setText(currentMatch.getTeam2().getTeamName());
        holder.getTextViewTeamScore2().setText(currentMatch.getTeam2().getTeamScore());

        ImageLoader imageLoader = new ImageLoader();
        imageLoader.loadLogo(holder.getImageViewLogo1(), teamIDList[position]+"/logoRebuild1", context, "gs://cr-ticker.appspot.com");
        imageLoader.loadLogo(holder.getImageViewLogo2(), teamIDList[position]+"/logoRebuild2", context, "gs://cr-ticker.appspot.com");
    }

    @Override
    public int getItemCount() {
            return matchMap.size();
    }

    public void setMatchMap(HashMap<String, Match> matchMap){
        this.matchMap = matchMap;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final View viewHolder;
        private final TextView textViewTeamName1;
        private final TextView textViewTeamName2;
        private final TextView textViewTeamScore1;
        private final TextView textViewTeamScore2;
        private final ImageView imageViewLogo1;
        private final ImageView imageViewLogo2;
        private final TextView textViewPlace;
        private final TextView textViewDate;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            viewHolder = view;
            textViewTeamName1 = (TextView) view.findViewById(R.id.home_teamName1);
            textViewTeamName2 = (TextView) view.findViewById(R.id.home_teamName2);
            textViewTeamScore1 = (TextView) view.findViewById(R.id.home_teamScore1);
            textViewTeamScore2 = (TextView) view.findViewById(R.id.home_teamScore2);
            imageViewLogo1 = (ImageView) view.findViewById(R.id.home_teamLogo1);
            imageViewLogo2 = (ImageView) view.findViewById(R.id.home_teamLogo2);
            textViewPlace = (TextView) view.findViewById(R.id.home_place);
            textViewDate = (TextView) view.findViewById(R.id.home_date);
        }

        public View getViewHolder(){ return viewHolder;}

        public TextView getTextViewTeamName1(){
            return textViewTeamName1;
        }

        public TextView getTextViewTeamName2(){
            return textViewTeamName2;
        }

        public TextView getTextViewTeamScore1(){
            return textViewTeamScore1;
        }

        public TextView getTextViewTeamScore2(){
            return textViewTeamScore2;
        }

        public ImageView getImageViewLogo1(){ return imageViewLogo1;}

        public ImageView getImageViewLogo2(){ return imageViewLogo2;}

        public TextView getTextViewPlace() { return textViewPlace;}

        public TextView getTextViewDate() { return textViewDate; }
    }
}
