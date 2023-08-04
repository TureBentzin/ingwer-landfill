package de.bentzin.ingwer.landfill;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.MessageFactory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Ture Bentzin
 * @since 2023-08-04
 */
public interface Displayable {

    default @NotNull String toBoxedString() {
        StringBuilder tableBuilder = new StringBuilder();

        // Add table header
        tableBuilder.append("******************************************\n");
        tableBuilder.append(String.format("*  %-38s  *\n", this.getClass().getName() + "#" + this.getClass().hashCode()));
        tableBuilder.append("******************************************\n");
        tableBuilder.append("*   Name    |   Type   | Modifiers | Value *\n");
        tableBuilder.append("******************************************\n");

        // Add rows for each field
        receiveFields().forEach(field -> {
            String name = field.getName();
            String type = field.getType().getSimpleName();
            String modifiers = Modifier.toString(field.getModifiers());
            String value = null; // You can add logic to get the value if needed
            try {
                value = Optional.ofNullable(field.get(this)).orElse("null").toString();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            // Append row to the table
            tableBuilder.append("* ");
            tableBuilder.append(String.format("%-10s | ", name));
            tableBuilder.append(String.format("%-8s | ", type));
            tableBuilder.append(String.format("%-10s | ", modifiers));
            tableBuilder.append(String.format("%-6s *\n", value));
        });

        // Add table footer
        tableBuilder.append("******************************************");

        return tableBuilder.toString();
    }

    //This will need further attention
    @ApiStatus.Experimental
    default @NotNull String toDynamicBoxedString() {
        StringBuilder tableBuilder = new StringBuilder(System.lineSeparator());

        // Calculate maximum column widths
        AtomicInteger maxNameWidth = new AtomicInteger(1);
        AtomicInteger maxTypeWidth = new AtomicInteger(1);
        AtomicInteger maxModifiersWidth = new AtomicInteger(1);

        receiveFields().forEach(field -> {
            maxNameWidth.set(Math.max(maxNameWidth.get(), field.getName().length()));
            maxTypeWidth.set(Math.max(maxTypeWidth.get(), field.getType().getSimpleName().length()));
            maxModifiersWidth.set(Math.max(maxModifiersWidth.get(), Modifier.toString(field.getModifiers()).length()));
        });


        int totalWidth = maxNameWidth.get() + maxTypeWidth.get() + maxModifiersWidth.get() + 30; // 18 accounts for separators and padding

        // Add table header
        tableBuilder.append("*".repeat(totalWidth)).append("\n");
        tableBuilder.append(String.format("*  %-38s  *\n", this.getClass().getName() + "#" + this.getClass().hashCode()));
        tableBuilder.append("*".repeat(totalWidth)).append("\n");
        tableBuilder.append(String.format("*   %-" + maxNameWidth + "s |   %-" + maxTypeWidth + "s | %-" + maxModifiersWidth + "s | Value *\n", "Name", "Type", "Modifiers"));
        tableBuilder.append("*".repeat(totalWidth)).append("\n");

        // Add rows for each field
        receiveFields().forEach(field -> {
            String name = field.getName();
            String type = field.getType().getSimpleName();
            String modifiers = Modifier.toString(field.getModifiers());
            String value = null; // You can add logic to get the value if needed
            try {
                value = Optional.ofNullable(field.get(this)).orElse("null").toString();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            // Append row to the table
            tableBuilder.append(String.format("*   %-" + maxNameWidth + "s |   %-" + maxTypeWidth + "s | %-" + maxModifiersWidth + "s | %-6s *\n", name, type, modifiers, value));
        });

        // Add table footer
        tableBuilder.append("*".repeat(totalWidth));

        return tableBuilder.toString();
    }

    default @NotNull Stream<Field> receiveFields() {
        return Arrays.stream(this.getClass().getFields());
    }

    default void logEmptyLine(@NotNull Level level) {
        withLogger(logger -> logger.log(level, System.lineSeparator()));
    }

    default void withLogger(@NotNull Consumer<Logger> action) {
        for (Field field : getClass().getDeclaredFields())
            if (Modifier.isStatic(field.getModifiers()) && field.getType().equals(Logger.class))
                try {
                    Logger logger = (Logger) field.get(null);
                    action.accept(logger);
                    return;
                } catch (Throwable ignored) {

                }
        throw new NoSuchElementException("cant find a logger!");
    }

    default @NotNull Optional<Logger> findLogger() {
        AtomicReference<Logger> loggerAtomicReference = new AtomicReference<>(null);
        withLogger(logger -> loggerAtomicReference.set(logger));
        return Optional.ofNullable(loggerAtomicReference.get());
    }

    @ApiStatus.Experimental
    default @NotNull Logger logger(){
        try {
            return findLogger().orElseThrow();
        }catch (Exception e) {
            return LogManager.getLogger(this);
        }
    }

    default void logEmptyLine() {
        logEmptyLine(Level.INFO);
    }

    default void logDynamicBoxedString(@NotNull Logger logger, Level level) {
        logger.log(level,toDynamicBoxedString());
    }

    default void logDynamicBoxedString(Logger logger) {
        logDynamicBoxedString(logger, Level.INFO);
    }

    default void logDynamicBoxedString() {
        logDynamicBoxedString(logger()); //May need new logic for performance
    }
}
