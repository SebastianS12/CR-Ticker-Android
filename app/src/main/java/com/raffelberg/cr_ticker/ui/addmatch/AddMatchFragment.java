package com.raffelberg.cr_ticker.ui.addmatch;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.raffelberg.cr_ticker.ImageOperations.ImageLoader;
import com.raffelberg.cr_ticker.R;
import com.raffelberg.cr_ticker.databinding.FragmentAddmatchBinding;
import com.raffelberg.cr_ticker.persistence.Match;
import com.raffelberg.cr_ticker.persistence.Team;

import java.util.ArrayList;
import java.util.List;

public class AddMatchFragment extends Fragment {

    private FragmentAddmatchBinding binding;
    private TextView placeTextView;
    private TextView dateTextView;
    private TextView teamName1TextView;
    private ImageView logo1ImageView;
    private TextView teamName2TextView;
    private ImageView logo2ImageView;
    private Button addButton;

    private List<String> logoPaths;

    public AddMatchFragment(){
        logoPaths = new ArrayList<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("logoPick", this, (requestKey, bundle) -> {
            String logoPath = bundle.getString("logoPath");
            int team = bundle.getInt("team");

            ImageLoader imageLoader = new ImageLoader();
            if(team == 1)
                imageLoader.loadLogo(logo1ImageView, logoPath, getContext());
            if(team == 2)
                imageLoader.loadLogo(logo2ImageView, logoPath, getContext());
        });

    }

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

        getLogoUrls();

        //select team logos
        logo1ImageView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("team", 1);
            bundle.putStringArrayList("logoPaths", (ArrayList<String>) logoPaths);
            getParentFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.addMatch_logoPickerContainer, LogoPickerFragment.class, bundle)
                    .commit();
        });
        logo2ImageView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("team", 2);
            bundle.putStringArrayList("logoPaths", (ArrayList<String>) logoPaths);
            getParentFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.addMatch_logoPickerContainer, LogoPickerFragment.class, bundle)
                    .commit();
        });

        addButton.setOnClickListener(view -> {
            uploadMatch();
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FirebaseAuth.getInstance().signOut();
        binding = null;
    }

    private void getLogoUrls(){
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://cr-ticker-herren-logos");
        StorageReference ref = storage.getReference();
        ref.listAll().addOnSuccessListener(listResult -> {
            Log.i("test", "connected");
            for(StorageReference prefix : listResult.getItems()){
                logoPaths.add(prefix.getName());
            }
        });
    }

    private void uploadMatch(){
        String id = getArguments().getString("id");
        String place = placeTextView.getText().toString();
        String date = dateTextView.getText().toString();
        String teamName1 = teamName1TextView.getText().toString();
        String teamName2 = teamName2TextView.getText().toString();

        Match newMatch = new Match(id, place, date, new Team(teamName1), new Team(teamName2));

        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
        dbReference.child("Matches").setValue(newMatch);

        ImageLoader imageLoader = new ImageLoader();
        imageLoader.uploadLogoFromImageView("logoRebuild1", logo1ImageView);
        imageLoader.uploadLogoFromImageView("logoRebuild2", logo2ImageView);
    }
}