package com.raffelberg.cr_ticker.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raffelberg.cr_ticker.R;

import java.util.List;

public class logAdapter extends RecyclerView.Adapter<logAdapter.Viewholder> {

    private List<String> logList;

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.getTextView().setText(logList.get(position));
    }

    @Override
    public int getItemCount() {
        if(logList != null) {
            return logList.size();
        }
        return 0;
    }

    public void setLogList(List<String> logList) {
        this.logList = logList;
    }

    public static class Viewholder extends RecyclerView.ViewHolder{
    private TextView textView;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            textView= itemView.findViewById(R.id.log);
        }

        public TextView getTextView(){
            return textView;
        }
    }
}
