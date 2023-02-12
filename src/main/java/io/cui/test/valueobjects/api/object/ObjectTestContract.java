package io.cui.test.valueobjects.api.object;

import io.cui.test.valueobjects.objects.ParameterizedInstantiator;

/**
 * An {@link ObjectTestContract} provides a method that again runs a number of asserts regarding the
 * canonical {@link Object} method.
 *
 * @author Oliver Wolff
 */
@FunctionalInterface
public interface ObjectTestContract {

    /**
     * Checks the concrete contract.
     *
     * @param instantiator must not be null. The actual type of the {@link Object} is not of
     *            interest, because we check {@link Object} contracts
     * @param objectTestConfig optional configuration configuring the tests
     */
    void assertContract(ParameterizedInstantiator<?> instantiator,
            ObjectTestConfig objectTestConfig);
}
