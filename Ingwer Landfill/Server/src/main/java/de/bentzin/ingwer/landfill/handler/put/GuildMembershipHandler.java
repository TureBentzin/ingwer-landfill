package de.bentzin.ingwer.landfill.handler.put;

import de.bentzin.ingwer.landfill.db.guild.Guild;
import de.bentzin.ingwer.landfill.db.guild.GuildMembership;
import de.bentzin.ingwer.landfill.db.user.Account;
import de.bentzin.ingwer.landfill.netty.packet.put.PutGuildMembershipPacket;
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
public class GuildMembershipHandler extends SimpleChannelInboundHandler<PutGuildMembershipPacket> {

    @Override
    protected void messageReceived(@NotNull ChannelHandlerContext ctx, @NotNull PutGuildMembershipPacket msg) throws Exception {
        class PutGuildMembershipTask extends TransactionalTask {

            @Override
            protected void execute(@NotNull Session session, @NotNull Channel channel, @NotNull Transaction transaction) throws TaskExecutionException {
                final Account account = session.byId(Account.class).loadOptional(msg.getAccountID()).orElseThrow(() -> new TaskExecutionException("account not found!"));
                final Guild guild = session.byId(Guild.class).loadOptional(msg.getGuildID()).orElseThrow(() -> new TaskExecutionException("guild not found!"));
                session.byId(GuildMembership.class).loadOptional(new GuildMembership.GuildMembershipPK(account, guild)).ifPresentOrElse(
                        guildMembership -> {
                            //already present - nothing to change then
                            return;
                        },
                        () -> {
                            GuildMembership guildMembership = new GuildMembership(account, guild);
                            session.persist(guildMembership);
                        }
                );
                PutTaskUtils.putConfirm(channel, msg);
            }
        }
        TaskEnqueuer.enqueueTask(new PutGuildMembershipTask(), ctx);
    }
}
