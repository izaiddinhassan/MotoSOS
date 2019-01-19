package com.basikal.motosos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmergencyContactFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "EmergencyContactFragment";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<EmergencyContact> mEmergencyContactList;
    private FloatingActionButton mAddButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_emergency_contact, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Emergency Contact");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = DbConn.getDatabase().getReference();
        mDatabase.keepSynced(true);

        mAddButton = view.findViewById(R.id.fabAdd);
        mRecyclerView = view.findViewById(R.id.rvEmergencyContact);

        mAddButton.setOnClickListener(this);

        getEmergencyContactFromDb();
    }

    public void getEmergencyContactFromDb() {
        String id = mAuth.getUid();
        mDatabase.child("EmergencyContact")
                .orderByChild("userId")
                .equalTo(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //iterating through all the values in database
                        mEmergencyContactList = new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            EmergencyContact emergencyContact = ds.getValue(EmergencyContact.class);
                            mEmergencyContactList.add(emergencyContact);
                            Log.d(TAG, "onDataChange: " + mEmergencyContactList.toString());
                        }
                        //creating adapter
                        mAdapter = new EmergencyContactAdapter(getActivity(), mEmergencyContactList, null);

                        //adding adapter to recyclerView
                        mRecyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        mRecyclerView.setHasFixedSize(true); //set fixed size for element in recycler view
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void createEmergencyContact() {
        EmergencyContactCreateFragment emergencyContactCreateFragment = new EmergencyContactCreateFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, emergencyContactCreateFragment, "FRAG_CREATE_CONTACT")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View v) {
        if (v == mAddButton) {
            createEmergencyContact();
        }
    }
}
