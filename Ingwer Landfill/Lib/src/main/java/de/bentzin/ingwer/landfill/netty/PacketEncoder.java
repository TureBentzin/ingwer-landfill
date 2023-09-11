package de.bentzin.ingwer.landfill.netty;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */

import io.netty5.buffer.Buffer;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.handler.codec.MessageToByteEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;


public class PacketEncoder extends MessageToByteEncoder<Packet> {

    private final @NotNull PacketRegistry registry;
    private static final @NotNull Logger logger = LogManager.getLogger();

    public PacketEncoder(@NotNull PacketRegistry registry) {
        this.registry = registry;
    }

    @SuppressWarnings("resource")
    @Override
    protected @NotNull Buffer allocateBuffer(@NotNull ChannelHandlerContext ctx, @NotNull Packet msg) {
        return ctx.channel().bufferAllocator().allocate(1024); //depends on packet
    }


    @SuppressWarnings("resource")
    @Override
    protected void encode(@NotNull ChannelHandlerContext ctx, @NotNull Packet msg, @NotNull Buffer buffer) {
        logger.info("encoding... " + msg);
        Integer integer = registry.getReversedPackets().get(msg.getClass());
        Buffer data = ctx.channel().bufferAllocator().allocate(1024);
        msg.encode(data);
        data.readerOffset(0);
        buffer.writeInt(data.readableBytes() + 4);
        buffer.writeInt(integer);
        int size = data.readableBytes();
        if (buffer.writableBytes() < size) {
            buffer.ensureWritable(size, 1, false);
        }
        data.copyInto(0, buffer, buffer.writerOffset(), data.readableBytes());
        buffer.skipWritableBytes(size);
        logger.info("encoded to: " + buffer.toString(StandardCharsets.UTF_8));
        NettyUtils.hexdump(buffer);
    }

   @Override
    public void channelExceptionCaught(ChannelHandlerContext ctx, @NotNull Throwable cause) throws Exception {
        logger.error("fuck: " + ctx.name() + " by " + cause);
    }

}
