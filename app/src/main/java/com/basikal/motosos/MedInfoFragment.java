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

import static android.support.constraint.Constraints.TAG;

public class MedInfoFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<MedInfo> mMedInfoList;
    private FloatingActionButton mAddButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_med_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Medical Information");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = DbConn.getDatabase().getReference();
        mDatabase.keepSynced(true);

        mRecyclerView = view.findViewById(R.id.rvMedInfo);
        mAddButton = view.findViewById(R.id.fabAdd);

        mAddButton.setOnClickListener(this);

        readMedicalInfoByIdFromDb();
    }

    private void readMedicalInfoByIdFromDb() {
        String id = mAuth.getUid();
        mDatabase.child("MedicalInfo")
                .orderByChild("userId")
                .equalTo(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //iterating through all the values in database
                        mMedInfoList = new ArrayList<>();
                        for (DataSnapshot medInfoSnapshot : dataSnapshot.getChildren()) {
                            MedInfo medInfo = medInfoSnapshot.getValue(MedInfo.class);
                            mMedInfoList.add(medInfo);
                        }
                        Log.d(TAG, "onDataChange: " + mMedInfoList.toString());
                        //creating adapter
                        mAdapter = new MedInfoAdapter(getActivity(), mMedInfoList, null);

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

    public void createMedicalInfo() {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new MedInfoCreateFragment(), "FRAG_CREATE_MED_INFO")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View v) {
        if (v == mAddButton) {
            createMedicalInfo();
        }
    }
}
