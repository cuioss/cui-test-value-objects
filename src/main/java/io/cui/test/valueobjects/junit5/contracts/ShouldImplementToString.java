package io.cui.test.valueobjects.junit5.contracts;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.contract.ReflectionUtil;
import io.cui.test.valueobjects.objects.TestObjectProvider;

/**
 * Simple check whether the {@link TestObjectProvider#getUnderTest()} implements
 * {@link Object#equals(Object)} and {@link Object#hashCode()}
 *
 * @author Oliver Wolff
 * @param <T> identifying the type to be tested is usually but not necessarily at least
 *            Serializable.
 *
 */
public interface ShouldImplementToString<T> extends TestObjectProvider<T> {

    /**
     * Simple check whether the {@link TestObjectProvider#getUnderTest()} implements
     * {@link Object#equals(Object)} and {@link Object#hashCode()}
     */
    @Test
    default void shouldImplementToString() {
        var underTest = getUnderTest();
        ReflectionUtil.assertToStringMethodIsOverriden(underTest.getClass());
        assertNotNull(underTest.toString(), "toString must not return 'null'");
    }
}
