package de.oerntec.softwareprototyp.connection;

import de.oerntec.softwareprototyp.common.SensorType;

/**
 * Created by arne on 10/15/16.
 */

public class UdpDataConnection extends DataConnection {
    @Override
    void push() throws ConnectionException {

    }

    @Override
    public void push(SensorType type, float[] data) throws ConnectionException {

    }
}
