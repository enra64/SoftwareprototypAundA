package de.oerntec.udprototype;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import sp_common.DataSink;
import sp_common.DataSource;
import sp_common.SensorData;
import sp_common.SensorType;

/**
 * This class implements the DataSource interface for the accelerometer sensor
 */
class Accelerometer implements DataSource, SensorEventListener {
    /**
     * The android sensor manager used to connect to the accelerometer
     */
    private final SensorManager mSensorManager;

    /**
     * The sink data should be pushed into
     */
    private DataSink mSink;

    Accelerometer(Context context){
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void setDataSink(DataSink dataSink) {
        mSink = dataSink;
    }

    @Override
    public void close() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // if no sink is set yet, discard the SensorEvent
        if(mSink == null)
            return;

        // push data into sink
        mSink.onData(
                new SensorData(
                        SensorType.Accelerometer,
                        sensorEvent.values,
                        sensorEvent.timestamp,
                        sensorEvent.accuracy));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
