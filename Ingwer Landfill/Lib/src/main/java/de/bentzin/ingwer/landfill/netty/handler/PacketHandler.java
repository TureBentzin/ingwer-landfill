package de.bentzin.ingwer.landfill.netty.handler;

import de.bentzin.ingwer.landfill.netty.Packet;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.channel.SimpleChannelInboundHandler;
import io.netty5.util.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class PacketHandler extends SimpleChannelInboundHandler<Packet> {

    private static final @NotNull Logger logger = LogManager.getLogger();

    public PacketHandler() {
        super(false);
    }

    @Override
    protected void messageReceived(@NotNull ChannelHandlerContext ctx, final @NotNull Packet msg) throws Exception {
        logger.info("packet inbound: " + msg.getClass().getSimpleName() + "#" + ctx.channel().id() + " -> " + msg.toDynamicBoxedString());
        ctx.fireChannelRead(msg); //forward to the next handler (remains untouched
    }

}
