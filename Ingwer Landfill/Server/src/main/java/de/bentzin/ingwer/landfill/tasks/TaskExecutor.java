package de.bentzin.ingwer.landfill.tasks;

import de.bentzin.ingwer.landfill.Displayable;
import de.bentzin.ingwer.landfill.OneWaySwitch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class TaskExecutor implements Runnable, Displayable {

    private final int STANDBY_DELAY;
    private final int LOOP_DELAY;
    private static final @NotNull Logger logger = LogManager.getLogger();

    protected TaskExecutor(@NotNull TaskManager taskmanager) {
        this.taskmanager = taskmanager;
        this.STANDBY_DELAY = TaskManager.STANDBY_DELAY;
        this.LOOP_DELAY = TaskManager.LOOP_DELAY;
    }

    protected TaskExecutor(int standbyDelay, int loopDelay, @NotNull TaskManager taskmanager) {
        STANDBY_DELAY = standbyDelay;
        LOOP_DELAY = loopDelay;
        this.taskmanager = taskmanager;
    }

    @Override
    public void run() {
        logger.info("started TaskExecutor in thread: " + Thread.currentThread().getName());
        while (!killSwitch.isFlipped()) {
            Optional<QueuedTask> optionalQueuedTask = taskmanager.acceptTask();
            if(optionalQueuedTask.isEmpty()) {
                try {
                    Thread.sleep(TaskManager.STANDBY_DELAY);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }else {
                QueuedTask queuedTask = optionalQueuedTask.orElseThrow(() -> new IllegalStateException("task was awaited but is not present!"));
                logger.debug("executing task: " + queuedTask.task().getClass().getSimpleName() + "#" +  queuedTask.task().hashCode());
                queuedTask.executeTask();
            }
        }
        logger.info("stopped TaskExecutor...");
        taskmanager.getExecutors().remove(this);
        logger.info("removed from executors list!");
    }

    private final @NotNull OneWaySwitch killSwitch = new OneWaySwitch();
    private final @NotNull TaskManager taskmanager;


    public void kill() {
        killSwitch.flip();
    }

    private @NotNull OneWaySwitch getKillSwitch() {
        return killSwitch;
    }
}
