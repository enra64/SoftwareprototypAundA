package de.oerntec.softwareprototyp.connection;

/**
 * Created by arne on 10/15/16.
 */

public abstract class DataConnection implements DataSink {
    abstract void push() throws ConnectionException;
}
