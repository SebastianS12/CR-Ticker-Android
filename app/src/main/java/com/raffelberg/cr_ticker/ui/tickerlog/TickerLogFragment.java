package com.raffelberg.cr_ticker.ui.tickerlog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raffelberg.cr_ticker.R;
import com.raffelberg.cr_ticker.databinding.FragmentHomeBinding;
import com.raffelberg.cr_ticker.databinding.FragmentTickerLogBinding;
import com.raffelberg.cr_ticker.persistence.Match;
import com.raffelberg.cr_ticker.persistence.MatchViewModel;
import com.raffelberg.cr_ticker.persistence.MatchViewModelFactory;


public class TickerLogFragment extends Fragment {

    private FragmentTickerLogBinding binding;
    private TextView team1Name;
    private TextView team2Name;
    private TextView team1Score;
    private TextView team2Score;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTickerLogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        team1Name = binding.tickerLogTeamName1;
        team2Name = binding.tickerLogTeamName2;
        team1Score = binding.tickerLogTeam1Score;
        team2Score = binding.tickerLogTeam2Score;

        RecyclerView recyclerView = binding.tickerLogRecyclerView;
        final TickerLogAdapter adapter = new TickerLogAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        String teamID = getArguments().getString("teamID");
        MatchViewModelFactory matchViewModelFactory = new MatchViewModelFactory(requireActivity().getApplication());
        MatchViewModel viewModel = (MatchViewModel) matchViewModelFactory.create(MatchViewModel.class);

        viewModel.getMatch(teamID).observe(getViewLifecycleOwner(), match -> {
            updateUI(match);
            adapter.setLogList(match.getLogs());
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        });

        return root;
    }

    private void updateUI(Match match){
        if(match != null) {
            team1Name.setText(match.getTeam1().getTeamName());
            team2Name.setText(match.getTeam2().getTeamName());
            team1Score.setText(match.getTeam1().getTeamScore());
            team2Score.setText(match.getTeam2().getTeamScore());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}