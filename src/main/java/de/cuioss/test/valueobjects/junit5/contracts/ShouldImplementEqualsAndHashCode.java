package de.cuioss.test.valueobjects.junit5.contracts;

import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.contract.EqualsAndHashcodeContractImpl;
import de.cuioss.test.valueobjects.objects.TestObjectProvider;

/**
 * Simple check whether the returned {@link TestObjectProvider#getUnderTest()}
 * implements {@link Object#equals(Object)} and {@link Object#hashCode()}
 *
 * @author Oliver Wolff
 * @param <T> identifying the type to be tested is usually but not necessarily
 *            at least Serializable.
 *
 */
public interface ShouldImplementEqualsAndHashCode<T> extends TestObjectProvider<T> {

    /**
     * Simple check whether the returned {@link TestObjectProvider#getUnderTest()}
     * implements {@link Object#equals(Object)} and {@link Object#hashCode()}
     */
    @Test
    default void shouldImplementEqualsAndHashcode() {
        var underTest = getUnderTest();
        EqualsAndHashcodeContractImpl.assertBasicContractOnEquals(underTest);
        EqualsAndHashcodeContractImpl.assertBasicContractOnHashCode(underTest);
    }
}
