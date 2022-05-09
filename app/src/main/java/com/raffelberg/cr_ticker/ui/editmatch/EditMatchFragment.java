package com.raffelberg.cr_ticker.ui.editmatch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.raffelberg.cr_ticker.R;
import com.raffelberg.cr_ticker.databinding.FragmentEditmatchBinding;
import com.raffelberg.cr_ticker.persistence.Match;
import com.raffelberg.cr_ticker.persistence.MatchViewModel;
import com.raffelberg.cr_ticker.persistence.MatchViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class EditMatchFragment extends Fragment {

    private FragmentEditmatchBinding binding;

    private TextView team1NameLabel;
    private TextView team2NameLabel;
    private TextView team1ScoreLabel;
    private TextView team2ScoreLabel;
    private Button team1ScoreIncreaseButton;
    private Button team2ScoreIncreaseButton;
    private Button team1ScoreDecreaseButton;
    private Button team2ScoreDecreaseButton;
    private EditText logInput;
    private Button logSendButton;

    private Match currentMatch;
    private DatabaseReference mDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEditmatchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        team1NameLabel = binding.editMatchTeamName1;
        team2NameLabel = binding.editMatchTeamName2;
        team1ScoreLabel = binding.editMatchTeam1Score;
        team2ScoreLabel = binding.editMatchTeam2Score;
        team1ScoreIncreaseButton = binding.editMatchTeam1ScoreIncrease;
        team2ScoreIncreaseButton = binding.editMatchTeam2ScoreIncrease;
        team1ScoreDecreaseButton = binding.editMatchTeam1ScoreDecrease;
        team2ScoreDecreaseButton = binding.editMatchTeam2ScoreDecrease;
        logInput = binding.editMatchLogInput;
        logSendButton = binding.editMatchSendButton;

        mDatabase = FirebaseDatabase.getInstance().getReference();

        addMatchListener();

        logSendButton.setOnClickListener(view -> {
            if(!logInput.getText().toString().equals("")){
                submitLog(logInput.getText().toString());
            }
        });

        team1ScoreIncreaseButton.setOnClickListener(view -> {
            increaseScore(team1ScoreLabel, "team1", currentMatch.getTeam1().getTeamName());
        });

        team2ScoreIncreaseButton.setOnClickListener(view -> {
            increaseScore(team2ScoreLabel, "team2", currentMatch.getTeam2().getTeamName());
        });

        team1ScoreDecreaseButton.setOnClickListener(view -> {
            decreaseScore(team1ScoreLabel, "team1");
        });

        team2ScoreDecreaseButton.setOnClickListener(view -> {
            decreaseScore(team2ScoreLabel, "team2");
        });

        return root;
    }

    public void addMatchListener(){
        MatchViewModelFactory matchViewModelFactory = new MatchViewModelFactory(requireActivity().getApplication());
        MatchViewModel matchViewModel = matchViewModelFactory.create(MatchViewModel.class);

        String id = getArguments().getString("id");
        matchViewModel.getMatch(id).observe(getViewLifecycleOwner(), match -> {
            if(match != null){
                currentMatch = match;

                team1NameLabel.setText(match.getTeam1().getTeamName());
                team2NameLabel.setText(match.getTeam2().getTeamName());
                team1ScoreLabel.setText(match.getTeam1().getTeamScore());
                team2ScoreLabel.setText(match.getTeam2().getTeamScore());
            }
        });
    }

    private List<String> addLog(String log){
        List<String> logs = currentMatch.getLogs();
        if(logs == null) {
            logs = new ArrayList<>();
        }
        logs.add(log);

        return logs;
    }

    public void submitLog(String log){
        List<String> logs = addLog(log);
        String id = requireArguments().getString("id");

        mDatabase.child("Matches").child(id).child("logs").setValue(logs).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //send firebase cloud message
                //sendNotification(log, teamID[0]);
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(requireContext(), getString(R.string.failure_message), Toast.LENGTH_LONG).show();
        });
    }

    public void increaseScore(TextView viewScore, String team, String teamName){
        String score = String.valueOf(Integer.parseInt(viewScore.getText().toString())+1);
        //viewScore.setText(score);
        String goalLog = getString(R.string.goal_notification) + " " + teamName;

        List<String> logs = addLog(goalLog);
        String id = requireArguments().getString("id");
        mDatabase.child("Matches").child(id).child(team).child("teamScore").setValue(score);
        mDatabase.child("Matches").child(id).child("logs").setValue(logs).addOnSuccessListener(unused -> {
            //sendImportantNotification(goalLog, teamID[0]);
        }).addOnFailureListener(e -> {
            Toast.makeText(requireContext(), getString(R.string.failure_message), Toast.LENGTH_LONG).show();
        });
    }

    public void decreaseScore(TextView viewScore, String team){
        if(Integer.parseInt(viewScore.getText().toString())>0) {
            String score = String.valueOf(Integer.parseInt(viewScore.getText().toString()) - 1);
            viewScore.setText(score);

            String id = requireArguments().getString("id");
            mDatabase.child("Matches").child(id).child(team).child("teamScore").setValue(score);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}