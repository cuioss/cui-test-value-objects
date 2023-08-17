package de.cuioss.test.valueobjects.api.object;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * In contrast to {@link VetoObjectTestContract} that is designed for cases
 * where the tests run all {@link ObjectTestConfig}, e.g. ValueObjectTest this
 * annotation is for opt-in cases. As default all {@link ObjectTestContract}s
 * will be run if this annotation is present, but you can Veto the individual
 * contracts
 *
 * @author Oliver Wolff
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Repeatable(VerifyObjectTestContracts.class)
public @interface VerifyObjectTestContract {

    /**
     * @return the concrete contract to be vetoed
     */
    ObjectTestContracts[] veto() default {};

}
