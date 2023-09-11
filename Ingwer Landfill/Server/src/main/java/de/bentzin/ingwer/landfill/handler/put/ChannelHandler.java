package de.bentzin.ingwer.landfill.handler.put;

import de.bentzin.ingwer.landfill.netty.packet.put.PutChannelPacket;
import de.bentzin.ingwer.landfill.tasks.TaskEnqueuer;
import de.bentzin.ingwer.landfill.tasks.TaskExecutionException;
import de.bentzin.ingwer.landfill.tasks.TransactionalTask;
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

    public static void isValidChannelId(long channelId) throws TaskExecutionException {
        String channelIdString = String.valueOf(channelId);

        // A valid Discord channel ID can be 17 or 18 digits long
        if (channelIdString.length() < 17 || channelIdString.length() > 18) {
            throw new TaskExecutionException("Invalid channel ID length");
        }

        // Check if the channelId is a positive value
        if (channelId <= 0) {
            throw new TaskExecutionException("Invalid channel ID value");
        }
    }

    private static final @NotNull Logger logger = LogManager.getLogger();

    @Override
    protected void messageReceived(@NotNull ChannelHandlerContext ctx, @NotNull PutChannelPacket msg) throws Exception {

        class PutChannelTask extends TransactionalTask {

            @Override
            protected void execute(@NotNull Session session, @NotNull Channel channel, @NotNull Transaction transaction)
                    throws TaskExecutionException {
                //channel update or insert?
                isValidChannelId(msg.getId());
                session.byId(de.bentzin.ingwer.landfill.db.channel.Channel.class).loadOptional(msg.getId())
                        .ifPresentOrElse(channel1 -> {
                            //update

                            if(msg.getChannelType() != null) channel1.setType(msg.getChannelType());
                            if(msg.getName() != null) channel1.setName(msg.getName());
                                },
                                () -> {
                            //insert

                                    final de.bentzin.ingwer.landfill.db.channel.Channel channel2 =
                                            new de.bentzin.ingwer.landfill.db.channel.Channel(
                                                    msg.getId(),
                                                    msg.getChannelType(),
                                                    msg.getName()
                                            );
                                    session.persist(channel2);
                                });
                PutTaskUtils.putConfirm(channel, msg);
            }
        }


        TaskEnqueuer.enqueueTask(new PutChannelTask(), ctx);
    }
}
