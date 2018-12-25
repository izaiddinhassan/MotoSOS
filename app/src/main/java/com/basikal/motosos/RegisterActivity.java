package com.basikal.motosos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmailView, mPasswordView, mNameView, mIcView, mDobView, mPhoneView,
            mAddressView, mInsuranceNumberView, mInsurancePhoneNoView;
    private Button mRegisterButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProgressBar mProgressBar;
    private Spinner mGenderSpinner, mBloodTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loadToolbar();

        //create instances
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //link with xml
        mEmailView = findViewById(R.id.etEmail);
        mPasswordView = findViewById(R.id.etPassword);
        mProgressBar = findViewById(R.id.progressBar);
        mRegisterButton = findViewById(R.id.btnRegister);
        mGenderSpinner = findViewById(R.id.spGender);
        mBloodTypeSpinner = findViewById(R.id.spBloodType);
        mNameView = findViewById(R.id.etName);
        mIcView = findViewById(R.id.etIC);
        mDobView = findViewById(R.id.etDOB);
        mPhoneView = findViewById(R.id.etPhoneNo);
        mAddressView = findViewById(R.id.etAddress);
        mInsuranceNumberView = findViewById(R.id.etInsurancePolicyNo);
        mInsurancePhoneNoView = findViewById(R.id.etInsurancePhoneNo);

        //set up spinner for gender and blood type
        loadSpinner();

        //add listener
        mRegisterButton.setOnClickListener(this);

        //auto fill date from ic number
        autoIcToDate();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void loadSpinner() {
        //set up spinner
        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGenderSpinner.setAdapter(adapterGender);

        ArrayAdapter<CharSequence> adapterBloodType = ArrayAdapter.createFromResource(this,
                R.array.blood_type_array, android.R.layout.simple_spinner_item);
        adapterBloodType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBloodTypeSpinner.setAdapter(adapterBloodType);
    }

    private void autoIcToDate() {
        mIcView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (mIcView.getText().toString().trim().length() < 12) {
                        mIcView.setError("Failed");
                    } else {
                        // your code here
                        mIcView.setError(null);
                    }
                } else {
                    if (mIcView.getText().toString().trim().length() < 12 || mIcView != null) {
                        mIcView.setError("Failed");
                    } else {
                        // your code here
                        mIcView.setError(null);
                    }
                }
            }
        });
        mIcView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ic = s.toString();
                if (ic.length() >= 12) {
                    String year = ic.substring(0, 2);
                    String month = ic.substring(2, 4);
                    String day = ic.substring(4, 6);
                    String dob = day + "-" + month + "-" + year;
                    mDobView.setText(dob);
                }
            }
        });
    }

    private void registerUserToAuth() {
        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show();
            mProgressBar.setVisibility(View.INVISIBLE);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            mProgressBar.setVisibility(View.INVISIBLE);
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            registerUserToDb();
                            mProgressBar.setVisibility(View.INVISIBLE);
                        } else {
                            Toast.makeText(RegisterActivity.this,
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                }
        );
    }

    private void registerUserToDb() {
        String uid = mAuth.getCurrentUser().getUid();
        String name = mNameView.getText().toString().trim();
        String email = mAuth.getCurrentUser().getEmail();
        String icNo = mIcView.getText().toString().trim();
        String phoneNo = mPhoneView.getText().toString().trim();
        String address = mAddressView.getText().toString().trim();
        String dob = mDobView.getText().toString().trim();
        String gender = mGenderSpinner.getSelectedItem().toString();
        String blood_type = mBloodTypeSpinner.getSelectedItem().toString();
        String insurance_number = mInsuranceNumberView.getText().toString().trim();
        String insurance_phone_no = mInsurancePhoneNoView.getText().toString().trim();

        User user = new User(uid, name, email, icNo, phoneNo, address, dob,
                gender, blood_type, insurance_number, insurance_phone_no);

        mDatabase.child("User")
                .child(uid)
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this,
                                    "Register Success", Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(RegisterActivity.this,
                                    MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this,
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == mRegisterButton) {
            mProgressBar.setVisibility(View.VISIBLE);
            registerUserToAuth();
        }
    }
}
