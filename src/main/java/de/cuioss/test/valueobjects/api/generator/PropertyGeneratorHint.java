package de.cuioss.test.valueobjects.api.generator;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.io.Serializable;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import de.cuioss.test.generator.TypedGenerator;

/**
 * For some cases it is simpler to give a hint for a certain type instead of declaring
 * {@link PropertyGenerator}.
 * e.g. Your class to be tested needs instances of certain Interface that need to be
 * {@link Serializable} but this is not declared at interface level. Therefore this hint may suffice
 * in case you provide a concrete implementation that can be instantiated. The test-framework will
 * than always use a {@link TypedGenerator} for all the tests.
 *
 * @author Oliver Wolff
 */
@Retention(RUNTIME)
@Target(TYPE)
@Repeatable(PropertyGeneratorHints.class)
public @interface PropertyGeneratorHint {

    /**
     * @return The type of object that are declared at field and or method / constructor level
     */
    Class<?> declaredType();

    /**
     * @return the actual implementation type to be used for the {@link #declaredType()}. It should
     *         provide a publicly accessible constructor
     */
    Class<?> implementationType();

}
