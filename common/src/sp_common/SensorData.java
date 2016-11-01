package sp_common;

import java.io.Serializable;
import java.util.Arrays;

/**
 * This class encapsulates all data for a single sensor event
 */
@SuppressWarnings("WeakerAccess")
public class SensorData implements Serializable, Cloneable {
    public SensorType sensorType;
    public float[] data;
    public long timestamp;
    public int accuracy;

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

    /**
     * Returns a properly cloned SensorData object
     */
    @Override
    public SensorData clone() {
        // have to do it this way to ensure proper initialization
        SensorData cloned = null;

        // try { clone() } to avoid throws in signature
        try {
            cloned = (SensorData) super.clone();
            cloned.sensorType = sensorType;
            cloned.data = data.clone();
            cloned.timestamp = timestamp;
            cloned.accuracy = accuracy;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        // this cannot be reached, as we inherit directly from Object
        return cloned;
    }
}
