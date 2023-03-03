package de.cuioss.test.valueobjects.api;

import java.io.Serializable;

import de.cuioss.test.valueobjects.api.object.ObjectTestContracts;
import de.cuioss.test.valueobjects.api.object.VetoObjectTestContract;

/**
 * Defines the method for testing the correctness of {@link Object} related tests, see
 * {@link #shouldImplementObjectContracts()}
 *
 * @author Oliver Wolff
 */
public interface ObjectContractTestSupport {

    /**
     * <p>
     * Base test for ValueObjects verifying the correctness of {@link Object#equals(Object)}
     * {@link Object#hashCode()}, {@link Object#toString()} and {@link Serializable}
     * implementations.
     * </p>
     * <p>
     * If you want to disable one of these tests you can annotate the test class with
     * {@link VetoObjectTestContract} with the value
     * {@link ObjectTestContracts#EQUALS_AND_HASHCODE},
     * {@link ObjectTestContracts#SERIALIZABLE} or {@link ObjectTestContracts#TO_STRING}
     * </p>
     */
    void shouldImplementObjectContracts();

}
