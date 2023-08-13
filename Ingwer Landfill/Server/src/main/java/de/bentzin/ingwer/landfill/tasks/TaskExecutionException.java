package de.bentzin.ingwer.landfill.tasks;

import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public sealed class TaskExecutionException extends Exception permits UnexpectedTaskExecutionException{

    public TaskExecutionException() {
    }

    public TaskExecutionException(@NotNull String message) {
        super(message);
    }

    public TaskExecutionException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }

    public TaskExecutionException(@NotNull Throwable cause) {
        super(cause);
    }

    @Override
    public @NotNull String getMessage() {
        return "Task execution failed: " + super.getMessage();
    }
}
