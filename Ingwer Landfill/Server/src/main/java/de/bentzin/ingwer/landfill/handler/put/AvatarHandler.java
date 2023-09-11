package de.bentzin.ingwer.landfill.handler.put;

import de.bentzin.ingwer.landfill.LandfillServer;
import de.bentzin.ingwer.landfill.db.cdn.ExternalReference;
import de.bentzin.ingwer.landfill.db.cdn.ReferenceType;
import de.bentzin.ingwer.landfill.db.user.Account;
import de.bentzin.ingwer.landfill.db.user.Avatar;
import de.bentzin.ingwer.landfill.netty.packet.put.PutAvatarPacket;
import de.bentzin.ingwer.landfill.netty.packet.put.PutPacket;
import de.bentzin.ingwer.landfill.netty.packet.response.PutConfirmResponsePacket;
import de.bentzin.ingwer.landfill.tasks.QueuedTask;
import de.bentzin.ingwer.landfill.tasks.Task;
import de.bentzin.ingwer.landfill.tasks.TaskEnqueuer;
import de.bentzin.ingwer.landfill.tasks.TaskExecutionException;
import io.netty5.channel.Channel;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.channel.SimpleChannelInboundHandler;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Ture Bentzin
 * @since 2023-08-15
 */
public class AvatarHandler extends SimpleChannelInboundHandler<PutAvatarPacket> {
    @Override
    protected void messageReceived(@NotNull ChannelHandlerContext ctx, @NotNull PutAvatarPacket msg) throws Exception {

        class PutAvatarTask implements Task {

            @Override
            public void execute(@NotNull Session session, @NotNull Channel channel) throws TaskExecutionException {
                final Transaction transaction = session.beginTransaction();
                //check for the account
                Account account = session.byId(Account.class).loadOptional(msg.getAccount())
                                .orElseThrow(() -> new TaskExecutionException("account with id: " +  msg.getAccount() + " is unknown!"));

                //check for avatars URL
                ExternalReference externalReference = session.byId(ExternalReference.class).loadOptional(msg.getReference())
                                .orElseGet(() -> {
                                    //new avatar - should be the usual case
                                 ExternalReference newAvatar = new ExternalReference(msg.getReference(), ReferenceType.AVATAR);
                                 session.persist(newAvatar);
                                 return newAvatar;
                                });

                Avatar avatar = session.get(Avatar.class, new Avatar.AvatarPK(account, externalReference));
                if(avatar == null) {
                    //new one
                    session.persist(new Avatar(account, externalReference));
                }else {
                    //already present - no further action
                    transaction.rollback(); //TODO TEST
                    return;
                }
                transaction.commit(); //add the avatar
                session.close();

                PutTaskUtils.putConfirm(channel, msg);
                channel.flush();

            }
        }

        TaskEnqueuer.enqueueTask(new PutAvatarTask(), ctx);
    }
}
