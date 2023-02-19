package io.cui.test.valueobjects.generator.dynamic.impl;

import static io.cui.test.valueobjects.generator.dynamic.impl.DynamicProxyGenerator.getGeneratorForType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.api.object.VetoObjectTestContract;
import io.cui.tools.property.PropertyMemberInfo;

class DynamicProxyGeneratorTest {

    @Test
    void shouldHandleAbstractType() {
        assertTrue(getGeneratorForType(AbstractList.class).isPresent());
        final var generator = getGeneratorForType(AbstractList.class).get();
        assertEquals(AbstractList.class, generator.getType());
        final var next = generator.next();
        assertNotNull(next);
        assertTrue(AbstractList.class.isAssignableFrom(next.getClass()));
    }

    @Test
    void shouldHandleAnyType() {
        assertTrue(getGeneratorForType(ArrayList.class).isPresent());
        final var generator = getGeneratorForType(ArrayList.class).get();
        assertEquals(ArrayList.class, generator.getType());
        final var next = generator.next();
        assertNotNull(next);
        assertTrue(ArrayList.class.isAssignableFrom(next.getClass()));
        // here should happen something
        assertNotNull(next.iterator());
    }

    @Test
    void shouldNotHandleInvalidTypes() {
        assertFalse(getGeneratorForType(null).isPresent());
        assertFalse(getGeneratorForType(PropertyMemberInfo.class).isPresent());
        assertFalse(getGeneratorForType(Serializable.class).isPresent());
        assertFalse(getGeneratorForType(VetoObjectTestContract.class).isPresent());
    }
}
