package com.raffelberg.cr_ticker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.raffelberg.cr_ticker.persistence.DBOperations;
import com.raffelberg.cr_ticker.persistence.Match;
import com.raffelberg.cr_ticker.ui.MatchViewModel;
import com.raffelberg.cr_ticker.ui.MatchViewModelFactory;
import com.raffelberg.cr_ticker.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.raffelberg.cr_ticker.ui.logAdapter;

import java.util.Objects;

public class manageMatchActivity extends AppCompatActivity {

    private Button startMatch_Button;
    private Button endMatch_Button;

    private TextView team1_name;
    private TextView team2_name;

    private TextView team1_score;
    private TextView team2_score;

    private EditText log_input;

    private MatchViewModel matchViewModel;

    private DatabaseReference myRef;
    private DBOperations mDBOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_match);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef= database.getReference();

        startMatch_Button = findViewById(R.id.startMatch_Button);
        endMatch_Button = findViewById(R.id.endMatch_Button);

        team1_name = findViewById(R.id.team1_name);
        team2_name = findViewById(R.id.team2_name);

        team1_score = findViewById(R.id.team1_Score);
        team2_score = findViewById(R.id.team2_Score);

        Button team1_score_increase = findViewById(R.id.team1_score_increase);
        Button team1_score_decrease = findViewById(R.id.team1_score_decrease);

        Button team2_score_increase = findViewById(R.id.team2_score_increase);
        Button team2_score_decrease = findViewById(R.id.team2_score_decrease);

        log_input = findViewById(R.id.log_textInput);

        ImageButton log_submit = findViewById(R.id.log_submitButton);

        mDBOperations = new DBOperations(getApplicationContext());

        MatchViewModelFactory factory= new MatchViewModelFactory(this.getApplication());
        matchViewModel = new ViewModelProvider(this,factory).get(MatchViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.log_overview);
        final logAdapter adapter = new logAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //update local database when matchData changes
        myRef.child("Match").child("currentMatch").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Match match = snapshot.getValue(Match.class);
                if(match != null) {
                    matchViewModel.updateMatch(match);
                    adapter.setLogList(match.getLogs());
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //set UI contents
        matchViewModel.getCurrentMatch().observe(this,match -> {
            if (match != null) {
                team1_name.setText(match.getTeam1().getTeam_name());
                team2_name.setText(match.getTeam2().getTeam_name());

                team1_score.setText(match.getTeam1().getTeam_score());
                team2_score.setText(match.getTeam2().getTeam_score());

                if(match.getState().equals("scheduled")){
                    endMatch_Button.setEnabled(false);
                    startMatch_Button.setEnabled(true);

                    startMatch_Button.setVisibility(View.VISIBLE);
                    endMatch_Button.setVisibility(View.INVISIBLE);
                }else if(match.getState().equals("live")){
                    startMatch_Button.setEnabled(false);
                    endMatch_Button.setEnabled(true);

                    endMatch_Button.setVisibility(View.VISIBLE);
                    startMatch_Button.setVisibility(View.INVISIBLE);

                }else{
                    endMatch_Button.setVisibility(View.VISIBLE);
                    startMatch_Button.setVisibility(View.VISIBLE);
                }
            }else{//no match added yet, deny any edits
                team1_score_decrease.setEnabled(false);
                team1_score_increase.setEnabled(false);
                team2_score_decrease.setEnabled(false);
                team2_score_increase.setEnabled(false);

                startMatch_Button.setEnabled(false);
                endMatch_Button.setEnabled(false);

                log_submit.setEnabled(false);

                findViewById(R.id.noMatch_info).setVisibility(View.VISIBLE);
            }
        });

        team1_score_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDBOperations.increaseScore(team1_score,"team1");
                String log = "Tor für "+team1_name.getText().toString();
                mDBOperations.addLog(log,matchViewModel,manageMatchActivity.this);
                sendKeyMessage(log);
            }
        });

        team2_score_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDBOperations.increaseScore(team2_score,"team2");
                String log = "Tor für "+team2_name.getText().toString();
                mDBOperations.addLog(log,matchViewModel,manageMatchActivity.this);
                sendKeyMessage(log);
            }

        });

        team1_score_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDBOperations.decreaseScore(team1_score,"team1");
            }
        });

        team2_score_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDBOperations.decreaseScore(team2_score,"team2");
            }
        });

        //start Match
        startMatch_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDBOperations.setStatus("live");
                String log = getString(R.string.startMatch_log);
                mDBOperations.addLog(log,matchViewModel,manageMatchActivity.this);
                sendKeyMessage(log);

                v.setVisibility(View.INVISIBLE);
                endMatch_Button.setVisibility(View.VISIBLE);
            }
        });

        //end Match
        endMatch_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDBOperations.setStatus("Beendet");
                String log = getString(R.string.endMatch_log);
                mDBOperations.addLog(log,matchViewModel,manageMatchActivity.this);
                sendKeyMessage(log);

                startMatch_Button.setVisibility(View.VISIBLE);
            }
        });

        //send log
        log_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDBOperations.addLog(log_input.getText().toString(),matchViewModel,manageMatchActivity.this);
                sendMessage(log_input.getText().toString());

                //clear input and hide keyboard
                log_input.setText("");
                log_input.onEditorAction(EditorInfo.IME_ACTION_DONE);

            }
        });
    }

    /**
     * sends message with normal priority to users
     * @param message: text to be delivered
     */
    public void sendMessage(String message){
        FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
        mFunctions.getHttpsCallable("sendMessage")
                .call(message)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task){

                        return (String) Objects.requireNonNull(task.getResult()).getData();
                    }
                });
    }

    /**
     * sends message with high priority to users
     * @param message: text to be delivered
     */
    public void sendKeyMessage(String message){
        FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
        mFunctions.getHttpsCallable("sendKeyMessage")
                .call(message)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task){

                        return (String) Objects.requireNonNull(task.getResult()).getData();
                    }
                });
    }


}