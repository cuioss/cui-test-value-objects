package io.cui.test.valueobjects.api.object;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Wraps a number of {@link VerifyObjectTestContract} elements.
 *
 * @author Oliver Wolff
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface VerifyObjectTestContracts {

    /**
     * @return an array of {@link VerifyObjectTestContract}.
     */
    VerifyObjectTestContract[] value();
}
