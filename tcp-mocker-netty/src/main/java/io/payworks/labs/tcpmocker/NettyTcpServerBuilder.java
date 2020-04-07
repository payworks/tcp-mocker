package io.payworks.labs.tcpmocker;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

public class NettyTcpServerBuilder extends DispatchingDataHandlerServerBuilder<NettyTcpServer, NettyTcpServerBuilder> {

    private final int acceptThreads;
    private final int connectThreads;
    private final int queueSize;

    public static NettyTcpServerBuilder create(final int acceptThreads,
                                               final int connectThreads,
                                               final int queueSize) {
        return new NettyTcpServerBuilder(acceptThreads, connectThreads, queueSize);
    }

    NettyTcpServerBuilder(final int acceptThreads,
                          final int connectThreads,
                          final int queueSize) {
        this.acceptThreads = acceptThreads;
        this.connectThreads = connectThreads;
        this.queueSize = queueSize;
    }

    NettyTcpServerBuilder() {
        this(1, 5, 100);
    }

    @Override
    public NettyTcpServer build() {
        try {
            return createNettyTcpServer();
        } catch (final Exception e) {
            throw new TcpServerException(e);
        }
    }

    private NettyTcpServer createNettyTcpServer() throws Exception {
        final DefaultThreadFactory acceptThreadFactory = new DefaultThreadFactory("accept");
        final DefaultThreadFactory connectThreadFactory = new DefaultThreadFactory("connect");

        final EventLoopGroup acceptGroup = new NioEventLoopGroup(acceptThreads, acceptThreadFactory);
        final EventLoopGroup connectGroup = new NioEventLoopGroup(connectThreads, connectThreadFactory);

        final ServerBootstrap bootstrap = new ServerBootstrap()
                .group(acceptGroup, connectGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, queueSize)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(final SocketChannel ch) {
                        final ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new NettyDataHandlerAdapter(createDataHandlerDispatcher()));
                    }
                });

        final NettyTcpServer nettyTcpServer = new NettyTcpServer(bootstrap.bind(getPort()), acceptGroup, connectGroup);

        Runtime.getRuntime().addShutdownHook(new Thread(nettyTcpServer::close));

        return nettyTcpServer;
    }

    @Override
    protected NettyTcpServerBuilder self() {
        return this;
    }
}
