package com.basikal.motosos;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import static com.basikal.motosos.App.CHANNEL_ID;

public class DetectionService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer, mGyro;
    private Float mAcceleration = 0.0f;
    private Boolean mGyroscope = false;
    private Float mGyroscopeValue = 0.0f;

    @Override
    public void onCreate() {
        super.onCreate();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        mSensorManager.registerListener(DetectionService.this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(DetectionService.this, mGyro, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Detection Service")
                .setContentText("Detection Service is currently running")
                .setSmallIcon(R.drawable.ic_motorcycle)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void detectAccident() {
        if (mAcceleration > 1 && mGyroscope == true) {
            Toast.makeText(this, "Accident Detected", Toast.LENGTH_LONG).show();
            mSensorManager.unregisterListener(this);
            stopSelf();

            Intent intent = new Intent(this, LockScreenActivity.class);
            intent.putExtra("ACCELERATION_VALUE", mAcceleration);
            intent.putExtra("GYROSCOPE_VALUE", mGyroscopeValue);
            startActivity(intent);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            float ax = sensorEvent.values[0];
            float ay = sensorEvent.values[1];
            float az = sensorEvent.values[2];
            mAcceleration = ax + ay + az;
        } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float gx = sensorEvent.values[0];
            float gy = sensorEvent.values[1];
            float gz = sensorEvent.values[2];
            float diffGyro = Math.abs(mGyroscopeValue - gx);
            if (diffGyro > 3) {
                mGyroscope = true;
            } else {
                mGyroscope = false;
            }
            mGyroscopeValue = gx;
        }
        detectAccident();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
