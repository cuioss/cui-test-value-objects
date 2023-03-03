package de.cuioss.test.valueobjects.api.generator;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Wraps a number of {@link PropertyGenerator} elements.
 *
 * @author Oliver Wolff
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface PropertyGeneratorHints {

    /**
     * @return an array of {@link PropertyGenerator}
     */
    PropertyGeneratorHint[] value();
}
