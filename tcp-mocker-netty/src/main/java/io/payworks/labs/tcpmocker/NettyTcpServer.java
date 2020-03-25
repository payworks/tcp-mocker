package io.payworks.labs.tcpmocker;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.net.InetSocketAddress;

public class NettyTcpServer extends AbstractTcpServer {

    private final Channel channel;

    NettyTcpServer(final ChannelFuture bindFuture) throws Exception {
        super(((InetSocketAddress) bindFuture.sync().channel().localAddress()).getPort());
        this.channel = bindFuture.channel();
    }

    @Override
    public void close() {
        try {
            channel.close().sync();
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public static NettyTcpServerBuilder builder() {
        return new NettyTcpServerBuilder();
    }
}
