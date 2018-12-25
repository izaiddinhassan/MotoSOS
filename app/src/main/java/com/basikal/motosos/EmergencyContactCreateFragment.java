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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmergencyContactCreateFragment extends Fragment implements View.OnClickListener {

    private EditText mNameView, mPhoneNoView, mAddressView;
    private Button mSaveButton, mCancelButton;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form_emergency_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mNameView = view.findViewById(R.id.etEmcName);
        mPhoneNoView = view.findViewById(R.id.etEmcPhoneNo);
        mAddressView = view.findViewById(R.id.etEmcAddress);
        mSaveButton = view.findViewById(R.id.btnSave);
        mCancelButton = view.findViewById(R.id.btnCancel);

        mSaveButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
    }

    public void saveEmergencyContactToDb() {
        String id = mDatabase.child("EmergencyContact").push().getKey();
        String name = mNameView.getText().toString().trim();
        String phoneNo = mPhoneNoView.getText().toString().trim();
        String address = mAddressView.getText().toString().trim();
        String userId = mAuth.getUid();

        EmergencyContact emergencyContact = new EmergencyContact(id, name, phoneNo, address, userId);
        mDatabase.child("EmergencyContact")
                .child(id)
                .setValue(emergencyContact)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container, new EmergencyContactFragment(), "FRAG_EMERGENCY_CONTACT")
                                    .commit();
                            Toast.makeText(getActivity(), "Emergency Contact Created Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == mSaveButton) {
            saveEmergencyContactToDb();
        } else if (v == mCancelButton) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new EmergencyContactFragment(), "FRAG_EMERGENCY_CONTACT")
                    .commit();
        }
    }
}
