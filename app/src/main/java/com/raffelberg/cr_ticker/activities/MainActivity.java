package com.raffelberg.cr_ticker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;


import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.raffelberg.cr_ticker.profiles.TickerEditor;
import com.raffelberg.cr_ticker.persistence.DBOperations;
import com.raffelberg.cr_ticker.persistence.Match;
import com.raffelberg.cr_ticker.ui.MatchViewModel;
import com.raffelberg.cr_ticker.ui.MatchViewModelFactory;
import com.raffelberg.cr_ticker.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    private TextView team1_name;
    private TextView team2_name;

    private ImageView team1_logo;
    private ImageView team2_logo;

    private TextView team1_score;
    private TextView team2_score;

    private TextView place_View;
    private TextView status_View;

    private MatchViewModel matchViewModel;

    DatabaseReference myRef;

    DBOperations dbOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef= database.getReference();

        ConstraintLayout mLayout = findViewById(R.id.clickLayout);

        drawerLayout = findViewById(R.id.drawerLayout);

        team1_name = findViewById(R.id.team1_name);
        team2_name = findViewById(R.id.team2_name);

        team1_logo = findViewById(R.id.team1_Logo);
        team2_logo = findViewById(R.id.team2_Logo);

        team1_score = findViewById(R.id.team1_Score);
        team2_score = findViewById(R.id.team2_Score);

        place_View = findViewById(R.id.place_View);
        status_View = findViewById(R.id.status_View);

        MatchViewModelFactory factory= new MatchViewModelFactory(this.getApplication());
        matchViewModel = new ViewModelProvider(this,factory).get(MatchViewModel.class);

        dbOperations= new DBOperations(getApplicationContext());

        FirebaseMessaging.getInstance().subscribeToTopic("cr-ticker");

        createNotificationChannel();

        //update local database when matchData changes
       myRef.child("Match").child("currentMatch").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Match match = snapshot.getValue(Match.class);
                if(match != null) {
                    dbOperations.addMatchRoom(match, matchViewModel, MainActivity.this);
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

               place_View.setText(match.getPlace());
               if(match.getState().equals("scheduled")){
                   status_View.setText(match.getDate());
               }else{
                   status_View.setText(match.getState());
               }
           }
       });


        File logo1 = new File(getFilesDir(),"logo1.jpg");
        File logo2 = new File(getFilesDir(),"logo2.jpg");

        //load logo from storage for team 1 into imageView, download from cloud if storage empty
        if(logo1.length() != 0) {
            Glide.with(this)
                    .load(logo1)
                    .circleCrop()
                    .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                    .into(team1_logo);
        }else {
            StorageReference storage = FirebaseStorage.getInstance().getReference().child("logo1.jpg");
            dbOperations.saveLogo("logo1.jpg",storage);
            Glide.with(this)
                    .load(storage)
                    .circleCrop()
                    .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                    .into(team1_logo);
        }


        //load logo from storage for team 2 into imageView, download from cloud if storage empty
        if(logo2.length() != 0){
            Glide.with(this)
                    .load(logo2)
                    .circleCrop()
                    .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                    .into(team2_logo);
        }else {
            StorageReference storage = FirebaseStorage.getInstance().getReference().child("logo2.jpg");
            dbOperations.saveLogo("logo2.jpg",storage);
            Glide.with(this)
                    .load(storage)
                    .circleCrop()
                    .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                    .into(team2_logo);

        }


        final Intent tickerLogIntent = new Intent(this, TickerLog.class);

        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(tickerLogIntent);
            }
        });

        //navigation drawer
        findViewById(R.id.menuIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        //refresh button
        findViewById(R.id.refreshIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
            }
        });

    }


    /**
     * log user out when mainActivity is destroyed which means app is exited
     */
    @Override
    protected void onDestroy() {
        TickerEditor.getInstance().setLogged_in(false);

        super.onDestroy();

    }



    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Nachrichten";
            String description = "Tickerereignisse";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("tickerChannel", name, importance);
            channel.setDescription(description);


            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * detects click on menuItems and redirects user
     * @param menuItem : menuItem clicked
     * @return -true for successful selection
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        TickerEditor user = TickerEditor.getInstance();

        switch (menuItem.getItemId()) {
            case (R.id.addMatch_Button):
                if (user.getLogged_in()) {
                    Intent addMatchIntent = new Intent(this, AddMatchActivity.class);
                    startActivity(addMatchIntent);
                } else {
                    Intent logInIntent = new Intent(this, LogInActivity.class);
                    logInIntent.putExtra("target", "addMatch");
                    startActivity(logInIntent);
                }
                break;

            case(R.id.manageMatch_Button):
                if (user.getLogged_in()) {
                    Intent manageMatchIntent = new Intent(this, manageMatchActivity.class);
                    startActivity(manageMatchIntent);
                } else {
                    Intent logInIntent = new Intent(this, LogInActivity.class);
                    logInIntent.putExtra("target", "manageMatch");
                    startActivity(logInIntent);
                }
                break;

            case (R.id.log_Button):
                Intent toLogIntent = new Intent(this, TickerLog.class);
                startActivity(toLogIntent);
                break;

            case (R.id.settings_Button):
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * override onBackPressed so it just closes the drawer when it's opened instead of the whole activity
     */
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }


}