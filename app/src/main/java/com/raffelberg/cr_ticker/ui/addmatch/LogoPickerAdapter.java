package com.raffelberg.cr_ticker.ui.addmatch;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.raffelberg.cr_ticker.ImageOperations.ImageLoader;
import com.raffelberg.cr_ticker.LogoPickListener;
import com.raffelberg.cr_ticker.R;
import com.raffelberg.cr_ticker.ui.home.HomeAdapter;

import java.util.ArrayList;
import java.util.List;

public class LogoPickerAdapter extends RecyclerView.Adapter<LogoPickerAdapter.ViewHolder>{

    private Context context;
    private LogoPickListener logoPickListener;
    List<String> logoPaths;

    public LogoPickerAdapter(Context context, LogoPickListener logoPickListener, List<String> logoPaths){
        this.context = context;
        this.logoPickListener = logoPickListener;
        this.logoPaths = logoPaths;
    }


    @NonNull
    @Override
    public LogoPickerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.logo_picker_item,parent,false);
        return new LogoPickerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogoPickerAdapter.ViewHolder holder, int position) {
        //holder.getImageView().setImageResource(context.getResources().getIdentifier("/drawable/"+logoList[position], null, context.getPackageName()));
        ImageLoader imageLoader = new ImageLoader();
        imageLoader.loadLogo(holder.getImageView(), logoPaths.get(position), context);
        holder.getImageView().setOnClickListener(v -> {
            //logoPickListener.addLogo(logoList[position]);
        });
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if(logoPaths != null)
            itemCount = logoPaths.size();

        return itemCount;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.logoPickerItem_Logo);
        }

        public ImageView getImageView() {
            return imageView;
        }
    }
}
