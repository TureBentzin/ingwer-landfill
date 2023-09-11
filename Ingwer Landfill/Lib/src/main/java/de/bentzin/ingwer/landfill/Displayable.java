package de.bentzin.ingwer.landfill;

import de.bentzin.ingwer.landfill.err.AmbiguousAnnotationException;
import de.bentzin.tools.DoNotOverride;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Ture Bentzin
 * @since 2023-08-04
 */
public interface Displayable {

    static @NotNull Predicate<Field> blacklist(@NotNull Set<Field> blackList) {
        return field -> !blackList.contains(field);
    }

    static @NotNull Predicate<Field> blacklist(Field @NotNull [] blacklist) {
        return blacklist(Arrays.stream(blacklist).collect(Collectors.toUnmodifiableSet()));
    }

    static @NotNull Predicate<Field> whitelist(Field @NotNull [] whitelist) {
        return whitelist(Arrays.stream(whitelist).collect(Collectors.toUnmodifiableSet()));
    }

    static @NotNull Predicate<Field> whitelist(@NotNull Set<Field> whitelist) {
        return whitelist::contains;
    }

    static @NotNull Predicate<Field> only(@NotNull Field field) {
        return field1 -> field1.equals(field1);
    }

    @DoNotOverride
    default @NotNull String toBoxedString() {
        StringBuilder tableBuilder = new StringBuilder();

        // Add table header
        tableBuilder.append("******************************************\n");
        tableBuilder.append(String.format("*  %-38s  *\n", this.getClass().getName() + "#" + this.hashCode()));
        tableBuilder.append("******************************************\n");
        tableBuilder.append("*   Name    |   Type   | Modifiers | Value *\n");
        tableBuilder.append("******************************************\n");

        // Add rows for each field
        fieldsToDisplay().forEach(field -> {
            String name = field.getName();
            String type = field.getType().getSimpleName();

            String modifiers = Modifier.toString(field.getModifiers());
            String value = null; // You can add logic to get the value if needed



            {
                /*{@link DisplaynameField}*/
                if (Displayable.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    try {
                        Displayable target = (Displayable) field.get(this);
                        value = String
                                .valueOf(AmbiguousAnnotationException
                                        .inline(DisplaynameField.class, target
                                                .collectFields()
                                                .filter(f -> f.isAnnotationPresent(DisplaynameField.class)))
                                        .findAny()
                                        .stream()
                                        .map(field1 -> {
                                            try {
                                                return field1.get(target);
                                            } catch (IllegalAccessException e) {
                                                throw new RuntimeException(e);
                                            }
                                        })
                                        .findFirst()
                                        .orElseThrow());

                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchElementException e) {
                        logger().debug("cant find a displaynameField!");
                        try {
                            value = String.valueOf(field.get(this));
                        } catch (IllegalAccessException illegalAccessException) {
                            throw new RuntimeException(illegalAccessException);
                        }

                    }

                }

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
    @DoNotOverride
    default @NotNull String toDynamicBoxedString() {
        StringBuilder tableBuilder = new StringBuilder(System.lineSeparator());

        // Calculate maximum column widths
        AtomicInteger maxNameWidth = new AtomicInteger(1);
        AtomicInteger maxTypeWidth = new AtomicInteger(1);
        AtomicInteger maxModifiersWidth = new AtomicInteger(1);

        fieldsToDisplay().forEach(field -> {
            maxNameWidth.set(Math.max(maxNameWidth.get(), field.getName().length()));
            maxTypeWidth.set(Math.max(maxTypeWidth.get(), field.getType().getSimpleName().length()));
            maxModifiersWidth.set(Math.max(maxModifiersWidth.get(), Modifier.toString(field.getModifiers()).length()));
        });


        int totalWidth = maxNameWidth.get() + maxTypeWidth.get() + maxModifiersWidth.get() + 30; // 18 accounts for separators and padding

        // Add table header
        tableBuilder.append("*".repeat(totalWidth)).append("\n");
        tableBuilder.append(String.format("*  %-38s  *\n", this.getClass().getName() + "#" + this.hashCode()));
        tableBuilder.append("*".repeat(totalWidth)).append("\n");
        tableBuilder.append(String.format("*   %-" + maxNameWidth + "s |   %-" + maxTypeWidth + "s | %-" + maxModifiersWidth + "s | Value *\n", "Name", "Type", "Modifiers"));
        tableBuilder.append("*".repeat(totalWidth)).append("\n");

        // Add rows for each field
        fieldsToDisplay().forEach(field -> {
            field.setAccessible(true);

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

    /**
     * Override this method to modify the collected Fields
     *
     * @return the fields that should be displayed
     */
    default @NotNull Stream<Field> fieldsToDisplay() {
        return collectFields();
    }


    default @NotNull Stream<Field> collectFields() {
        return Arrays.stream(this.getClass().getDeclaredFields());
    }

    @DoNotOverride
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
        withLogger(loggerAtomicReference::set);
        return Optional.ofNullable(loggerAtomicReference.get());
    }

    @ApiStatus.Experimental
    default @NotNull Logger logger() {
        try {
            return findLogger().orElseThrow();
        } catch (Exception e) {
            return LogManager.getLogger(this);
        }
    }

    default void logEmptyLine() {
        logEmptyLine(Level.INFO);
    }

    default void logDynamicBoxedString(@NotNull Logger logger, @NotNull Level level) {
        logger.log(level, toDynamicBoxedString());
    }

    default void logDynamicBoxedString(@NotNull Logger logger) {
        logDynamicBoxedString(logger, Level.INFO);
    }

    default void logDynamicBoxedString() {
        logDynamicBoxedString(logger()); //May need new logic for performance
    }
}
