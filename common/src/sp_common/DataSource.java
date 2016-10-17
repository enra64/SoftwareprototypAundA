package sp_common;

/**
 * Interface for sensor data sources
 */
public interface DataSource {

    /**
     * Save the data sink to be used for pushing new data
     * @param sink any implementation of the sp_common.DataSink interface
     */
    void setDataSink(DataSink sink);

    /**
     * called when the source is no longer used
     */
    void close();
}
