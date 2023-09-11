package de.bentzin.ingwer.landfill.handler.put;

import de.bentzin.ingwer.landfill.db.user.Account;
import de.bentzin.ingwer.landfill.netty.packet.put.PutAccountPacket;
import de.bentzin.ingwer.landfill.netty.packet.put.PutPacket;
import de.bentzin.ingwer.landfill.netty.packet.response.PutConfirmResponsePacket;
import de.bentzin.ingwer.landfill.tasks.Task;
import de.bentzin.ingwer.landfill.tasks.TaskEnqueuer;
import de.bentzin.ingwer.landfill.tasks.TaskExecutionException;
import io.netty5.channel.Channel;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class AccountHandler extends SimpleChannelInboundHandler<PutAccountPacket> {

    private static final @NotNull Logger logger = LogManager.getLogger();

    @Override
    protected void messageReceived(@NotNull ChannelHandlerContext ctx, @NotNull PutAccountPacket msg) throws Exception {

        class PutAccountTask implements Task {
            @Override
            public void execute(@NotNull Session session, @NotNull Channel channel) throws TaskExecutionException {
                Optional<Account> account = session.byId(Account.class).loadOptional(msg.getId());
                Transaction transaction = session.beginTransaction();
                account.ifPresentOrElse(account1 -> {
                    //account1.logDynamicBoxedString();
                    if (msg.getAboutMe() != null) account1.setAboutMe(msg.getAboutMe());
                    if (msg.getPronouns() != null) account1.setPronouns(msg.getPronouns());
                    if (msg.getLegacyName() != null) account1.setLegacyName(msg.getLegacyName());

                    {
                        account1.currentUsername(msg.getUserName(), session);
                        account1.currentDisplayname(msg.getDisplayName(), session);
                    }
                }, () -> {

                    Account newAccount = new Account(msg.getId(),
                            new ArrayList<>(),
                            new ArrayList<>(),
                            Date.from(Instant.ofEpochMilli(msg.getJoinDate())),
                            msg.getLegacyName(),
                            msg.getPronouns(),
                            msg.getAboutMe(),
                            msg.isBot());
                    session.persist(newAccount);
                    newAccount.currentUsername(msg.getUserName(), session);
                    newAccount.currentDisplayname(msg.getDisplayName(), session);
                });
                transaction.commit();
                session.close();

                //send confirmation
                PutTaskUtils.putConfirm(channel, msg);
                channel.flush();
            }
        }

        TaskEnqueuer.enqueueTask(new PutAccountTask(), ctx);
    }

}
