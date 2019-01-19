package com.basikal.motosos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class EmergencyContactUpdateFragment extends Fragment implements View.OnClickListener {
    private EditText mEmcNameView, mEmcPhoneNoView, mEmcAddressView;
    private Button mSaveButton, mCancelButton;
    private Bundle mArgs;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form_emergency_contact, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Update Emergency Contact");

        mDatabase = DbConn.getDatabase().getReference();

        mEmcNameView = view.findViewById(R.id.etEmcName);
        mEmcPhoneNoView = view.findViewById(R.id.etEmcPhoneNo);
        mEmcAddressView = view.findViewById(R.id.etEmcAddress);
        mSaveButton = view.findViewById(R.id.btnSave);
        mCancelButton = view.findViewById(R.id.btnCancel);

        mArgs = getArguments();
        String emcName = mArgs.getString("emcName");
        String emcPhoneNo = mArgs.getString("emcPhoneNo");
        String emcAddress = mArgs.getString("emcAddress");

        mEmcNameView.setText(emcName);
        mEmcPhoneNoView.setText(emcPhoneNo);
        mEmcAddressView.setText(emcAddress);

        mSaveButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
    }

    public void updateEmergencyContact() {
        String emcId = mArgs.getString("emcId");
        String emcName = mEmcNameView.getText().toString().trim();
        String emcPhoneNo = mEmcPhoneNoView.getText().toString().trim();
        String emcAddress = mEmcAddressView.getText().toString().trim();

        HashMap<String, Object> map = new HashMap<>();
        map.put("emcName", emcName);
        map.put("emcPhoneNo", emcPhoneNo);
        map.put("emcAddress", emcAddress);

        mDatabase.child("EmergencyContact")
                .child(emcId)
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            upPage();
                            Toast.makeText(getActivity(), "Emergency Contact Information Updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void upPage() {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new EmergencyContactFragment(), "FRAG_UPDATE_USER")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                updateEmergencyContact();
                break;
            case R.id.btnCancel:
                upPage();
                break;
        }
    }
}
