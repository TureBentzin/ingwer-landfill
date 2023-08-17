package de.bentzin.ingwer.landfill.tasks;

import io.netty5.channel.Channel;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;


/**
 * The {@link TransactionalTask} class provides a framework for executing tasks within a transactional context.
 * It extends the {@link Task} interface, enhancing it with transaction handling capabilities. This class is designed
 * to streamline and simplify the execution of tasks that involve database operations using Hibernate.
 * <p>
 * By using the {@link TransactionalTask} class, developers can ensure that tasks are executed within a single
 * transaction, promoting data consistency and reliability. It encapsulates the process of starting a transaction,
 * executing the task-specific logic, and handling transaction commits or rollbacks based on the outcome.
 * <p>
 * Advantages over the pure {@link Task} Interface:
 * <p>
 * 1. Transaction Management: {@link TransactionalTask} automatically manages transactions for task execution.
 *    It initializes a transaction, invokes the task's logic, and handles the transaction's outcome, ensuring
 *    that the changes are either committed or rolled back appropriately.
 * <p>
 * 2. Simplified Exception Handling: With {@link TransactionalTask}, exception handling is streamlined.
 *    If a {@link TaskExecutionException} is thrown during task execution, the transaction is rolled back automatically,
 *    ensuring that the database remains consistent. This eliminates the need for developers to explicitly
 *    manage transactional state and exception handling for each task.
 * <p>
 * 3. Encapsulation of Boilerplate Code: The repetitive boilerplate code associated with transaction management,
 *    such as starting and committing transactions, is abstracted away. Developers can focus solely on
 *    implementing the task-specific logic, resulting in cleaner and more maintainable code.
 * <p>
 * Usage:
 * <p>
 * To create a transactional task, developers should extend the {@link TransactionalTask} class and implement the
 * `execute` method, passing the required {@link Session} and {@link Channel} objects as well as a {@link Transaction} object.
 * The transaction will be started automatically, and developers should focus on the task-specific logic.
 * <p>
 * Example:
 * <p>
 *
 * <blockquote><pre>
 * public class MyTransactionalTask extends TransactionalTask {
 *     &#064;Override
 *     protected void execute(@NotNull Session session, @NotNull Channel channel, @NotNull Transaction transaction)
 *             throws TaskExecutionException {
 *         // Task-specific logic involving database operations using the provided session
 *         // The transaction will be committed upon successful execution, or rolled back on failure.
 *     }
 *  }

 *  </pre></blockquote>
 *
 * @author Ture Bentzin
 * @since 2023-08-17
 * @see Task
 * @see TaskExecutionException
 */
public abstract class TransactionalTask implements Task {

    /**
     * Executes the task within a transactional context. The transaction is committed upon successful execution
     * of the task's logic. In case of a {@link TaskExecutionException}, the transaction is rolled back to maintain
     * data consistency.
     *
     * @param session The Hibernate session for database operations.
     * @param channel The communication channel for task-specific communication.
     * @throws TaskExecutionException If an error occurs during task execution.
     */
    public void execute(final @NotNull Session session, final @NotNull Channel channel) throws TaskExecutionException {
        final Transaction transaction = session.beginTransaction();
        try {
            execute(session, channel, transaction);
            transaction.commit();
        } catch (TaskExecutionException e) {
            transaction.rollback();
            logger().error("Failed to execute Task! Automatic rollback was initiated!");
            channel.flush();
            session.close();
            throw e;
        } finally {
            channel.flush();
            session.close();
        }
    }

    /**
     * Implement this method to define the task-specific logic that operates within a transactional context.
     *
     * @param session The Hibernate session for database operations.
     * @param channel The communication channel for task-specific communication.
     * @param transaction The transaction associated with the task's execution.
     * @throws TaskExecutionException If an error occurs during task execution.
     */
    protected abstract void execute(final @NotNull Session session, final @NotNull Channel channel, final @NotNull Transaction transaction)
            throws TaskExecutionException;
}

