package de.bentzin.ingwer.landfill.tasks;

import io.netty5.channel.Channel;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class TaskManagerTest {

    private TaskManager taskmanager = null;

    @Before
    public void initializeTaskManagerTest() {
        taskmanager = new TaskManager();
    }

//Needs some attention before pr

    @Test
    public void addTasks() throws InterruptedException {
        class TestTask implements Task {
            @Override
            public void execute(@NotNull Session session, Channel channel) throws TaskExecutionException {
                logger().info("session: " + session);
                logger().info("channel: " + channel);
                logger().info("waiting 3s");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new TaskExecutionException(e);
                }
            }
        }

        for (int i = 0; i < 15; i++) {
            taskmanager.enqueue(new QueuedTask(new TestTask(),() -> {throw new UnsupportedOperationException();},() -> {throw new UnsupportedOperationException();}));
        }
        while (taskmanager.viewQueue().size() > 0) {
            Thread.sleep(100);
        }
    }


}