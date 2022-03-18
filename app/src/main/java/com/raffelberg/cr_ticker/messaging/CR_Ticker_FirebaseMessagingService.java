package com.raffelberg.cr_ticker.messaging;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.raffelberg.cr_ticker.R;
import com.raffelberg.cr_ticker.activities.TickerLog;
import com.raffelberg.cr_ticker.persistence.DBOperations;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

public class CR_Ticker_FirebaseMessagingService extends FirebaseMessagingService {

    /**
     * receives either storageTickle or new log notification
     * storageTickle: cloud storage changed -> update logos in local storage
     * log notification: check urgency of message and send to users with proper preferences
     * @param remoteMessage received message
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Map<String,String> data = remoteMessage.getData();
        

        if(data.size()>0){
            if (data.containsKey("storageTickle")){
                DBOperations dbOperations = new DBOperations(getApplicationContext());
                StorageReference storage1 = FirebaseStorage.getInstance().getReference().child("logo1.jpg");
                StorageReference storage2 = FirebaseStorage.getInstance().getReference().child("logo2.jpg");

                dbOperations.saveLogo("logo1.jpg",storage1);
                dbOperations.saveLogo("logo2.jpg",storage2);
            }else{
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if(sharedPreferences.getBoolean("showMessages",true)) {
                    sendNotification(data);
                }else if(sharedPreferences.getBoolean("showKeyMessages",true)&&data.containsKey("keyMessage")) {
                    sendNotification(data);
                }
            }
        }
    }

    /**
     * sends Notification with message text to users
     * @param data :message text
     */
    private void sendNotification(Map<String, String> data) {
        String key;

        Intent intent = new Intent(this, TickerLog.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Create the pending intent to launch the activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        if (data.containsKey("keyMessage")) {
            key = "keyMessage";
        } else {
            key = "log";
        }

        String message = data.get(key);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"tickerChannel")
                .setSmallIcon(R.mipmap.cr_logo_round)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int id = (int) System.currentTimeMillis();

        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());

    }


}
