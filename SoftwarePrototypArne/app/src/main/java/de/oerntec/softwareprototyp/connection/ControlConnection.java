package de.oerntec.softwareprototyp.connection;

import java.util.List;

import de.oerntec.softwareprototyp.common.SensorType;

/**
 * Created by arne on 10/15/16.
 */

public interface ControlConnection {
    List<Server> findServers();
    boolean connect(Server s);

    interface EnableMotionListener {
        void onEnableMotion(SensorType s, boolean enable);
    }
}
