package de.cuioss.test.valueobjects.api.generator;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import de.cuioss.test.generator.TypedGenerator;

/**
 * Used for adding an additional {@link TypedGenerator} for the actual
 * test-class. This can be done defining one or more {@link TypedGenerator} as
 * class, see {@link #value()}.
 *
 * @author Oliver Wolff
 */
@Retention(RUNTIME)
@Target(TYPE)
@Repeatable(PropertyGenerators.class)
public @interface PropertyGenerator {

    /**
     * @return one or an array of {@link TypedGenerator}. This is the standard usage
     *         for this annotation.
     */
    @SuppressWarnings("java:S1452")
    Class<? extends TypedGenerator<?>>[] value() default {};

}
