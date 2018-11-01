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

public class MedInfoAdapter extends RecyclerView.Adapter<MedInfoAdapter.MedInfoViewHolder> {

    private Context mCtx;
    private List<MedInfo> medInfoList;

    public MedInfoAdapter(Context mCtx, List<MedInfo> medInfoList) {
        this.mCtx = mCtx;
        this.medInfoList = medInfoList;
    }

    @NonNull
    @Override
    public MedInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_med_info, parent, false);
        return new MedInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedInfoViewHolder holder, int position) {
        MedInfo medInfo = medInfoList.get(position);
        holder.typeView.setText(medInfo.getMedType());
        holder.nameView.setText(medInfo.getMedName());
        holder.extraView.setText(medInfo.getMedExtra());
    }

    @Override
    public int getItemCount() {
        return medInfoList.size();
    }

    class MedInfoViewHolder extends RecyclerView.ViewHolder {
        TextView typeView, nameView, extraView;
        CardView medInfoLayout;

        public MedInfoViewHolder(View itemView) {
            super(itemView);
            typeView = itemView.findViewById(R.id.tvType);
            nameView = itemView.findViewById(R.id.tvName);
            extraView = itemView.findViewById(R.id.tvExtra);
            medInfoLayout = itemView.findViewById(R.id.cvMedInfo);
        }
    }
}
