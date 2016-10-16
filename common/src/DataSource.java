/**
 * Interface for sensor data sources
 */
public interface DataSource {

    /**
     * Save the data sink to be used for pushing new data
     * @param sink any implementation of the DataSink interface
     */
    void setDataSink(DataSink sink);
}
