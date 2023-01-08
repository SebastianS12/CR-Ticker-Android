package com.raffelberg.cr_ticker;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.raffelberg.cr_ticker.databinding.ActivityMainBinding;
import com.raffelberg.cr_ticker.persistence.Match;
import com.raffelberg.cr_ticker.persistence.MatchRepository;
import com.raffelberg.cr_ticker.persistence.MatchViewModel;
import com.raffelberg.cr_ticker.persistence.MatchViewModelFactory;

public class MainActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private MatchViewModel matchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MatchViewModelFactory factory= new MatchViewModelFactory(this.getApplication());
        matchViewModel = new ViewModelProvider(this,factory).get(MatchViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        mAppBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                .setOpenableLayout(drawer)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //add database listeners
        for(String id : getResources().getStringArray(R.array.teamIDS)){
            addDatabaseListener(id, this);
        }

        //setup subscriptions
        setUpSubscriptions();

        //refer to LogIn if needed
        mAuth = FirebaseAuth.getInstance();
        navController.addOnDestinationChangedListener((controller, destination, arguments)->{
            Bundle bundle = new Bundle();
            bundle.putInt("destination", destination.getId());
            if(destination.getId() == R.id.nav_addmatch && mAuth.getCurrentUser() == null)
                navController.navigate(R.id.action_nav_addmatch_to_logInFragment, bundle);
            if(destination.getId() == R.id.nav_editMatch && mAuth.getCurrentUser() == null)
                navController.navigate(R.id.action_nav_editMatch_to_logInFragment, bundle);
            drawer.close();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    private void addDatabaseListener(String id, LifecycleOwner lifecycleOwner){
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
        dbReference.child("Matches").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Match currentMatch = snapshot.getValue(Match.class);
                matchViewModel.updateMatch(currentMatch);
                matchViewModel.getMatch(currentMatch.getId()).observe(lifecycleOwner, match -> {
                    if(match == null){
                        matchViewModel.insertMatch(currentMatch);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("DatabaseError", error.getMessage().toString());
            }
        });
    }

    private void setUpSubscriptions(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();

        if(preferences.getBoolean("all_notifications_herren1", true)){
            //subscribe
            firebaseMessaging.subscribeToTopic("all_notifications_herren1");
        }
        if(preferences.getBoolean("important_notifications_herren1", true)){
            //subscribe
        }
        if(preferences.getBoolean("all_notifications_damen1", true)){
            //subscribe
        }
        if(preferences.getBoolean("important_notifications_damen1", true)){
            //subscribe
        }
    }
}