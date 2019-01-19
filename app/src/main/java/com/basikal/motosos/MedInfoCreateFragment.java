package com.basikal.motosos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MedInfoCreateFragment extends Fragment implements View.OnClickListener {
    private DatabaseReference mDatabase;
    private Button mAddView, mCancelView;
    private FirebaseAuth mAuth;
    private Spinner mType;
    private EditText mNameView, mNotesView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form_med_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatabase = DbConn.getDatabase().getReference("MedicalInfo");
        mAuth = FirebaseAuth.getInstance();

        mType = view.findViewById(R.id.spType);
        mNameView = view.findViewById(R.id.etName);
        mNotesView = view.findViewById(R.id.etExtra);
        mAddView = view.findViewById(R.id.btnSave);
        mCancelView = view.findViewById(R.id.btnCancel);

        mAddView.setOnClickListener(this);
        mCancelView.setOnClickListener(this);

        ArrayAdapter<CharSequence> adapterMedicalType = ArrayAdapter.createFromResource(getActivity(),
                R.array.medical_type_array, android.R.layout.simple_spinner_item);
        adapterMedicalType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mType.setAdapter(adapterMedicalType);
    }

    public void upPage() {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new MedInfoFragment(), "FRAG_MED_INFO")
                .addToBackStack(null)
                .commit();
    }

    public void createMedInfo() {
        String id = mDatabase.push().getKey();
        String type = mType.getSelectedItem().toString();
        String name = mNameView.getText().toString().trim();
        String note = mNotesView.getText().toString().trim();
        String userId = mAuth.getUid();

        if (name.isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (note.isEmpty()) {
            note = "n/a";
        }

        MedInfo medInfo = new MedInfo(id, type, name, note, userId);
        mDatabase.child(id).setValue(medInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Medical Information Created", Toast.LENGTH_SHORT).show();
                    upPage();
                } else {
                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                createMedInfo();
                break;
            case R.id.btnCancel:
                upPage();
                break;
        }
    }
}
