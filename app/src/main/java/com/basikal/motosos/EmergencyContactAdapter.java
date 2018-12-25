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

public class EmergencyContactAdapter extends RecyclerView.Adapter<EmergencyContactAdapter.EmergencyContactViewHolder> {

    private Context mCtx;
    private List<EmergencyContact> emergencyContactList;

    public EmergencyContactAdapter(Context mCtx, List<EmergencyContact> emergencyContactList) {
        this.mCtx = mCtx;
        this.emergencyContactList = emergencyContactList;
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
        holder.nameView.setText(emergencyContact.getEmcName());
        holder.phoneNoView.setText(emergencyContact.getEmcPhoneNo());
        holder.addressView.setText(emergencyContact.getEmcAddress());
    }

    @Override
    public int getItemCount() {
        return emergencyContactList.size();
    }

    class EmergencyContactViewHolder extends RecyclerView.ViewHolder {
        TextView nameView, phoneNoView, addressView;
        CardView emergencyContactLayout;

        public EmergencyContactViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.tvName);
            phoneNoView = itemView.findViewById(R.id.tvPhoneNo);
            addressView = itemView.findViewById(R.id.tvAddress);
            emergencyContactLayout = itemView.findViewById(R.id.cvEmergencyContact);
        }
    }
}
