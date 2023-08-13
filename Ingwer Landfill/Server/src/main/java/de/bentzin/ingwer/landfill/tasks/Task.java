package de.bentzin.ingwer.landfill.tasks;

import io.netty5.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public interface Task{

    default int jobID() {
        return 0;
    }

    default @NotNull Logger logger() {
        return LogManager.getLogger("Task-" + Thread.currentThread().getName());
    }

    void execute(@NotNull Session session, @NotNull Channel channel) throws TaskExecutionException;
}
