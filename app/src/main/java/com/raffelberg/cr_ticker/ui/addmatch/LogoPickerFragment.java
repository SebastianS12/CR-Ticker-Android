package com.raffelberg.cr_ticker.ui.addmatch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.raffelberg.cr_ticker.LogoPickListener;
import com.raffelberg.cr_ticker.R;
import com.raffelberg.cr_ticker.databinding.FragmentAddmatchBinding;
import com.raffelberg.cr_ticker.databinding.FragmentLogoPickerBinding;

import java.util.List;

public class LogoPickerFragment extends Fragment implements LogoPickListener {

    private FragmentLogoPickerBinding binding;
    private int team;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLogoPickerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        team = requireArguments().getInt("team", 1);
        List<String> logoPaths = requireArguments().getStringArrayList("logoPaths");

        RecyclerView recyclerView = binding.logoPickerRecyclerView;
        final LogoPickerAdapter adapter = new LogoPickerAdapter(getContext(), this, logoPaths);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        Button cancelButton = binding.logoPickerCancelButton;
        cancelButton.setOnClickListener(v->{
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });
    }

    @Override
    public void addLogo(String logoID) {
        Bundle result = new Bundle();
        result.putInt("team", team);
        result.putString("logoID", logoID);
        getParentFragmentManager().setFragmentResult("logoPick", result);
        getParentFragmentManager().beginTransaction().remove(this).commit();
    }
}