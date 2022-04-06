package com.raffelberg.cr_ticker.ui.addmatch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raffelberg.cr_ticker.LogoPickListener;
import com.raffelberg.cr_ticker.R;
import com.raffelberg.cr_ticker.ui.home.HomeAdapter;

public class LogoPickerAdapter extends RecyclerView.Adapter<LogoPickerAdapter.ViewHolder>{

    private Context context;
    private LogoPickListener logoPickListener;

    public LogoPickerAdapter(Context context, LogoPickListener logoPickListener){
        this.context = context;
        this.logoPickListener = logoPickListener;
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
        holder.getImageView().setOnClickListener(v -> {
            //logoPickListener.addLogo(logoList[position]);
        });
    }

    @Override
    public int getItemCount() {
        return 0;
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
