package de.cuioss.test.valueobjects.api.property;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Wraps a number of {@link PropertyBuilderConfig} elements.
 *
 * @author Oliver Wolff
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface PropertyBuilderConfigs {

    /**
     * @return an array of {@link PropertyBuilderConfig}
     */
    PropertyBuilderConfig[] value();
}
