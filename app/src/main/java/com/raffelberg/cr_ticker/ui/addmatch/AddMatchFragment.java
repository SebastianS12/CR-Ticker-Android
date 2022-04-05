package com.raffelberg.cr_ticker.ui.addmatch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.raffelberg.cr_ticker.databinding.FragmentAddmatchBinding;

public class AddMatchFragment extends Fragment {

    private FragmentAddmatchBinding binding;
    private TextView placeTextView;
    private TextView dateTextView;
    private TextView teamName1TextView;
    private ImageView logo1ImageView;
    private TextView teamName2TextView;
    private ImageView logo2ImageView;
    private Button addButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddmatchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        placeTextView = binding.addMatchPlaceInput;
        dateTextView = binding.addMatchDateInput;
        teamName1TextView = binding.addMatchTeam1NameInput;
        logo1ImageView = binding.addMatchTeam1Logo;
        teamName2TextView = binding.addMatchTeam2NameInput;
        logo2ImageView = binding.addMatchTeam2Logo;
        addButton = binding.addMatchAddButton;

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}