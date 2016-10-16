import java.io.Serializable;

/**
 * This class encapsulates all data for a single sensor event
 */
public class SensorData implements Serializable {
    public float[] data;
    public SensorType sensorType;
    public long timestamp;
    public int accuracy;
}
