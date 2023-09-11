package de.bentzin.ingwer.landfill.tasks;

import de.bentzin.ingwer.landfill.LandfillServer;
import io.netty5.channel.ChannelHandlerContext;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Ture Bentzin
 * @since 2023-08-16
 */
public class TaskEnqueuer {

    private TaskEnqueuer() {

    }

    public static <TASK extends Task> void enqueueTask(@NotNull TASK task, @NotNull ChannelHandlerContext context) { //That's how ignoring conventions must feel like...
        Objects.requireNonNull(LandfillServer
                        .LANDFILL_SERVER
                        .getTaskmanager())
                .enqueue(new QueuedTask(task, context::channel, () -> Objects.requireNonNull(LandfillServer.LANDFILL_SERVER.getDatabaseConnector()).getDatabase().openSession()));

    }
}
