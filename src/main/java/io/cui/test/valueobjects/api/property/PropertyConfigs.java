package io.cui.test.valueobjects.api.property;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Wraps a number of {@link PropertyConfig} elements.
 *
 * @author Oliver Wolff
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface PropertyConfigs {

    /**
     * @return an array of {@link PropertyConfig}
     */
    PropertyConfig[] value();
}
