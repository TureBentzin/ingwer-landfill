package de.bentzin.ingwer.landfill.tasks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public final class Taskmanager implements Closeable {

    public static final int STANDBY_DELAY = 2000;
    private static final int EXECUTOR_AMOUNT = 1;

    private static final @NotNull Logger logger = LogManager.getLogger();

    private final @NotNull Queue<QueuedTask> taskQueue;
    private final @NotNull Set<TaskExecutor> executors = new HashSet<>();
    private final @NotNull ThreadGroup executorGroup;

    public Taskmanager(@NotNull Queue<QueuedTask> taskQueue) {
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


    public Taskmanager() {
        this(new LinkedBlockingQueue<>());
    }

    public @NotNull Collection<QueuedTask> viewQueue() {
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
