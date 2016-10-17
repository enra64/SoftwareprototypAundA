/**
 * Interface for sensor data sinks
 */
public interface DataSink {

    /**
     * onData is called whenever the sensor has received and processed new data
     */
    void onData(SensorData data);
}
