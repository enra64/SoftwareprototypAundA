package de.oerntec.softwareprototyp.connection;

import de.oerntec.softwareprototyp.common.SensorType;

/**
 * Created by arne on 10/15/16.
 */

public interface DataSink {
    void push(SensorType type, float[] data) throws ConnectionException;
}
