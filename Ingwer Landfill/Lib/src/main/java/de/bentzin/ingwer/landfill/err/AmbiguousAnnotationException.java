package de.bentzin.ingwer.landfill.err;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class AmbiguousAnnotationException extends RuntimeException {

    private static final @NotNull Logger logger = LogManager.getLogger();

    @SuppressWarnings("DataFlowIssue")
    @ApiStatus.Experimental
    public static @NotNull Stream<Field> inline(@NotNull Class<? extends Annotation> annotationType, @NotNull Stream<Field> fields) {
        if(fields.count() > 1) throw new AmbiguousAnnotationException(annotationType, fields);
        return fields;
    }

    private final @NotNull Class<? extends Annotation> annotationType;
    private final @NotNull Set<Field> annotatedFields;

    public AmbiguousAnnotationException(@NotNull Class<? extends Annotation> annotationType, @NotNull Stream<Field> annotatedFields) {
        this.annotationType = annotationType;

        final long l = annotatedFields.count();
        //noinspection DataFlowIssue
        this.annotatedFields = annotatedFields
                .filter(field -> Arrays
                                .stream(field.getAnnotations())
                                .anyMatch(annotation -> annotation
                                        .annotationType()
                                        .equals(annotationType)))
                .collect(Collectors.toUnmodifiableSet());
        if (annotatedFields.count() != l) {
            logger.warn("annotatedFields contained non annotated fields as well");
        }
        if(this.annotatedFields.isEmpty()) {
            logger.error("annotatedFields contained no annotated fields");
        }
    }

    public @NotNull Class<? extends Annotation> getAnnotationType() {
        return annotationType;
    }

    protected static @NotNull Logger getLogger() {
        return logger;
    }

    @Unmodifiable
    public @NotNull Set<Field> getAnnotatedFields() {
        return annotatedFields;
    }

    @Override
    public @NotNull String getMessage() {
        return fieldsToString() + " are all annotated with \"" + annotationType.getName() + "\"! There should only be one field annotated!";
    }

    protected @NotNull String fieldsToString() {
        return annotatedFields.stream().map(field -> field.getName() + " : " + field.getType()).toList().toString();
    }
}
