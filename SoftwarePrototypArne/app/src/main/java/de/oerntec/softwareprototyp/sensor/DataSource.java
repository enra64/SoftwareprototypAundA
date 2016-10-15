package de.oerntec.softwareprototyp.sensor;

import de.oerntec.softwareprototyp.connection.DataSink;

/**
 * Created by arne on 10/15/16.
 */
public interface DataSource {
    void setUpdateSink(DataSink sink);
}