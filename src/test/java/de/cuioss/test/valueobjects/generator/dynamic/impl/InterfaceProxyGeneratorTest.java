package de.cuioss.test.valueobjects.generator.dynamic.impl;

import static de.cuioss.test.valueobjects.generator.dynamic.impl.InterfaceProxyGenerator.getGeneratorForType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.SortedSet;

import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.api.object.VetoObjectTestContract;
import de.cuioss.tools.property.PropertyMemberInfo;

class InterfaceProxyGeneratorTest {

    @Test
    void shouldHandleMarkerInterface() {
        assertTrue(getGeneratorForType(Serializable.class).isPresent());
        final var generator = getGeneratorForType(Serializable.class).get();
        assertEquals(Serializable.class, generator.getType());
        final var next = generator.next();
        assertNotNull(next);
        assertTrue(Serializable.class.isAssignableFrom(next.getClass()));
    }

    @Test
    void shouldHandleComplexInterface() {
        assertTrue(getGeneratorForType(SortedSet.class).isPresent());
        final var generator = getGeneratorForType(SortedSet.class).get();
        assertEquals(SortedSet.class, generator.getType());
        final var next = generator.next();
        assertNotNull(next);
        assertTrue(SortedSet.class.isAssignableFrom(next.getClass()));
        // Actually there should happen nothing -> these are mocks
        assertNull(next.iterator());
    }

    @Test
    void shouldNotHandleInvalidTypes() {
        assertFalse(getGeneratorForType(null).isPresent());
        assertFalse(getGeneratorForType(PropertyMemberInfo.class).isPresent());
        assertFalse(getGeneratorForType(AbstractList.class).isPresent());
        assertFalse(getGeneratorForType(VetoObjectTestContract.class).isPresent());
    }
}
