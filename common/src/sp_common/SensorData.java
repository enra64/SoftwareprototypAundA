package sp_common;

import sun.management.Sensor;

import java.io.Serializable;
import java.util.Arrays;

/**
 * This class encapsulates all data for a single sensor event
 */
public class SensorData implements Serializable {
    public SensorData(SensorType sensorType, float[] data, long timestamp, int accuracy) {
        this.sensorType = sensorType;
        this.data = data;
        this.timestamp = timestamp;
        this.accuracy = accuracy;
    }

    public SensorData() {
    }

    @Override
    public String toString() {
        // print the data as csv
        return Arrays.toString(data).replaceAll("[ \\[\\]]", "") + "," + timestamp + "," + accuracy;
    }

    public SensorType sensorType;
    public float[] data;
    public long timestamp;
    public int accuracy;
}
