package com.basikal.motosos;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String defaultValue = getResources().getString(R.string.detection_status_default);
        String detection_status = sharedPref.getString(getString(R.string.detection_status), defaultValue);
        if (detection_status.equalsIgnoreCase("on")) {
            mStartButton.setVisibility(View.GONE);
            mStopButton.setVisibility(View.VISIBLE);
        } else {
            mStartButton.setVisibility(View.VISIBLE);
            mStopButton.setVisibility(View.GONE);
        }
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
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (view == mStartButton) {
            Log.d(TAG, "onClick: start");
            mStartButton.setVisibility(View.GONE);
            mStopButton.setVisibility(View.VISIBLE);
            startService(view);
            editor.putString(getString(R.string.detection_status), "on");
            editor.apply();
        } else if (view == mStopButton) {
            Log.d(TAG, "onClick: stop");
            mStartButton.setVisibility(View.VISIBLE);
            mStopButton.setVisibility(View.GONE);
            stopService(view);
            editor.putString(getString(R.string.detection_status), "off");
            editor.apply();
        }
    }
}
