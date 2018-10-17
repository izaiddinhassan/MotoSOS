package com.basikal.motosos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LogsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "LogsFragment";
    private Button mAddTestButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<Logs> mLogsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_logs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Accident Logs");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = DbConn.getDatabase().getReference();
        mDatabase.keepSynced(true);

        mRecyclerView = view.findViewById(R.id.rvLogs);
        mAddTestButton = view.findViewById(R.id.btnAddTest);

        mAddTestButton.setOnClickListener(this);

        String id = mAuth.getUid();
        Log.d(TAG, "onViewCreated: " + id);
        mDatabase.child("Logs")
                .orderByChild("userId")
                .equalTo(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //iterating through all the values in database
                        mLogsList = new ArrayList<>();
                        for (DataSnapshot logsSnapshot : dataSnapshot.getChildren()) {
                            Logs logs = logsSnapshot.getValue(Logs.class);
                            Log.d(TAG, "onDataChange: " + logs);
                            mLogsList.add(logs);
                }
                //creating adapter
                mAdapter = new LogsAdapter(getActivity(), mLogsList);

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

    public void addTestData() {
        String logId = mDatabase.child("AccidentLog").push().getKey();
        String logDate = "aa";
        String logTime = "aa";
        String logLatLong = "aa";
        String logGyroValue = "aa";
        String logAcceleroValue = "aa";
        String logAccidentStatus = "aa";
        String userId = mAuth.getUid();
        Logs logs = new Logs(logId, logDate, logTime, logLatLong, logGyroValue, logAcceleroValue, logAccidentStatus, userId);
        mDatabase.child("Logs")
                .child(logId)
                .setValue(logs)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == mAddTestButton) {
            addTestData();
        }
    }
}
