package com.raffelberg.cr_ticker.ImageOperations;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ImageLoader {

    public void loadLogo(ImageView imageView, String logoPath, Context context, String instance){
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
            StorageReference storageRef = FirebaseStorage.getInstance(instance).getReference().child(logoPath);
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

    public void uploadLogoFromImageView(String id, String logoName, ImageView imageView){
        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(id).child(logoName);
        storageRef.putBytes(data);
    }
}
