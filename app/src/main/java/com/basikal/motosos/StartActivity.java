package com.basikal.motosos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mLoginView, mRegisterView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mAuth = FirebaseAuth.getInstance();

        mLoginView = findViewById(R.id.tvLogin);
        mRegisterView = findViewById(R.id.tvRegister);

        mLoginView.setOnClickListener(this);
        mRegisterView.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            //Already logged in
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mLoginView) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (view == mRegisterView) {
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }
}
