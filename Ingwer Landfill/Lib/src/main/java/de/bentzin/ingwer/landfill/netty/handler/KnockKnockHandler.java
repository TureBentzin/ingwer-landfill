package de.bentzin.ingwer.landfill.netty.handler;

import de.bentzin.ingwer.landfill.netty.packet.KnockKnockPacket;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class KnockKnockHandler extends SimpleChannelInboundHandler<KnockKnockPacket> {

    private static final @NotNull Logger logger = LogManager.getLogger();

    @Override
    public boolean acceptInboundMessage(@NotNull Object msg) throws Exception {
        return super.acceptInboundMessage(msg) && msg instanceof KnockKnockPacket;
    }

    @Override
    protected void messageReceived(@NotNull ChannelHandlerContext ctx, @NotNull KnockKnockPacket msg) throws Exception {
            String format = SimpleDateFormat.getDateTimeInstance().format(Date.from(Instant.ofEpochMilli(((KnockKnockPacket) msg).getTime())));
            logger.info("Client connected to Landfill: \"" + msg.getWorkerID() + "\" @ " + format);

    }
}
