package com.raffelberg.cr_ticker.ImageOperations;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class ImageLoader {

    public void loadLogo(ImageView imageView, String logoPath, Context context){
        File logoFile = new File(context.getFilesDir(), logoPath);
        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true)
                .centerInside()
                .transform(new CircleCrop());

        if(logoFile.isFile()){
            Glide.with(imageView)
                    .load(logoFile)
                    .apply(options)
                    .into(imageView);
        }else{
            StorageReference storageRef = FirebaseStorage.getInstance("gs://cr-ticker-herren-logos").getReference().child(logoPath);
            Glide.with(imageView)
                    .load(storageRef)
                    .apply(options)
                    .into(imageView);

            storeLogo(logoPath, context);
        }
    }

    private void storeLogo(String logoPath, Context context){
        File file = new File(context.getFilesDir(), logoPath);
        StorageReference storageRef = FirebaseStorage.getInstance("gs://cr-ticker-herren-logos").getReference().child(logoPath);

        storageRef.getFile(file).addOnSuccessListener(taskSnapshot -> {
            Log.e("Download", "Download");
        });
    }
}
