package com.basikal.motosos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mNameView, mEmailView, mPasswordView, mPhoneView, mAddressView;
    private TextView mLoginView;
    private Button mRegisterButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //create instance
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //link with xml
        mNameView = findViewById(R.id.etName);
        mEmailView = findViewById(R.id.etEmail);
        mPasswordView = findViewById(R.id.etPassword);
        mPhoneView = findViewById(R.id.etPhoneNo);
        mAddressView = findViewById(R.id.etAddress);
        mLoginView = findViewById(R.id.tvLogin);
        mRegisterButton = findViewById(R.id.btnRegister);
        mProgressBar = findViewById(R.id.progressBar);

        //add listener
        mLoginView.setOnClickListener(this);
        mRegisterButton.setOnClickListener(this);
    }

    private void register() {
        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();
        String name = mNameView.getText().toString().trim();
        String phoneNo = mPhoneView.getText().toString().trim();
        String address = mAddressView.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phoneNo)) {
            Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Please enter valid address", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    registerUserInfo();
                    finish();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                } else {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUserInfo() {
        String uid = mAuth.getCurrentUser().getUid();
        String email = mAuth.getCurrentUser().getEmail();
        String name = mNameView.getText().toString().trim();
        String phoneNo = mPhoneView.getText().toString().trim();
        String address = mAddressView.getText().toString().trim();

        User user = new User(uid, name, email, phoneNo, address);
        mDatabase.child("User").child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mRegisterButton) {
            register();
        } else {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
