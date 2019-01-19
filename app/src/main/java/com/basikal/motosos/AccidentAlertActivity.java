package com.basikal.motosos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jintin.mixadapter.MixAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AccidentAlertActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LockScreen Activity";
    private TextView mNameView, mIcNoView, mAddressView, mDobView, mGenderView, mBloodTypeView, mInsPolicy, mInsPhoneNo;
    private Button mCloseButton, mCancelButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private CountDownTimer mCountDownTimer;
    private TextView mCountdownView;
    private Double mLat = 0d, mLong = 0d;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Float mAcceleration = 0.0f;
    private Float mGyroscopeValue = 0.0f;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mAdapter2;
    private List<MedInfo> mMedInfoList;
    private List<EmergencyContact> mEmergencyContactList;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_alert);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //set up for full screen on lock screen
        lockScreenSetup();

        //instantiate object
        mAuth = FirebaseAuth.getInstance();
        mDatabase = DbConn.getDatabase().getReference();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mRecyclerView = findViewById(R.id.rv);

        //link with xml
        mNameView = findViewById(R.id.tvName);
        mIcNoView = findViewById(R.id.tvIcNo);
        mAddressView = findViewById(R.id.tvAddress);
        mDobView = findViewById(R.id.tvDob);
        mGenderView = findViewById(R.id.tvGender);
        mBloodTypeView = findViewById(R.id.tvBloodType);
        mInsPolicy = findViewById(R.id.tvInsPolicyNumber);
        mInsPhoneNo = findViewById(R.id.tvInsPhoneNumber);
        mCancelButton = findViewById(R.id.btnCancel);
        mCloseButton = findViewById(R.id.btnClose);
        mCountdownView = findViewById(R.id.tvCountdown);

        //attach listener
        mCancelButton.setOnClickListener(this);
        mCloseButton.setOnClickListener(this);

        //get value from previous activity/services/fragment
        getBundleData();

        //get user data from FireBase
        getUserData();

        //start countdown
        countdownTimer();

        //location callback
        getLocationCallback();

        //request current location
        requestLocationUpdates();
    }

    private void getBundleData() {
        Bundle extras = getIntent().getExtras();
        mAcceleration = extras.getFloat("ACCELERATION_VALUE");
        mGyroscopeValue = extras.getFloat("GYROSCOPE_VALUE");
    }

    private void lockScreenSetup() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        getWindow().addFlags(//WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    //location callback
    private LocationCallback getLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    mLat = location.getLatitude();
                    mLong = location.getLongitude();
                    Log.d(TAG, "onLocationResult: " + mLat + " " + mLong);
                }
                //delay remove location function to get more accurate reading
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                        }
                    }
                }, 12000);//time in millisecond
            }
        };
        return mLocationCallback;
    }

    @SuppressLint("MissingPermission")
    public void requestLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, getLocationCallback(), Looper.myLooper());
    }

    private void getUserData() {
        String uid = mAuth.getUid();
        mDatabase.child("User").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mNameView.setText(user.name);
                mIcNoView.setText(user.icNo);
                mAddressView.setText(user.address);
                mDobView.setText(user.dob);
                mGenderView.setText(user.gender);
                mBloodTypeView.setText(user.bloodType);
                mInsPolicy.setText(user.insurancePolicy);
                mInsPhoneNo.setText(user.insurancePhone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        readEmergencyContactInfoFromDB();
    }

    private void readEmergencyContactInfoFromDB() {
        final String id = mAuth.getUid();
        mDatabase.child("MedicalInfo")
                .orderByChild("userId")
                .equalTo(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //iterating through all the values in database
                        mMedInfoList = new ArrayList<>();
                        for (DataSnapshot medInfoSnapshot : dataSnapshot.getChildren()) {
                            MedInfo medInfo = medInfoSnapshot.getValue(MedInfo.class);
                            mMedInfoList.add(medInfo);
                        }

                        mDatabase.child("EmergencyContact")
                                .orderByChild("userId")
                                .equalTo(id)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        //iterating through all the values in database
                                        mEmergencyContactList = new ArrayList<>();
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            EmergencyContact emergencyContact = ds.getValue(EmergencyContact.class);
                                            mEmergencyContactList.add(emergencyContact);
                                            Log.d(TAG, "onDataChange: " + mEmergencyContactList.toString());
                                        }
                                        //creating adapter
                                        mAdapter = new EmergencyContactAdapter(getApplicationContext(), mEmergencyContactList, "Accident");

                                        //creating adapter
                                        mAdapter2 = new MedInfoAdapter(getApplicationContext(), mMedInfoList, "Accident");

                                        MixAdapter<RecyclerView.ViewHolder> adapter = new MixAdapter<>();
                                        adapter.addAdapter(mAdapter);
                                        adapter.addAdapter(mAdapter2);

                                        //adding adapter to recyclerView
                                        mRecyclerView.setAdapter(adapter);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        mRecyclerView.setHasFixedSize(true); //set fixed size for element in recycler view
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void countdownTimer() {
        mCountDownTimer = new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                mCountdownView.setText(String.valueOf(millisUntilFinished / 1000));
                //here you can have your logic to set text to edit text
            }

            public void onFinish() {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AccidentAlertActivity.this);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(getString(R.string.detection_status), "off");
                editor.apply();

                mCancelButton.setVisibility(View.GONE);
                mCloseButton.setVisibility(View.VISIBLE);
                sendSms();
                String smsDeliveryMessage = "An emergency message has been sent to emergency contact number.";
                //mCountdownView.setText(R.string.sms_delivered_message);
                mCountdownView.setText(smsDeliveryMessage);
                //save accident to log(database)
                createLog();

            }
        };
        mCountDownTimer.start();
    }

    private void sendSms() {


        String name = mNameView.getText().toString().trim();
        String smsMessage = name + " involved in an accident in http://maps.google.com/?q=" + mLat + "," + mLong + ". Please send help!!";

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

        List<String> emcPhoneNoList = new ArrayList<>();
        for (EmergencyContact emergencyContact : mEmergencyContactList) {
            String emcPhoneNo = emergencyContact.emcPhoneNo;
            emcPhoneNoList.add(emcPhoneNo);
        }

        SmsManager smsManager = SmsManager.getDefault();
        for (String phone_no : emcPhoneNoList) {
            smsManager.sendTextMessage(phone_no, null, smsMessage, sentPendingIntent, deliveredPendingIntent);
        }

    }

    private void createLog() {
        String uid = mDatabase.child("Logs").push().getKey();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("H:m:s", Locale.getDefault()).format(new Date());
        String latitude = Double.toString(mLat);
        String longitude = Double.toString(mLong);
        String gyro = Float.toString(mAcceleration);
        String accelerometer = Float.toString(mGyroscopeValue);
        String status = "true";
        String userUid = mAuth.getCurrentUser().getUid();
        Logs logs = new Logs(uid, date, time, latitude, longitude, gyro, accelerometer, status, userUid);

        mDatabase.child("Logs").child(uid).setValue(logs).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AccidentAlertActivity.this, "Saved successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AccidentAlertActivity.this, "Saved Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveAccidentInformationWhenCancel() {
        String uid = mDatabase.child("Logs").push().getKey();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("H:m:s", Locale.getDefault()).format(new Date());
        String latitude = Double.toString(mLat);
        String longitude = Double.toString(mLong);
        String gyro = Float.toString(mAcceleration);
        String accelerometer = Float.toString(mGyroscopeValue);
        String status = "false";
        String userUid = mAuth.getCurrentUser().getUid();
        Logs logs = new Logs(uid, date, time, latitude, longitude, gyro, accelerometer, status, userUid);

        mDatabase.child("Logs").child(uid).setValue(logs).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AccidentAlertActivity.this, "Saved successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AccidentAlertActivity.this, "Saved Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mCancelButton) {
            mCountDownTimer.cancel();
            mCancelButton.setVisibility(View.GONE);
            mCloseButton.setVisibility(View.VISIBLE);
            mCountdownView.setText("Emergency SMS cancelled");
            saveAccidentInformationWhenCancel();
        } else if (v == mCloseButton) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
