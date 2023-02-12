package io.cui.test.valueobjects.generator.dynamic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.AbstractList;
import java.util.Observer;
import java.util.SortedSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.generator.TypedGeneratorRegistry;
import io.cui.test.valueobjects.property.impl.PropertyMetadataImpl;

class DynamicTypedGeneratorTest {

    @BeforeEach
    void before() {
        TypedGeneratorRegistry.clear();
        TypedGeneratorRegistry.registerBasicTypes();
    }

    @Test
    void shouldFailOnNull() {
        assertThrows(NullPointerException.class, () -> new DynamicTypedGenerator<>(null));
    }

    @Test
    void shouldHandlePlainObjects() {
        final DynamicTypedGenerator<Integer> generator = new DynamicTypedGenerator<>(Integer.class);
        assertEquals(Integer.class, generator.getType());
        assertNotNull(generator.next());
        assertTrue(Integer.class.isAssignableFrom(generator.next().getClass()));
    }

    @Test
    void shouldHandleInterfaces() {
        final DynamicTypedGenerator<Observer> generator = new DynamicTypedGenerator<>(Observer.class);
        assertEquals(Observer.class, generator.getType());
        assertNotNull(generator.next());
        assertTrue(Observer.class.isAssignableFrom(generator.next().getClass()));
    }

    @SuppressWarnings("rawtypes")
    @Test
    void shouldHandleAbstractClasses() {
        final DynamicTypedGenerator<AbstractList> generator = new DynamicTypedGenerator<>(AbstractList.class);
        assertEquals(AbstractList.class, generator.getType());
        assertNotNull(generator.next());
        assertTrue(AbstractList.class.isAssignableFrom(generator.next().getClass()));
    }

    @Test
    void shouldHandleDifficultTypes() {
        final DynamicTypedGenerator<PropertyMetadataImpl> generator =
            new DynamicTypedGenerator<>(PropertyMetadataImpl.class);
        assertEquals(PropertyMetadataImpl.class, generator.getType());
        assertNotNull(generator.next());
        assertTrue(PropertyMetadataImpl.class.isAssignableFrom(generator.next().getClass()));
    }

    @SuppressWarnings("rawtypes")
    @Test
    void shouldHandleSortedSets() {
        final DynamicTypedGenerator<SortedSet> generator = new DynamicTypedGenerator<>(SortedSet.class);
        assertEquals(SortedSet.class, generator.getType());
        assertNotNull(generator.next());
        assertTrue(SortedSet.class.isAssignableFrom(generator.next().getClass()));
    }
}
