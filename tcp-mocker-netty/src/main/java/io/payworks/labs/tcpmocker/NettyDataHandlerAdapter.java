package io.payworks.labs.tcpmocker;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.payworks.labs.tcpmocker.datahandler.DataHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class NettyDataHandlerAdapter extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(NettyDataHandlerAdapter.class);

    private final DataHandler dataHandler;

    private ByteBuf readBuf;
    private ByteBuf writeBuf;

    NettyDataHandlerAdapter(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        logger.info("Connection Established");
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) {
        alloc(ctx);

        if (msg instanceof ByteBuf) {
            readBytes((ByteBuf) msg);

            if (handle()) {
                writeBytes(ctx);
            }
        } else {
            throw new UnsupportedOperationException(String.format("Unsupported Message: %s", msg.getClass()));
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        logger.error("Connection Closed", cause);
        ctx.close();
    }

    private void alloc(ChannelHandlerContext ctx) {
        if (readBuf == null) readBuf = ctx.alloc().buffer();
        if (writeBuf == null) writeBuf = ctx.alloc().buffer();
    }

    private void readBytes(ByteBuf byteBuf) {
        readBuf.writeBytes(byteBuf);
        byteBuf.release();
    }

    private boolean handle() {
        Optional<byte[]> handleResult = dataHandler.handle(getBytes(readBuf));

        handleResult
                .ifPresent(bytes -> {
                    writeBuf.writeBytes(bytes);

                    readBuf.release();
                    readBuf = null;
                });

        return handleResult.isPresent();
    }

    private static byte[] getBytes(ByteBuf byteBuf) {
        if (byteBuf.hasArray()) {
            return byteBuf.array();
        } else {
            byte[] data = new byte[byteBuf.readableBytes()];
            byteBuf.getBytes(0, data);
            return data;
        }
    }

    private void writeBytes(ChannelHandlerContext ctx) {
        if (writeBuf.writerIndex() == 0) {
            writeBuf = null;
            ctx.close();
        }
        else {
            ctx.write(writeBuf)
                    .addListener(f -> {
                        if (!f.isSuccess()) {
                            logger.error("Unexpected Error!", f.cause());
                        }
                    });
            writeBuf = null;
        }
    }
}
