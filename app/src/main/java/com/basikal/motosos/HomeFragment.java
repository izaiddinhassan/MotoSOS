package com.basikal.motosos;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import static android.support.constraint.Constraints.TAG;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private ImageButton mStartButton, mStopButton, mEditButton, mSaveButton;
    private EditText mEmergencyPhoneNumber;

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
        mEditButton = view.findViewById(R.id.btnEdit);
        mSaveButton = view.findViewById(R.id.btnSave);
        mEmergencyPhoneNumber = view.findViewById(R.id.etEmergencyContact);

        mStartButton.setOnClickListener(this);
        mStopButton.setOnClickListener(this);
        mEditButton.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);

        mEmergencyPhoneNumber.setEnabled(false);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String defaultValue = getResources().getString(R.string.detection_status_default);
        String detection_status = sharedPref.getString(getString(R.string.detection_status), defaultValue);
        if (detection_status.equalsIgnoreCase("on")) {
            mStartButton.setVisibility(View.GONE);
            mStopButton.setVisibility(View.VISIBLE);
        } else {
            mStartButton.setVisibility(View.VISIBLE);
            mStopButton.setVisibility(View.GONE);
        }

        String defaultNo = getResources().getString(R.string.emergency_contact_number_default);
        String phone_no = sharedPref.getString(getString(R.string.emergency_contact_number), defaultNo);
        if (!phone_no.equalsIgnoreCase(defaultNo)) {
            mEmergencyPhoneNumber.setText(phone_no);
        }
        Log.d(TAG, "PhoneNo: " + defaultNo + phone_no);

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
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
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
        } else if (view == mEditButton) {
            mEmergencyPhoneNumber.setEnabled(true);
            mEditButton.setVisibility(View.GONE);
            mSaveButton.setVisibility(View.VISIBLE);
            mEmergencyPhoneNumber.setHint("enter emergency phone number here");
        } else if (view == mSaveButton) {
            mEmergencyPhoneNumber.setEnabled(false);
            mSaveButton.setVisibility(View.GONE);
            mEditButton.setVisibility(View.VISIBLE);
            mEmergencyPhoneNumber.setHint("please set emergency number by clicking -->");
            editor.putString(getString(R.string.emergency_contact_number),
                    mEmergencyPhoneNumber.getText().toString().trim());
            editor.commit();
        }
    }
}
