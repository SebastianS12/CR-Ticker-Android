package com.raffelberg.cr_ticker.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.raffelberg.cr_ticker.persistence.DBOperations;
import com.raffelberg.cr_ticker.persistence.Match;
import com.raffelberg.cr_ticker.ui.MatchViewModel;
import com.raffelberg.cr_ticker.ui.MatchViewModelFactory;
import com.raffelberg.cr_ticker.R;
import com.raffelberg.cr_ticker.persistence.Team;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

public class AddMatchActivity extends AppCompatActivity {

    private EditText editText_place;
    private EditText editText_date;
    private EditText team1_name;
    private EditText team2_name;

    private ImageView team1_logo;
    private ImageView team2_logo;
    private Drawable logoDrawable1;
    private Drawable logoDrawable2;

    private Button addButton;

    private Uri team1_logo_path;
    private Uri team2_logo_path;

    private FirebaseStorage storage;

    private MatchViewModel matchViewModel;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_match);

        editText_place = findViewById(R.id.editText_place);
        editText_date = findViewById(R.id.editText_date);
        team1_name = findViewById(R.id.editText_team1_name);
        team2_name = findViewById(R.id.editText_team2_name);

        team1_logo = findViewById(R.id.imageView_team1_logo);
        team2_logo = findViewById(R.id.imageView_team2_logo);

        addButton = findViewById(R.id.button_addMatch);

        //select team logos
        team1_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("team", 1);

                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.logoPickerContainer, LogoPickerFragment.class, bundle)
                        .commit();
            }
        });

        team2_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("team", 2);

                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.logoPickerContainer, LogoPickerFragment.class, bundle)
                        .commit();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean filledOut = true;
                if(editText_place.getText().toString().equals("")){
                    editText_place.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e50000")));
                    filledOut = false;
                }
                if(editText_date.getText().toString().equals("")){
                    editText_date.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e50000")));
                    filledOut = false;
                }
                if (team1_name.getText().toString().equals("")){
                    team1_name.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e50000")));
                    filledOut = false;
                }
                if(team2_name.getText().toString().equals("")){
                    team2_name.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e50000")));
                    filledOut = false;
                }
                if(filledOut){
                    uploadMatchData();
                    Toast.makeText(getApplicationContext(), "Match added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });

        storage = FirebaseStorage.getInstance();

        MatchViewModelFactory factory= new MatchViewModelFactory(this.getApplication());
        matchViewModel = new ViewModelProvider(this,factory).get(MatchViewModel.class);

        getSupportFragmentManager().setFragmentResultListener("logoPick", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull @NotNull String requestKey, @NonNull @NotNull Bundle result) {
                int team = result.getInt("team");
                String logoID = result.getString("logoID");
                addLogo(team, logoID);
            }
        });
    }

    /**
    * Uploads Match data from textInputs to realtime db
     * Uploads images from imageSelect to cloud storage
     *
     */
    private void uploadMatchData() {

        String team_name;

        DBOperations mDBOperations = new DBOperations(getApplicationContext());

        //build team1
        team_name = team1_name.getText().toString();
        Team team1 = new Team(team_name);

        //build team2
        team_name = team2_name.getText().toString();
        Team team2 = new Team(team_name);

        //place and date for match
        String place = editText_place.getText().toString();
        String date = editText_date.getText().toString();

        //upload logos
        StorageReference storageRefLogo1 = storage.getReference().child("logo1.jpg");
        StorageReference storageRefLogo2 = storage.getReference().child("logo2.jpg");

        mDBOperations.uploadLogo(storageRefLogo1,team1_logo_path, logoDrawable1);
        mDBOperations.uploadLogo(storageRefLogo2,team2_logo_path, logoDrawable2);

        //create Match and upload to realtime db
        Match currentMatch = new Match(place, date, team1,team2);

        mDBOperations.uploadMatch(currentMatch);

        mDBOperations.addMatchRoom(currentMatch,matchViewModel,AddMatchActivity.this);

        //save team logos from db to internal storage
        String filename_logo1 = "logo1.jpg";
        String filename_logo2 = "logo2.jpg";

        mDBOperations.saveLogo(filename_logo1,storageRefLogo1);
        mDBOperations.saveLogo(filename_logo2,storageRefLogo2);
    }


    /**
     * lets the user select an image(logo) from gallery
     * @param imageView : team1 or team2
     */
    private void selectImage(View imageView) {
        Intent selectImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(imageView.getId()==team1_logo.getId()){
            startActivityForResult(selectImageIntent,1);
        }else{
            startActivityForResult(selectImageIntent,2);
        }

    }

    /**
     * gets result from image select, loads storage into view and saves path @team_logo_path
     * @param requestCode  -determines imageView, 1 for Team1 2 for Team2
     * @param resultCode   -outcome of imageSelect
     * @param data         -selected ImageUri from selectImageIntent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            if(requestCode == 1 && selectedImage != null){
                Glide.with(this)
                        .load(selectedImage)
                        .into(team1_logo);
                team1_logo_path = selectedImage;
            }else if(selectedImage != null){
                Glide.with(this)
                        .load(selectedImage)
                        .into(team2_logo);
                team2_logo_path = selectedImage;
            }

        }
    }


    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity :  context
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    /**
     * fragment result listener callback, loads the selected drawable into the image view
     * @param team team the logo belongs to (either 1 or 2)
     * @param logoID drawable id
     */
    private void addLogo(int team, String logoID){
        int resourceId = Resources.getSystem().getIdentifier(logoID, "drawable","android");

        if(team == 1){
            team1_logo.setImageResource(getResources().getIdentifier("/drawable/"+logoID, null, getPackageName()));
            logoDrawable1 = team1_logo.getDrawable();
        }else{
            team2_logo.setImageResource(getResources().getIdentifier("/drawable/"+logoID, null, getPackageName()));
            logoDrawable2 = team2_logo.getDrawable();
        }
    }

}