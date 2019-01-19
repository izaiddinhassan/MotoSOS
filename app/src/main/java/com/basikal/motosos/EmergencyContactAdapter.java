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

public class EmergencyContactAdapter
        extends RecyclerView.Adapter<EmergencyContactAdapter.EmergencyContactViewHolder> {

    private Context mCtx;
    private List<EmergencyContact> emergencyContactList;
    private String accident;

    public EmergencyContactAdapter(Context mCtx, List<EmergencyContact> emergencyContactList, String accident) {
        this.mCtx = mCtx;
        this.emergencyContactList = emergencyContactList;
        this.accident = accident;
    }

    @NonNull
    @Override
    public EmergencyContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_emergency_contact, parent, false);
        return new EmergencyContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmergencyContactViewHolder holder, int position) {
        EmergencyContact emergencyContact = emergencyContactList.get(position);
        final String emcId = emergencyContact.getEmcId();
        final String emcName = emergencyContact.getEmcName();
        final String emcPhoneNo = emergencyContact.getEmcPhoneNo();
        final String emcAddress = emergencyContact.getEmcAddress();
        final String userId = emergencyContact.getUserId();
        holder.nameView.setText(emcName);
        holder.phoneNoView.setText(emcPhoneNo);
        holder.addressView.setText(emcAddress);

        holder.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mCtx)
                        .setMessage("Are you sure you want to delete")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DatabaseReference dr = FirebaseDatabase.getInstance().getReference();
                                dr.child("EmergencyContact").child(emcId).removeValue();
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
                bundle.putString("emcId", emcId);
                bundle.putString("emcName", emcName);
                bundle.putString("emcPhoneNo", emcPhoneNo);
                bundle.putString("emcAddress", emcAddress);
                bundle.putString("userId", userId);
                FragmentManager manager = ((AppCompatActivity) mCtx).getSupportFragmentManager();
                EmergencyContactUpdateFragment emergencyContactUpdateFragment = new EmergencyContactUpdateFragment();
                emergencyContactUpdateFragment.setArguments(bundle);
                manager.beginTransaction()
                        .replace(R.id.fragment_container, emergencyContactUpdateFragment, "FRAG_UPDATE_USER")
                        .addToBackStack(null)
                        .commit();
            }
        });

        if (accident != null) {
            holder.deleteView.setVisibility(View.GONE);
            holder.updateView.setVisibility(View.GONE);
            holder.emergencyContactLayout.setOnClickListener(null);
        }
    }


    @Override
    public int getItemCount() {
        return emergencyContactList.size();
    }

    class EmergencyContactViewHolder extends RecyclerView.ViewHolder {
        TextView nameView, phoneNoView, addressView, deleteView, updateView;
        CardView emergencyContactLayout;

        public EmergencyContactViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.tvName);
            phoneNoView = itemView.findViewById(R.id.tvPhoneNo);
            addressView = itemView.findViewById(R.id.tvAddress);
            deleteView = itemView.findViewById(R.id.tvDelete);
            updateView = itemView.findViewById(R.id.tvUpdate);
            emergencyContactLayout = itemView.findViewById(R.id.cvEmergencyContact);
        }
    }
}
