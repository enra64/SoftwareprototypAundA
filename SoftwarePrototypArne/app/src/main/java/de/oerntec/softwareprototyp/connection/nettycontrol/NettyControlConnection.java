package de.oerntec.softwareprototyp.connection.nettycontrol;

import java.util.List;

import de.oerntec.softwareprototyp.common.messages.Command;
import de.oerntec.softwareprototyp.connection.ControlConnection;
import de.oerntec.softwareprototyp.connection.Server;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class NettyControlConnection implements ControlConnection {
    public interface CommandSink {
        void onCommand(Command m);
    }

    EventLoopGroup mWorkerGroup = new NioEventLoopGroup();


    NettyControlConnection(final CommandSink sink) {
        Bootstrap b = new Bootstrap();
        b.group(mWorkerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("encoder", new ObjectEncoder());
                        ch.pipeline().addLast("decoder", new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                        ch.pipeline().addLast("encoder", new CommandHandler(sink));
                    }
                });
    }

    @Override
    public List<Server> findServers() {
        return null;
    }

    @Override
    public boolean connect(Server s) {
        return false;
    }
}
