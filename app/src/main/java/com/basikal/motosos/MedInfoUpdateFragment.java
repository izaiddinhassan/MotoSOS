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
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class MedInfoUpdateFragment extends Fragment implements View.OnClickListener {
    private EditText mMedName, mMedExtra;
    private Spinner mTypeSpinner;
    private Button mSaveButton, mCancelButton;
    private Bundle mArgs;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form_med_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase = DbConn.getDatabase().getReference();

        mMedName = view.findViewById(R.id.etName);
        mMedExtra = view.findViewById(R.id.etExtra);
        mTypeSpinner = view.findViewById(R.id.spType);
        mSaveButton = view.findViewById(R.id.btnSave);
        mCancelButton = view.findViewById(R.id.btnCancel);

        mArgs = getArguments();
        String medType = mArgs.getString("medType");
        String medName = mArgs.getString("medName");
        String medExtra = mArgs.getString("medExtra");

        mMedName.setText(medName);
        mMedExtra.setText(medExtra);

        ArrayAdapter<CharSequence> adapterMedicalType = ArrayAdapter.createFromResource(getActivity(),
                R.array.medical_type_array, android.R.layout.simple_spinner_item);
        adapterMedicalType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(adapterMedicalType);

        switch (medType) {
            case "Allergy":
                mTypeSpinner.setSelection(0);
                break;
            case "Medical Condition":
                mTypeSpinner.setSelection(1);
                break;
            case "Medication":
                mTypeSpinner.setSelection(2);
                break;
        }

        mSaveButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);

    }

    public void updateMedicalInfo() {
        String medId = mArgs.getString("medId");
        String medType = mTypeSpinner.getSelectedItem().toString();
        String medName = mMedName.getText().toString().trim();
        String medExtra = mMedExtra.getText().toString().trim();

        HashMap<String, Object> map = new HashMap<>();
        map.put("medType", medType);
        map.put("medName", medName);
        map.put("medExtra", medExtra);

        mDatabase.child("MedicalInfo")
                .child(medId)
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            upPage();
                            Toast.makeText(getActivity(), "Medical Information Updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void upPage() {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new MedInfoFragment(), "FRAG_MED_INFO")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                updateMedicalInfo();
                break;
            case R.id.btnCancel:
                upPage();
                break;
        }
    }
}
