package de.bentzin.ingwer.landfill.handler.put;

import de.bentzin.ingwer.landfill.db.guild.Guild;
import de.bentzin.ingwer.landfill.db.user.Account;
import de.bentzin.ingwer.landfill.netty.packet.put.PutGuildPacket;
import de.bentzin.ingwer.landfill.tasks.Task;
import de.bentzin.ingwer.landfill.tasks.TaskEnqueuer;
import de.bentzin.ingwer.landfill.tasks.TaskExecutionException;
import io.netty5.channel.Channel;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.channel.SimpleChannelInboundHandler;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

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

                //check for the account
                final Account account = session.byId(Account.class).loadOptional(msg.getOwnerID())
                        .orElseThrow(() -> new TaskExecutionException("account with id: " + msg.getOwnerID() + " is unknown!"));


                //check for ID
                Optional<Guild> guild = session.byId(Guild.class).loadOptional(msg.getId());

                guild.ifPresentOrElse(guild1 -> {

                            //Update
                            if (msg.getDescription() != null) {
                                guild1.setDescription(msg.getDescription());
                            }
                            if (msg.getOwnerID() != 0) {
                                guild1.setOwner(account);
                            } //this may fire back
                            if (msg.getVerificationRequirement() != null) {
                                guild1.setVerificationRequirement(msg.getVerificationRequirement());
                            }
                            if (msg.getPremiumTier() != null) {
                                guild1.setPremiumTier(msg.getPremiumTier());
                            }
                            if (msg.getGuildNSFWLevel() != null) {
                                guild1.setNsfwLevel(msg.getGuildNSFWLevel());
                            }
                        },
                        () -> {

                        });

                transaction.commit(); //add the guild
                session.close();

                PutTaskUtils.putConfirm(channel, msg);
                channel.flush();

            }
        }

        TaskEnqueuer.enqueueTask(new PutGuildTask(), ctx);
    }
}
