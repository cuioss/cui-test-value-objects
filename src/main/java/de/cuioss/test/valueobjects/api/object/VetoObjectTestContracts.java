package de.cuioss.test.valueobjects.api.object;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Wraps a number of {@link VetoObjectTestContract} elements.
 *
 * @author Oliver Wolff
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface VetoObjectTestContracts {

    /**
     * @return an array of {@link VetoObjectTestContract}.
     */
    VetoObjectTestContract[] value();
}
