package de.bentzin.ingwer.landfill.tasks;

import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public final class UnexpectedTaskExecutionException extends TaskExecutionException{

    public UnexpectedTaskExecutionException(@NotNull RuntimeException unexpectedException) {
        initCause(unexpectedException);
    }

    @Override
    public @NotNull String getMessage() {
        return "An unexpected exception was not handed. Please report this issue.";
    }
}
