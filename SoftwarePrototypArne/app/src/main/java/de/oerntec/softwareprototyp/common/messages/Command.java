package de.oerntec.softwareprototyp.common.messages;

import de.oerntec.softwareprototyp.common.CommandType;

/**
 * Created by arne on 10/15/16.
 */

public abstract class Command extends Message {
    Command(CommandType type){
        mCommandType = type;
    }

    public CommandType getCommandType(){
        return mCommandType;
    }

    private CommandType mCommandType;
}
