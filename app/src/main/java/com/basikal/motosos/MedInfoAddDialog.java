package com.basikal.motosos;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
        return inflater.inflate(R.layout.dialog_add_med_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase = DbConn.getDatabase().getReference();
        mAuth = FirebaseAuth.getInstance();

        mType = view.findViewById(R.id.spType);

        String[] spinnerValue = {"Allergy", "Medical Condition", "Medication"};
        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, spinnerValue);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mType.setAdapter(aa);


        /*mTypeTitleView = view.findViewById(R.id.tvIceTitle);
        mTypeNameView = view.findViewById(R.id.tvIceName);
        mAddView = view.findViewById(R.id.tvAdd);
        mCancelView = view.findViewById(R.id.tvCancel);

        mAddView.setOnClickListener(this);
        mCancelView.setOnClickListener(this);

        mType = getArguments().getString("type");
        Toast.makeText(getActivity(), mType, Toast.LENGTH_SHORT).show();
        if (mType.equalsIgnoreCase("Medication")) {
            mTypeTitleView.setText("Medication");
            mTypeNameView.setText("Medication Name");
        } else if (mType.equalsIgnoreCase("Allergy")) {
            mTypeTitleView.setText("Allergy");
            mTypeNameView.setText("Allergy Name");
        }*/
    }

    public void addMedicalInfoToDb() {
        String uid = mAuth.getUid();
        String medType = mType.getSelectedItem().toString();
    }

    /*public void addIceInfo() {
        String uid = mAuth.getCurrentUser().getUid();
        String medicationUid = mDatabase.child("Medication").push().getKey();
        String medicationName = "Tester la";
        String conditionUid = mDatabase.child("Medication").push().getKey();
        String conditionName = "Tester la";
        String allergyUid = mDatabase.child("Medication").push().getKey();
        String allergyName = "Tester la";

        mType = getArguments().getString("type");
        if (mType.equalsIgnoreCase("Medication")) {
            MedInfo medInfo = new MedInfo(medicationUid, medicationName);
            mDatabase.child("Medication").child(uid).child(medicationUid).setValue(medInfo);
            getDialog().dismiss();
        } else if (mType.equalsIgnoreCase("Allergy")) {
            Medication medication = new Medication(medicationUid, medicationName);
            mDatabase.child("Allergy").child(uid).child(medicationUid).setValue(medication);
            getDialog().dismiss();
        } else {
            Medication medication = new Medication(medicationUid, medicationName);
            mDatabase.child("MedicalCondition").child(uid).child(medicationUid).setValue(medication);
            getDialog().dismiss();
        }
    }*/

    @Override
    public void onClick(View v) {
        if (v == mAddView) {
            addMedicalInfoToDb();
        } else if (v == mCancelView) {
            getDialog().dismiss();
        }
    }
}
