package com.raffelberg.cr_ticker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.raffelberg.cr_ticker.persistence.Match;
import com.raffelberg.cr_ticker.ui.MatchViewModel;
import com.raffelberg.cr_ticker.ui.MatchViewModelFactory;
import com.raffelberg.cr_ticker.R;
import com.raffelberg.cr_ticker.ui.logAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TickerLog extends AppCompatActivity {

    private TextView team1_name;
    private TextView team2_name;

    private TextView team1_score;
    private TextView team2_score;

    private MatchViewModel matchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticker_log);

        team1_name = findViewById(R.id.team1_name);
        team2_name = findViewById(R.id.team2_name);

        team1_score = findViewById(R.id.team1_Score);
        team2_score = findViewById(R.id.team2_Score);

        RecyclerView recyclerView = findViewById(R.id.log_RecyclerView);
        final logAdapter adapter = new logAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();


        MatchViewModelFactory factory= new MatchViewModelFactory(this.getApplication());
        matchViewModel = new ViewModelProvider(this,factory).get(MatchViewModel.class);

        //update local database and UI when machData changes, move recyclerView to most recent entry
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
            if(match != null) {
                team1_name.setText(match.getTeam1().getTeam_name());
                team2_name.setText(match.getTeam2().getTeam_name());

                team1_score.setText(match.getTeam1().getTeam_score());
                team2_score.setText(match.getTeam2().getTeam_score());
            }
        });

        //back arrow action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        return true;
    }

    /**
     * refreshes activity when refreshIcon is clicked
     * @param item -refreshIcon
     * @return -returns true for successful selection
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.refreshIcon) {
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
        }else{
            finish();
        }
        return true;
    }
}