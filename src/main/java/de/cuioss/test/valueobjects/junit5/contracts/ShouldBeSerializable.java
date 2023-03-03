package de.cuioss.test.valueobjects.junit5.contracts;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;

import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.contract.SerializableContractImpl;
import de.cuioss.test.valueobjects.objects.TestObjectProvider;

/**
 * Simple check whether the returned {@link TestObjectProvider#getUnderTest()} implements
 * {@link Serializable} correctly
 *
 * @author Oliver Wolff
 * @param <T> identifying the type to be tested is usually but not necessarily at least
 *            Serializable.
 *
 */
public interface ShouldBeSerializable<T> extends TestObjectProvider<T> {

    /**
     * Simple check whether the returned {@link TestObjectProvider#getUnderTest()} implements
     * {@link Serializable} correctly
     */
    @Test
    default void shouldImplementSerializable() {
        var underTest = getUnderTest();
        assertTrue(
                underTest instanceof Serializable,
                underTest.getClass().getName() + " does not implement java.io.Serializable");
        SerializableContractImpl.serializeAndDeserialize(underTest);
    }
}
