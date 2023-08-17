package de.bentzin.ingwer.landfill.tasks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.Closeable;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public final class TaskManager implements Closeable {

    public static final int STANDBY_DELAY = 2000;
    public static final int LOOP_DELAY = 100; //Debug delay - set 0 for pr
    private static final int EXECUTOR_AMOUNT = 1;

    private static final @NotNull Logger logger = LogManager.getLogger();

    private final @NotNull Queue<QueuedTask> taskQueue;
    private final @NotNull Set<TaskExecutor> executors = new HashSet<>();
    private final @NotNull ThreadGroup executorGroup;

    public TaskManager(@NotNull Queue<QueuedTask> taskQueue) {
        this.taskQueue = taskQueue;

        //create Executors
        executorGroup = new ThreadGroup("task_executors");

        for (int i = 0; i < EXECUTOR_AMOUNT; i++) {
            TaskExecutor taskExecutor = new TaskExecutor(this);
            Thread thread = new Thread(executorGroup, taskExecutor, "taskExecutor-" + i);
            logger.info("firing thread: " + thread.getName());
            thread.start();
        }
    }


    public TaskManager() {
        this(new LinkedBlockingQueue<>());
    }

    @Contract(pure = true)
    public @NotNull @UnmodifiableView Collection<QueuedTask> viewQueue() {
        return Collections.unmodifiableCollection(taskQueue);
    }

    private @NotNull Queue<QueuedTask> getTaskQueue() {
        return taskQueue;
    }

    public boolean enqueue(@NotNull QueuedTask task) {
        return taskQueue.add(task);
    }

    public @NotNull Optional<QueuedTask> acceptTask() {
        return Optional.ofNullable(getTaskQueue().poll());
    }

    public @NotNull Set<TaskExecutor> getExecutors() {
        return executors;
    }

    public @NotNull ThreadGroup getExecutorGroup() {
        return executorGroup;
    }

    public void shutdownGracefully() throws InterruptedException{
        getExecutors().forEach(TaskExecutor::kill);
        while (!getExecutors().isEmpty()) {
            Thread.sleep(10);
        }
        logger.info("all executors where terminated");
        close();

    }

    @Override
    public void close() {
        int i = getExecutorGroup().activeCount();
        getExecutorGroup().interrupt();
        logger.debug("killed " + i + " threads!");
    }
}
