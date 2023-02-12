package io.cui.test.valueobjects.api.contracts;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Wraps a number of {@link VerifyFactoryMethod} elements.
 *
 * @author Oliver Wolff
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface VerifyFactoryMethods {

    /**
     * @return an array of {@link VerifyConstructor}.
     */
    VerifyFactoryMethod[] value();
}
