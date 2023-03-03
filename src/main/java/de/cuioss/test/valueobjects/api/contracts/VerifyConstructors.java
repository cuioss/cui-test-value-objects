package de.cuioss.test.valueobjects.api.contracts;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Wraps a number of {@link VerifyConstructor} elements.
 *
 * @author Oliver Wolff
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface VerifyConstructors {

    /**
     * @return an array of {@link VerifyConstructor}.
     */
    VerifyConstructor[] value();
}
