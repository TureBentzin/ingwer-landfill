package de.bentzin.ingwer.landfill.tasks;

import de.bentzin.ingwer.landfill.Displayable;
import de.bentzin.ingwer.landfill.netty.packet.response.MalformedDataPacket;
import io.netty5.channel.Channel;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public record QueuedTask(@NotNull Task task, @NotNull Supplier<Channel> channel, @NotNull Supplier<Session> session) implements Displayable {

    private static final @NotNull Logger logger = LogManager.getLogger();

    public void executeTask() {
        logger.debug("executing task: " + task.hashCode() + "@" + task().jobID());
        try {
            task().execute(session.get(), channel.get());
        } catch (TaskExecutionException executionException) {
            logger.warn("failed to execute task...");
            logger.throwing(Level.WARN, executionException);
            Channel channel1 = channel.get();
            channel1.write(new MalformedDataPacket("Failed to execute Task: " + executionException.getMessage()));
            channel1.flush();

        } catch (RuntimeException runtimeException) {
            logger.error("failed to execute task fatally!");
            logger.throwing(runtimeException);
            Channel channel1 = channel.get();
            channel1.write(new MalformedDataPacket(runtimeException.getClass().getName() + " : " + runtimeException.getMessage() + " caused by: " + runtimeException.getCause()));
            channel1.flush();
        }

    }
}
