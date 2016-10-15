package de.oerntec.softwareprototyp.connection;

import java.util.List;

/**
 * Created by arne on 10/15/16.
 */

public class TcpControlConnection implements ControlConnection {
    @Override
    public List<Server> findServers() {
        return null;
    }

    @Override
    public boolean connect(Server s) {
        return false;
    }
}
