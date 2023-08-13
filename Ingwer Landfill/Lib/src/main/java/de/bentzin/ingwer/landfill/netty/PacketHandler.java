package de.bentzin.ingwer.landfill.netty;

import io.netty5.channel.ChannelHandlerContext;
import io.netty5.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class PacketHandler extends SimpleChannelInboundHandler<Packet> {

    private static final @NotNull Logger logger = LogManager.getLogger();

    @Override
    protected void messageReceived(@NotNull ChannelHandlerContext ctx, @NotNull Packet msg) throws Exception {

        logger.info("packet inbound: " + ctx.channel().id() + " -> " + msg);

    }

}
