package de.bentzin.ingwer.landfill.handler.put;

import de.bentzin.ingwer.landfill.db.channel.GuildChannel;
import de.bentzin.ingwer.landfill.db.guild.Guild;
import de.bentzin.ingwer.landfill.netty.packet.put.PutGuildChannelPacket;
import de.bentzin.ingwer.landfill.tasks.TaskEnqueuer;
import de.bentzin.ingwer.landfill.tasks.TaskExecutionException;
import de.bentzin.ingwer.landfill.tasks.TransactionalTask;
import io.netty5.channel.Channel;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.channel.SimpleChannelInboundHandler;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-17
 */
public class GuildChannelHandler extends SimpleChannelInboundHandler<PutGuildChannelPacket> {

    @Override
    protected void messageReceived(@NotNull ChannelHandlerContext ctx, @NotNull PutGuildChannelPacket msg) throws Exception {
        class PutGuildChannelTask extends TransactionalTask {

            @Override
            protected void execute(@NotNull Session session, @NotNull Channel channel, @NotNull Transaction transaction)
                    throws TaskExecutionException {
                //channel update or insert?

                final de.bentzin.ingwer.landfill.db.channel.Channel discordChannel = session.byId(de.bentzin.ingwer.landfill.db.channel.Channel.class)
                                .loadOptional(msg.getChannelID()).orElseThrow(() -> new TaskExecutionException("channel not found!"));

                final Guild guild = session.byId(Guild.class).loadOptional(msg.getGuildID())
                                .orElseThrow(() -> new TaskExecutionException("guild not found!"));

                session.byId(GuildChannel.class).loadOptional(msg.getChannelID())
                                .ifPresentOrElse(
                                        guildChannel -> {

                                            //Update
                                            guildChannel.setTopic(msg.getTopic());
                                        },
                                        () -> {

                                            GuildChannel guildChannel = new GuildChannel(discordChannel, guild, msg.getTopic());
                                            session.persist(guildChannel);
                                        }
                                );
                PutTaskUtils.putConfirm(channel, msg);
            }
        }


        TaskEnqueuer.enqueueTask(new PutGuildChannelTask(), ctx);
    }
}
