package de.oerntec.softwareprototyp.connection.nettycontrol;

import de.oerntec.softwareprototyp.common.messages.Command;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by arne on 10/15/16.
 */

public class CommandHandler extends ChannelInboundHandlerAdapter {
    private final NettyControlConnection.CommandSink mCommandSink;

    CommandHandler(NettyControlConnection.CommandSink sink){
        mCommandSink = sink;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Command c = (Command) msg;
        mCommandSink.onCommand(c);
        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}