package de.bentzin.ingwer.landfill.netty;

import de.bentzin.ingwer.landfill.netty.packet.response.MalformedDataPacket;
import io.netty5.buffer.Buffer;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class PacketDecoder extends ByteToMessageDecoder {

    private final @NotNull PacketRegistry registry;
    private static final @NotNull Logger logger = LogManager.getLogger();

    public PacketDecoder(@NotNull PacketRegistry registry) {
        this.registry = registry;
    }

    @Override
    protected void decode(@NotNull ChannelHandlerContext channelHandlerContext, @NotNull Buffer buffer) throws Exception {
        logger.info("decoding: " + buffer.toString(StandardCharsets.UTF_8));
        NettyUtils.hexdump(buffer);
        try {


        try(buffer) {
            if(buffer.readableBytes() < 8) return;
            int length = buffer.readInt();
            int id = buffer.readInt();
            try(final Buffer data = buffer.readSplit(length - 4)) {
                Packet apply = registry.getConstructors().get(id).apply(data);
                channelHandlerContext.fireChannelRead(apply);
            }
        }

        }catch (RuntimeException runtimeException) {
            if(runtimeException.getCause() instanceof IndexOutOfBoundsException e){
                channelHandlerContext.channel().write(new MalformedDataPacket(String.valueOf(e)));
                channelHandlerContext.channel().flush();
            }
        }
    }

    @Override
    public void channelExceptionCaught(ChannelHandlerContext ctx, @NotNull Throwable cause) throws Exception {
        logger.error("fuck: " + ctx.name() + " by " + cause);
    }
}
