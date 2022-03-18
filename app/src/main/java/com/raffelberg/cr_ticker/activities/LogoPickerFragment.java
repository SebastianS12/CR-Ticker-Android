package com.raffelberg.cr_ticker.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.raffelberg.cr_ticker.LogoPickListener;
import com.raffelberg.cr_ticker.R;
import com.raffelberg.cr_ticker.ui.logAdapter;
import com.raffelberg.cr_ticker.ui.logoPickerAdapter;


public class LogoPickerFragment extends Fragment implements LogoPickListener {

    int team;

    public LogoPickerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logo_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @org.jetbrains.annotations.NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        team = requireArguments().getInt("team");

        RecyclerView recyclerView = view.findViewById(R.id.logo_RecyclerView);
        final logoPickerAdapter adapter = new logoPickerAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        Button cancelButton = view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v -> {
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