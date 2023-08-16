package de.bentzin.ingwer.landfill.handler.put;

import de.bentzin.ingwer.landfill.netty.packet.put.PutGuildPacket;
import de.bentzin.ingwer.landfill.netty.packet.put.PutPacket;
import de.bentzin.ingwer.landfill.netty.packet.response.PutConfirmResponsePacket;
import de.bentzin.ingwer.landfill.tasks.Task;
import de.bentzin.ingwer.landfill.tasks.TaskEnqueuer;
import de.bentzin.ingwer.landfill.tasks.TaskExecutionException;
import io.netty5.channel.Channel;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.channel.SimpleChannelInboundHandler;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-16
 */
public class GuildHandler extends SimpleChannelInboundHandler<PutGuildPacket> {
    @Override
    protected void messageReceived(@NotNull ChannelHandlerContext ctx, @NotNull PutGuildPacket msg) throws Exception {

        class PutGuildTask implements Task {

            @Override
            public void execute(@NotNull Session session, @NotNull Channel channel) throws TaskExecutionException {
                final Transaction transaction = session.beginTransaction();

                


                transaction.commit(); //add the guild
                session.close();

                channel.write(new PutConfirmResponsePacket(PutPacket.Datatype.GUILD, msg.getChecksum(), 0,true)); //TODO Jobs
                channel.flush();

            }
        }

        TaskEnqueuer.enqueueTask(new PutGuildTask(), ctx);
    }
}
