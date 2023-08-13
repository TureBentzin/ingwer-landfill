package de.bentzin.ingwer.landfill.netty;

import io.netty5.buffer.Buffer;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.handler.codec.ByteToMessageDecoder;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class PacketDecoder extends ByteToMessageDecoder {

    private final @NotNull PacketRegistry registry;

    public PacketDecoder(@NotNull PacketRegistry registry) {
        this.registry = registry;
    }

    @Override
    protected void decode(@NotNull ChannelHandlerContext channelHandlerContext, @NotNull Buffer buffer) throws Exception {
        System.out.println("decoding: " + buffer.toString(StandardCharsets.UTF_8));
        NettyUtils.hexdump(buffer);
        try(buffer) {
            if(buffer.readableBytes() < 8) return;
            int length = buffer.readInt();
            int id = buffer.readInt();
            try(final Buffer data = buffer.readSplit(length - 4)) {
                Packet apply = registry.getConstructors().get(id).apply(data);
                channelHandlerContext.fireChannelRead(apply);
            }
        }
    }

    @Override
    public void channelExceptionCaught(ChannelHandlerContext ctx, @NotNull Throwable cause) throws Exception {
        System.err.println("fuck: " + ctx.name() + " by " + cause);
    }
}
