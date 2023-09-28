package com.example.sensorserver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.Random;

public class MyService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private final String TAG = "MYSERVICE";
    private float magneticFieldValueX;
    private float magneticFieldValueY;
    private float magneticFieldValueZ;
    private Sensor mSensor;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED) != null){
            mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED);
            Log.d(TAG, "Connected to Magnetic Field Sensor ");
            sensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_NORMAL);
        } else{
            Log.d(TAG, "There is no Magnetic Field Sensor ");
        }

        return binder;
    }

    IVehicleAidlInterface.Stub binder = new IVehicleAidlInterface.Stub() {
        @Override
        public float getMagneticFieldValueX() throws RemoteException {
            return magneticFieldValueX;
        }
        public float getMagneticFieldValueY() throws RemoteException {
            return magneticFieldValueY;
        }
        public float getMagneticFieldValueZ() throws RemoteException {
            return magneticFieldValueZ;
        }
    };

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG,"Magnetic Field Values- X: " + event.values[0]+ " ,Y: "+ event.values[1]+ " ,Z: "+ event.values[2]);
        magneticFieldValueX = event.values[0];
        magneticFieldValueY = event.values[1];
        magneticFieldValueZ = event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}