package com.raffelberg.cr_ticker.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raffelberg.cr_ticker.R;
import com.raffelberg.cr_ticker.databinding.FragmentHomeBinding;
import com.raffelberg.cr_ticker.persistence.Match;
import com.raffelberg.cr_ticker.persistence.MatchViewModel;
import com.raffelberg.cr_ticker.persistence.MatchViewModelFactory;

import java.util.HashMap;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = binding.homeRecyclerView;
        adapter = new HomeAdapter(getResources().getStringArray(R.array.teamIDS));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        HashMap<String, Match> matchList = getMatches();
        adapter.setMatchMap(matchList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public HashMap<String, Match> getMatches(){
        String[] teamIDs = getResources().getStringArray(R.array.teamIDS);
        HashMap<String, Match> matchList = new HashMap<>();

        MatchViewModelFactory matchViewModelFactory = new MatchViewModelFactory(requireActivity().getApplication());
        MatchViewModel matchViewModel = matchViewModelFactory.create(MatchViewModel.class);
        for(int i = 0; i < teamIDs.length; i++){
            int index = i;
            matchViewModel.getMatch(teamIDs[index]).observe(getViewLifecycleOwner(), match -> {
                if(match != null){
                    matchList.put(match.getId(), match);
                    adapter.notifyItemChanged(index);
                }
            });
        }
        return matchList;
    }
}