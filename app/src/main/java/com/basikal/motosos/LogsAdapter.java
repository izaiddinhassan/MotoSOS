package com.basikal.motosos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.LogsViewHolder> {

    private Context mCtx;
    private List<Logs> logsList;

    public LogsAdapter(Context mCtx, List<Logs> logsList) {
        this.mCtx = mCtx;
        this.logsList = logsList;
    }

    @NonNull
    @Override
    public LogsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_logs, parent, false);
        return new LogsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogsViewHolder holder, int position) {
        Logs logs = logsList.get(position);
        holder.dateView.setText(logs.getLogDate());
        holder.timeView.setText(logs.getLogTime());
        holder.latView.setText(logs.getLogLat());
        holder.longView.setText(logs.getLogLong());
        holder.gyroValue.setText(logs.getLogGyroValue());
        holder.acceleroValue.setText(logs.getLogAcceleroValue());
        holder.accidentStatusView.setText(logs.getLogAccidentStatus());
    }

    @Override
    public int getItemCount() {
        return logsList.size();
    }

    class LogsViewHolder extends RecyclerView.ViewHolder {
        TextView dateView, timeView, latView, longView, gyroValue, acceleroValue, accidentStatusView;
        CardView logsLayout;

        public LogsViewHolder(View itemView) {
            super(itemView);
            dateView = itemView.findViewById(R.id.tvDate);
            timeView = itemView.findViewById(R.id.tvTime);
            latView = itemView.findViewById(R.id.tvLat);
            longView = itemView.findViewById(R.id.tvLong);
            gyroValue = itemView.findViewById(R.id.tvGyroValue);
            acceleroValue = itemView.findViewById(R.id.tvAcceleroValue);
            accidentStatusView = itemView.findViewById(R.id.tvAccidentStatus);
            logsLayout = itemView.findViewById(R.id.cvLogs);
        }
    }
}
