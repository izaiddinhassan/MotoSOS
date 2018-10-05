package com.basikal.motosos;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import static android.support.constraint.Constraints.TAG;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private ImageButton mStartButton, mStopButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("HOME");
        mStartButton = view.findViewById(R.id.btnStart);
        mStopButton = view.findViewById(R.id.btnStop);

        mStartButton.setOnClickListener(this);
        mStopButton.setOnClickListener(this);
    }

    public void startService(View view) {
        Intent serviceIntent = new Intent(getActivity(), DetectionService.class);
        getActivity().startService(serviceIntent);
    }

    public void stopService(View view) {
        Intent serviceIntent = new Intent(getActivity(), DetectionService.class);
        getActivity().stopService(serviceIntent);
    }


    @Override
    public void onClick(View view) {
        if (view == mStartButton) {
            Log.d(TAG, "onClick: start");
            mStartButton.setVisibility(View.GONE);
            mStopButton.setVisibility(View.VISIBLE);
            startService(view);
        } else if (view == mStopButton) {
            Log.d(TAG, "onClick: stop");
            mStartButton.setVisibility(View.VISIBLE);
            mStopButton.setVisibility(View.GONE);
            stopService(view);
        }
    }
}
