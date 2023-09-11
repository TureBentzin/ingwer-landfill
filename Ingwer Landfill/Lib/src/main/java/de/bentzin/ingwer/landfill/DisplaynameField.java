package de.bentzin.ingwer.landfill;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * Indicates that an Object implementing {@link Displayable} should be shown as only this field when displayed by another
 * Displayable object!
 * @author Ture Bentzin
 * @since 2023-08-13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DisplaynameField {
}
