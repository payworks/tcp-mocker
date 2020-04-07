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
        bootstrap(ctx);

        if (msg instanceof ByteBuf) {
            readMsgToReadBuf((ByteBuf) msg);

            if (handleToWriteBuf()) {
                flushWriteBuf(ctx);
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

    private void bootstrap(ChannelHandlerContext ctx) {
        if (readBuf == null) {
            readBuf = ctx.alloc().buffer();
        }
        
        if (writeBuf == null) {
            writeBuf = ctx.alloc().buffer();
        }
    }

    private void readMsgToReadBuf(ByteBuf byteBuf) {
        readBuf.writeBytes(byteBuf);
        byteBuf.release();
    }

    private boolean handleToWriteBuf() {
        Optional<byte[]> handleResult = tryHandle();

        if (handleResult.isEmpty()) {
            return false;
        } else {
            readBuf.release();
            readBuf = null;
            
            writeBuf.writeBytes(handleResult.get());
            
            return true;
        }
    }

    private Optional<byte[]> tryHandle() {
        return dataHandler.handle(toByteArray(readBuf));
    }

    private static byte[] toByteArray(ByteBuf byteBuf) {
        if (byteBuf.hasArray()) {
            return byteBuf.array();
        } else {
            byte[] data = new byte[byteBuf.readableBytes()];
            byteBuf.getBytes(0, data);
            return data;
        }
    }

    private void flushWriteBuf(ChannelHandlerContext ctx) {
        final ByteBuf writeBufRef = writeBuf;
        writeBuf = null;

        if (writeBufRef.writerIndex() != 0) {
            ctx.write(writeBufRef)
                    .addListener(f -> {
                        if (!f.isSuccess()) {
                            logger.error("Unexpected Error!", f.cause());
                        }
                    });
        } else {
            writeBufRef.release();
            ctx.close();
        }
    }
}
