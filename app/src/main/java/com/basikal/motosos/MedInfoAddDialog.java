package com.basikal.motosos;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MedInfoAddDialog extends DialogFragment implements View.OnClickListener {
    private DatabaseReference mDatabase;
    private TextView mAddView, mCancelView;
    private FirebaseAuth mAuth;
    private Spinner mType;
    private EditText mNameView, mNotesView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form_med_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase = DbConn.getDatabase().getReference();
        mAuth = FirebaseAuth.getInstance();

        mType = view.findViewById(R.id.spType);
        mNameView = view.findViewById(R.id.etName);
        mNotesView = view.findViewById(R.id.etExtra);
        mAddView = view.findViewById(R.id.tvAdd);
        mCancelView = view.findViewById(R.id.tvCancel);

        mAddView.setOnClickListener(this);
        mCancelView.setOnClickListener(this);

        ArrayAdapter<CharSequence> adapterMedicalType = ArrayAdapter.createFromResource(getActivity(),
                R.array.medical_type_array, android.R.layout.simple_spinner_item);
        adapterMedicalType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mType.setAdapter(adapterMedicalType);
    }

    public void addMedicalInfoToDb() {
        String medId = mDatabase.push().getKey();
        String medType = mType.getSelectedItem().toString();
        String medName = mNameView.getText().toString().trim();
        String medNote = mNotesView.getText().toString().trim();
        String userUid = mAuth.getUid();

        MedInfo medInfo = new MedInfo(medId, medType, medName, medNote, userUid);

        mDatabase.child("MedicalInfo")
                .child(medId)
                .setValue(medInfo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Data Successfully Added", Toast.LENGTH_SHORT).show();
                            getDialog().dismiss();
                        } else {
                            Toast.makeText(getActivity(), "FAILED: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == mAddView) {
            addMedicalInfoToDb();
        } else if (v == mCancelView) {
            getDialog().dismiss();
        }
    }
}
