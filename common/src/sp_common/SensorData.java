package sp_common;

import sun.management.Sensor;

import java.io.Serializable;
import java.util.Arrays;

/**
 * This class encapsulates all data for a single sensor event
 */
public class SensorData implements Serializable, Cloneable {
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

    @Override
    protected SensorData clone() throws CloneNotSupportedException {
        // have to do it this way to ensure proper initialization
        SensorData cloned = (SensorData) super.clone();
        cloned.sensorType = sensorType;
        cloned.data = data.clone();
        cloned.timestamp = timestamp;
        cloned.accuracy = accuracy;
        return cloned;
    }

    public SensorType sensorType;
    public float[] data;
    public long timestamp;
    public int accuracy;
}
