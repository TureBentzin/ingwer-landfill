package de.bentzin.ingwer.landfill.netty;

import de.bentzin.ingwer.landfill.netty.handler.KnockKnockHandler;
import de.bentzin.ingwer.landfill.netty.handler.PacketHandler;
import de.bentzin.ingwer.landfill.netty.packet.KnockKnockPacket;
import de.bentzin.ingwer.landfill.netty.packet.StringPacket;
import de.bentzin.ingwer.landfill.netty.packet.put.PutAccountPacket;
import de.bentzin.ingwer.landfill.netty.packet.response.PutConfirmResponsePacket;
import io.netty5.buffer.Buffer;
import io.netty5.buffer.BufferUtil;
import io.netty5.channel.ChannelPipeline;
import io.netty5.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty5.handler.codec.LengthFieldPrepender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class NettyUtils {

    private static final @NotNull Logger logger = LogManager.getLogger();


    public static final int MAX_PACKET_SIZE = 16384;
    public static final int lengthFieldLength = 2;
    // public static int MIN_PACKET_SIZE = 1;

    public static LengthFieldPrepender lengthFieldPrepender() {
        return new LengthFieldPrepender(lengthFieldLength);
    }

    public static LengthFieldBasedFrameDecoder lengthFieldBasedFrameDecoder() {
        return new LengthFieldBasedFrameDecoder(MAX_PACKET_SIZE, 0, lengthFieldLength, 0, 2);
    }

    public static void initializePipeline(@NotNull ChannelPipeline pipeline, @NotNull PacketRegistry registry) {
        pipeline.addLast("length-prepender", lengthFieldPrepender());
        pipeline.addLast("encoder", new PacketEncoder(registry));
        pipeline.addLast("length-decoder", lengthFieldBasedFrameDecoder());
        pipeline.addLast("decoder", new PacketDecoder(registry));
        pipeline.addLast("handler", new PacketHandler());
        pipeline.addLast("knock-knock", new KnockKnockHandler() );


        logger.info("pipeline was initialized for: " + pipeline.channel().remoteAddress());
        //pipeline.addLast(sslCtx.newHandler(ch.bufferAllocator()));
    }

    public static @NotNull PacketRegistry newPacketRegistry() {
        PacketRegistry packetRegistry = new PacketRegistry();
        try {
            packetRegistry.registerPacket(0, StringPacket.class);
            packetRegistry.registerPacket(1, KnockKnockPacket.class);
            packetRegistry.registerPacket(2, PutAccountPacket.class);
            packetRegistry.registerPacket(3, PutConfirmResponsePacket.class);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return packetRegistry;
    }

    public static void hexdump(Buffer buffer) {
        StringBuilder builder = new StringBuilder();
        BufferUtil.appendPrettyHexDump(builder,buffer);
        System.out.println(builder);
    }

}
