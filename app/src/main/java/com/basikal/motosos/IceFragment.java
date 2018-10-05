package com.basikal.motosos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class IceFragment extends Fragment {
    private FabSpeedDial mFabSpeedDial;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ice, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("In Case of Emergency Information");
        mFabSpeedDial = view.findViewById(R.id.fabAdd);

        mFabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                Bundle args = new Bundle();
                if (menuItem.toString().equalsIgnoreCase("Medication")) {
                    IceAddDialog dialog = new IceAddDialog();
                    args.putString("type", "Medication");
                    dialog.setArguments(args);
                    dialog.show(getActivity().getFragmentManager(), "addIce");
                } else if (menuItem.toString().equalsIgnoreCase("Medical Condition")) {
                    IceAddDialog dialog = new IceAddDialog();
                    args.putString("type", "Medical Condition");
                    dialog.setArguments(args);
                    dialog.show(getActivity().getFragmentManager(), "addIce");
                } else {
                    IceAddDialog dialog = new IceAddDialog();
                    args.putString("type", "Allergy");
                    dialog.setArguments(args);
                    dialog.show(getActivity().getFragmentManager(), "addIce");
                }
                return false;
            }
        });
    }


}
