package de.oerntec.softwareprototyp.common.messages;

import de.oerntec.softwareprototyp.common.CommandType;
import de.oerntec.softwareprototyp.common.SensorType;

/**
 * Created by arne on 10/15/16.
 */

public class SetSensorState extends Command {
    SetSensorState(CommandType commandType, SensorType sensorType, boolean enable) {
        super(commandType);

        mEnable = enable;
        mSensorType = sensorType;
    }

    boolean getEnable() {
        return mEnable;
    }

    SensorType getSensorType(){
        return mSensorType;
    }

    private boolean mEnable;
    private SensorType mSensorType;
}
