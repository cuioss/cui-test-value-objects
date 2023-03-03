package de.cuioss.test.valueobjects.api.object;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A contract Veto is used for suppressing certain test-contracts. The default implementations run
 * usually all available contracts, e.g. it will run
 * {@link ObjectTestContracts#EQUALS_AND_HASHCODE}, {@link ObjectTestContracts#SERIALIZABLE} and
 * {@link ObjectTestContracts#TO_STRING}. If one of the contracts is not suitable for your concrete
 * test you can suppress this contract to be run by using this annotation.
 *
 * @author Oliver Wolff
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Repeatable(VetoObjectTestContracts.class)
public @interface VetoObjectTestContract {

    /**
     * @return the concrete contract to be suppressed / ignored
     */
    ObjectTestContracts[] value();
}
