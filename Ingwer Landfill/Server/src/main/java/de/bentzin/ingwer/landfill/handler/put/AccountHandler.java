package de.bentzin.ingwer.landfill.handler.put;

import de.bentzin.ingwer.landfill.LandfillServer;
import de.bentzin.ingwer.landfill.db.user.Account;
import de.bentzin.ingwer.landfill.netty.Packet;
import de.bentzin.ingwer.landfill.netty.packet.put.PutAccountPacket;
import de.bentzin.ingwer.landfill.tasks.QueuedTask;
import de.bentzin.ingwer.landfill.tasks.Task;
import de.bentzin.ingwer.landfill.tasks.TaskExecutionException;
import io.netty5.channel.Channel;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.util.Pack;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class AccountHandler extends SimpleChannelInboundHandler<Packet> {

    private static final @NotNull Logger logger = LogManager.getLogger();

    @Override
    protected void messageReceived(@NotNull ChannelHandlerContext ctx, @NotNull Packet msgTest) throws Exception {
        if(msgTest instanceof PutAccountPacket msg) {
            logger.info("handeling: " + msg);
            class PutAccountTask implements Task {
                @Override
                public void execute(@NotNull Session session, Channel channel) throws TaskExecutionException {
                    Optional<Account> account = session.byId(Account.class).loadOptional(msg.getId());
                    Transaction transaction = session.beginTransaction();
                    account.ifPresentOrElse(account1 -> {
                        account1.logDynamicBoxedString();
                        if (msg.getAboutMe() != null) account1.setAboutMe(msg.getAboutMe());
                        if (msg.getPronouns() != null) account1.setPronouns(msg.getPronouns());
                        if (msg.getLegacyName() != null) account1.setLegacyName(msg.getLegacyName());

                        {
                            account1.currentUsername(msg.getUserName());
                        }
                    }, () -> {
                        Account newAccount = new Account(msg.getId(),
                                new ArrayList<>(),
                                msg.getDisplayName(),
                                Date.from(Instant.ofEpochMilli(msg.getJoinDate())),
                                msg.getLegacyName(),
                                msg.getPronouns(),
                                msg.getAboutMe());
                        session.persist(newAccount);
                    });
                    transaction.commit();
                    session.close();
                }
            }

            Objects.requireNonNull(LandfillServer
                            .LANDFILL_SERVER
                            .getTaskmanager())
                    .enqueue(new QueuedTask(new PutAccountTask(), ctx::channel, () -> Objects.requireNonNull(LandfillServer.LANDFILL_SERVER.getDatabaseConnector()).getLandfillDB().openSession()));
        }
    }
}
