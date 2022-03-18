package com.raffelberg.cr_ticker.persistence;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.raffelberg.cr_ticker.R;
import com.raffelberg.cr_ticker.ui.MatchViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class DBOperations {

    private Context context;
    DatabaseReference myRef;

    public DBOperations(Context context){
        this.context=context;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    /**
     * uploads image to cloud storage
     * @param storageRef: upload path
     * @param path: path of image to upload
     */
    public  void uploadLogo(StorageReference storageRef, Uri path, Drawable drawable){
        /*
        if(path != null) {
            try {
                File file = new File(getRealPathFromURI(path));
                InputStream stream = new FileInputStream(file);
                UploadTask uploadTask = storageRef.putStream(stream);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/
        if(drawable != null){

            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.WEBP,100,stream);
            byte[] byteArray = stream.toByteArray();
            storageRef.putBytes(byteArray);
        }else{  //default Logo

            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.default_logo);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.WEBP,100,stream);
            byte[] byteArray = stream.toByteArray();
            storageRef.putBytes(byteArray);
        }
    }

    /**
     * saves logo to local storage
     * @param filename: location to store the logo in
     * @param storageRef: path of image to download from cloud storage
     */
   public  void saveLogo(String filename, StorageReference storageRef){

       File logo = new File(context.getFilesDir(), filename);
       storageRef.getFile(logo);

   }

    /**
     * uploads mach to realtime database
     * @param match: match to upload
     */
   public void uploadMatch(Match match){

       HashMap<String,Match> matchUpload = new HashMap<>();

       matchUpload.put("currentMatch",match);

       myRef.child("Match").setValue(matchUpload);

   }

    /**
     * updates match in local database, overwrites current match if new
     * theres always only one match in the database
     * @param mMatch: new matchData to update
     * @param mMatchViewModel: controller
     * @param lifecycleOwner: context
     */
   public void addMatchRoom(Match mMatch, MatchViewModel mMatchViewModel, LifecycleOwner lifecycleOwner){

        final LiveData<Match> currentMatch = mMatchViewModel.getCurrentMatch();
       Observer<Match> observer = new Observer<Match>() {
           @Override
           public void onChanged(Match match) {
               if(match != null) {
                   mMatchViewModel.updateMatch(mMatch);
               }else{
                   mMatchViewModel.insertMatch(mMatch);
               }
               currentMatch.removeObserver(this);

           }
       };
      currentMatch.observe(lifecycleOwner,observer);
   }

    /**
     * uploads new score to realtime database
     * @param viewScore: View to be updated
     * @param team: team whose score increases
     */
   public void increaseScore(TextView viewScore, String team){

        String score = String.valueOf(Integer.parseInt(viewScore.getText().toString())+1);
        viewScore.setText(score);

       myRef.child("Match").child("currentMatch").child(team).child("team_score").setValue(score);

   }

    /**
     * uploads new score to realtime database
     * @param viewScore: View to be updated
     * @param team: team whose score decreases
     */
    public void decreaseScore(TextView viewScore, String team){

        if(Integer.parseInt(viewScore.getText().toString())>0) {
            String score = String.valueOf(Integer.parseInt(viewScore.getText().toString()) - 1);
            viewScore.setText(score);

            myRef.child("Match").child("currentMatch").child(team).child("team_score").setValue(score);
        }
    }

    /**
     * uploads new match status to realtime database
     * @param status: new match status
     */
    public void setStatus(String status){
        myRef.child("Match").child("currentMatch").child("state").setValue(status);
    }

    /**
     * uploads new log to realtime database
     * @param log: new log
     * @param matchViewModel: controller
     * @param lifecycleOwner: context
     */
    public void addLog(String log, MatchViewModel matchViewModel,LifecycleOwner lifecycleOwner){

        final LiveData<Match> currentMatch = matchViewModel.getCurrentMatch();

        Observer<Match> observer = new Observer<Match>() {
            @Override
            public void onChanged(Match match) {
                if(match != null) {
                    if(match.getLogs() == null){
                        match.setLogs(new ArrayList<>());
                    }
                        match.addLog(log);
                        matchViewModel.updateMatch(match);
                        myRef.child("Match").child("currentMatch").child("logs").setValue(match.getLogs());
                    }
                currentMatch.removeObserver(this);
                }



        };
        currentMatch.observe(lifecycleOwner,observer);

    }

    /**
     * helper function for uploadLogo
     * @param uri item to get the path from
     * @return path from given uri
     */
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        String result = cursor.getString(idx);
        cursor.close();
        return result;
    }




}
