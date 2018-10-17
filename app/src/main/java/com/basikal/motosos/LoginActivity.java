package com.basikal.motosos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmailView, mPasswordView;
    private Button mLoginButton;
    private TextView mForgetPasswordView;
    private FirebaseAuth mAuth;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MotoSOS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //create instances
        mAuth = FirebaseAuth.getInstance();

        //link with xml
        mEmailView = findViewById(R.id.etEmail);
        mPasswordView = findViewById(R.id.etPassword);
        mLoginButton = findViewById(R.id.btnLogin);
        mForgetPasswordView = findViewById(R.id.tvForgetPassword);
        mProgressBar = findViewById(R.id.progressBar);

        //set listener
        mLoginButton.setOnClickListener(this);
        mForgetPasswordView.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void login() {
        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }


        mProgressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            //Already logged in
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mLoginButton) {
            login();
        } else if (v == mForgetPasswordView) {
            //forgot password method
        }
    }
}
