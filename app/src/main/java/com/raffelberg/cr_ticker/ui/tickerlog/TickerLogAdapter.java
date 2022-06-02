package com.raffelberg.cr_ticker.ui.tickerlog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raffelberg.cr_ticker.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TickerLogAdapter extends RecyclerView.Adapter<com.raffelberg.cr_ticker.ui.tickerlog.TickerLogAdapter.ViewHolder>{

    private List<String> logList;

    @NonNull
    @NotNull
    @Override
    public TickerLogAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.log_item, parent, false);

        return new com.raffelberg.cr_ticker.ui.tickerlog.TickerLogAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TickerLogAdapter.ViewHolder holder, int position) {
        holder.getLogTextView().setText(logList.get(position));
    }

    @Override
    public int getItemCount() {
        if(logList != null) {
            return logList.size();
        }
        return 0;
    }

    public void setLogList(List<String> logList){
        this.logList = logList;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        final private TextView logTextView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            logTextView = (TextView) itemView.findViewById(R.id.log_item_textfield);
        }

        public TextView getLogTextView() {
            return logTextView;
        }
    }
}
