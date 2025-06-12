package com.s23010621.repository;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorRepository implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor temperatureSensor;
    private TemperatureCallback callback;
    private Context context;

    public SensorRepository(Context context)  {
        this.context = context;
    }
    public void setTemperatureCallback(TemperatureCallback callback) {
        this.callback = callback;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (temperatureSensor != null) {
            sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public interface TemperatureCallback {
        void onTemperatureChanged(float temperature);
    }
    public void unregister() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float temp = event.values[0];
            callback.onTemperatureChanged(temp);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}