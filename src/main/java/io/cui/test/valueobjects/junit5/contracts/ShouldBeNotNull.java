package io.cui.test.valueobjects.junit5.contracts;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.objects.TestObjectProvider;

/**
 * Simple check whether the returned {@link TestObjectProvider#getUnderTest()} returns a non-null
 * value
 *
 * @author Oliver Wolff
 * @param <T> identifying the type to be tested is usually but not necessarily at least
 *            Serializable.
 *
 */
public interface ShouldBeNotNull<T> extends TestObjectProvider<T> {

    /**
     * Simple check whether the returned {@link TestObjectProvider#getUnderTest()} returns a
     * non-null value
     */
    @Test
    default void shouldBeNotNull() {
        assertNotNull(getUnderTest(), "Given Object must not be null");
    }
}
