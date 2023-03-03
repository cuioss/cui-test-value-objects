package de.cuioss.test.valueobjects.api;

import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;

/**
 * A {@link TestContract} provides a method that again runs a number of asserts.
 *
 * @author Oliver Wolff
 * @param <T> identifying the type of objects to be tested
 */
public interface TestContract<T> {

    /**
     * Checks the concrete contract.
     */
    void assertContract();

    /**
     * @return the underlying {@link ParameterizedInstantiator}
     */
    ParameterizedInstantiator<T> getInstantiator();
}
