package com.basikal.motosos;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LockscreenActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mNameView, mPhoneNumberView, mAddressView;
    private Button mCloseButton, mCancelButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private CountDownTimer mCountDownTimer;
    private TextView mCountdownView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockscreen);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = DbConn.getDatabase().getReference();

        mNameView = findViewById(R.id.tvName);
        mPhoneNumberView = findViewById(R.id.tvPhoneNo);
        mAddressView = findViewById(R.id.tvAddress);
        mCancelButton = findViewById(R.id.btnCancel);
        mCloseButton = findViewById(R.id.btnClose);
        mCountdownView = findViewById(R.id.tvCountdown);

        mCancelButton.setOnClickListener(this);
        mCloseButton.setOnClickListener(this);

        String uid = mAuth.getUid();

        mDatabase.child("User").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mNameView.setText(user.name);
                mPhoneNumberView.setText(user.phoneNo);
                mAddressView.setText(user.address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mCountDownTimer = new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                mCountdownView.setText(String.valueOf(millisUntilFinished / 1000));
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.detection_status), "off");
                editor.apply();
                mCancelButton.setVisibility(View.GONE);
                mCloseButton.setVisibility(View.VISIBLE);
                sendSms();
                mCountdownView.setText(R.string.sms_delivered_message);

            }
        };

        mCountDownTimer.start();
    }

    private void sendSms() {
        String phoneNumber = "+60196142432";
        String smsMessage = "Ali involved in an accident in http://maps.google.com/?q=4.2316392,101.99187789999996. Please send help!!";

        String SMS_SENT = "SMS_SENT";
        String SMS_DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_SENT), 0);
        PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_DELIVERED), 0);

        // For when the SMS has been sent
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS sent successfully", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure cause", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "Service is currently unavailable", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "No pdu provided", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio was explicitly turned off", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SMS_SENT));

        // For when the SMS has been delivered
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SMS_DELIVERED));

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, smsMessage, sentPendingIntent, deliveredPendingIntent);
    }

    @Override
    public void onClick(View v) {
        if (v == mCancelButton) {
            mCountDownTimer.cancel();
            mCancelButton.setVisibility(View.GONE);
            mCloseButton.setVisibility(View.VISIBLE);
            mCountdownView.setText("Emergency SMS cancelled");
        } else if (v == mCloseButton) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
