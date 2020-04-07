package io.payworks.labs.tcpmocker;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;

import java.net.InetSocketAddress;

public class NettyTcpServer extends AbstractTcpServer {

    private final Channel channel;
    private final EventLoopGroup acceptGroup;
    private final EventLoopGroup connectGroup;

    NettyTcpServer(final ChannelFuture bindFuture,
                   final EventLoopGroup acceptGroup,
                   final EventLoopGroup connectGroup) throws Exception {
        super(((InetSocketAddress) bindFuture.sync().channel().localAddress()).getPort());
        channel = bindFuture.channel();
        this.acceptGroup = acceptGroup;
        this.connectGroup = connectGroup;
    }

    @Override
    public void close() {
        try {
            if (channel.isOpen()) {
                channel.close().sync();
            }

            acceptGroup.shutdownGracefully().syncUninterruptibly();
            connectGroup.shutdownGracefully().syncUninterruptibly();
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public static NettyTcpServerBuilder builder() {
        return new NettyTcpServerBuilder();
    }
}
