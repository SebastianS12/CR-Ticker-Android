package com.raffelberg.cr_ticker.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.raffelberg.cr_ticker.LogoPickListener;
import com.raffelberg.cr_ticker.R;

import java.util.List;

public class logoPickerAdapter extends RecyclerView.Adapter<logoPickerAdapter.Viewholder>{

    private Context context;
    private final String[] logoList = {"cr_logo", "uhlenhorst_logo", "chtc_logo", "bthv_logo", "rwk_logo", "bg_logo"};
    private LogoPickListener logoPickListener;

    public logoPickerAdapter(Context context, LogoPickListener logoPickListener){
        this.context = context;
        this.logoPickListener = logoPickListener;
    }

    @NonNull
    @Override
    public logoPickerAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.logo_picker_item,parent,false);
        return new logoPickerAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull logoPickerAdapter.Viewholder holder, int position) {
        //holder.getImageView().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.cr_logo));
        holder.getImageView().setImageResource(context.getResources().getIdentifier("/drawable/"+logoList[position], null, context.getPackageName()));
        holder.getImageView().setOnClickListener(v -> {
            logoPickListener.addLogo(logoList[position]);
        });
    }

    @Override
    public int getItemCount() {
        if(logoList != null) {
            return logoList.length;
        }
        return 0;
    }

    //public void setLogoList(List<String> logoList) {
        //this.logoList = logoList;
    //}

    public static class Viewholder extends RecyclerView.ViewHolder{
        private ImageView imageView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            imageView= itemView.findViewById(R.id.logo_image);
            imageView.setOnClickListener(v -> {

            });
        }

        public ImageView getImageView(){
            return imageView;
        }
    }
}
