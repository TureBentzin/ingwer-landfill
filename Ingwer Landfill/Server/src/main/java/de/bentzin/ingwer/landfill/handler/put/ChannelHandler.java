package de.bentzin.ingwer.landfill.handler.put;

import de.bentzin.ingwer.landfill.netty.packet.put.PutChannelPacket;
import de.bentzin.ingwer.landfill.tasks.Task;
import de.bentzin.ingwer.landfill.tasks.TaskExecutionException;
import io.netty5.channel.Channel;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-17
 */
public class ChannelHandler extends SimpleChannelInboundHandler<PutChannelPacket> {

    private static final @NotNull Logger logger = LogManager.getLogger();

    @Override
    protected void messageReceived(@NotNull ChannelHandlerContext ctx, @NotNull PutChannelPacket msg) throws Exception {

        class PutChannelTask implements Task {

            @Override
            public void execute(@NotNull Session session, @NotNull Channel channel) throws TaskExecutionException {
                Transaction transaction = session.beginTransaction();
            }
        }
    }
}
