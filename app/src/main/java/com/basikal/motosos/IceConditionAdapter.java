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

public class IceConditionAdapter extends RecyclerView.Adapter<IceConditionAdapter.IceViewHolder> {

    private Context mCtx;
    private List<Ice> iceList;

    public IceConditionAdapter(Context mCtx, List<Ice> iceList) {
        this.mCtx = mCtx;
        this.iceList = iceList;
    }

    @NonNull
    @Override
    public IceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_ice_condition, parent, false);
        return new IceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IceViewHolder holder, int position) {
        holder.test1.setText("test1");
        holder.test1.setText("test2");
        holder.test1.setText("test3");
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class IceViewHolder extends RecyclerView.ViewHolder {
        TextView test1, test2, test3;
        CardView iceLayout;

        public IceViewHolder(View itemView) {
            super(itemView);
            iceLayout = itemView.findViewById(R.id.cvIce);
        }
    }
}
