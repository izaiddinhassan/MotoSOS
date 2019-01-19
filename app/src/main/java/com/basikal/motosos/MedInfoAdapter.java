package com.basikal.motosos;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MedInfoAdapter extends RecyclerView.Adapter<MedInfoAdapter.MedInfoViewHolder> {

    private Context mCtx;
    private List<MedInfo> medInfoList;
    private String accident;

    public MedInfoAdapter(Context mCtx, List<MedInfo> medInfoList, String accident) {
        this.mCtx = mCtx;
        this.medInfoList = medInfoList;
        this.accident = accident;
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
        final String medId = medInfo.getMedId();
        final String medType = medInfo.getMedType();
        final String medName = medInfo.getMedName();
        final String medExtra = medInfo.getMedExtra();
        final String userId = medInfo.getUserId();
        holder.typeView.setText(medType);
        holder.nameView.setText(medName);
        holder.extraView.setText(medExtra);

        holder.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mCtx)
                        .setMessage("Are you sure you want to delete")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DatabaseReference dr = FirebaseDatabase.getInstance().getReference();
                                dr.child("MedicalInfo").child(medId).removeValue();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        holder.updateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("medId", medId);
                bundle.putString("medType", medType);
                bundle.putString("medName", medName);
                bundle.putString("medExtra", medExtra);
                bundle.putString("userId", userId);
                FragmentManager manager = ((AppCompatActivity) mCtx).getSupportFragmentManager();
                MedInfoUpdateFragment medInfoUpdateFragment = new MedInfoUpdateFragment();
                medInfoUpdateFragment.setArguments(bundle);
                manager.beginTransaction()
                        .replace(R.id.fragment_container, medInfoUpdateFragment, "FRAG_UPDATE_MED_INFO")
                        .addToBackStack(null)
                        .commit();
            }
        });
        if (accident != null) {
            holder.deleteView.setVisibility(View.GONE);
            holder.updateView.setVisibility(View.GONE);
            holder.medInfoLayout.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return medInfoList.size();
    }

    class MedInfoViewHolder extends RecyclerView.ViewHolder {
        TextView typeView, nameView, extraView, deleteView, updateView;
        CardView medInfoLayout;

        public MedInfoViewHolder(View itemView) {
            super(itemView);
            typeView = itemView.findViewById(R.id.tvType);
            nameView = itemView.findViewById(R.id.tvName);
            extraView = itemView.findViewById(R.id.tvExtra);
            deleteView = itemView.findViewById(R.id.tvDelete);
            updateView = itemView.findViewById(R.id.tvUpdate);
            medInfoLayout = itemView.findViewById(R.id.cvMedInfo);
        }
    }
}
