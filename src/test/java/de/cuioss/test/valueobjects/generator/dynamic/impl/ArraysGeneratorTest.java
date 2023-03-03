package de.cuioss.test.valueobjects.generator.dynamic.impl;

import static de.cuioss.test.valueobjects.generator.dynamic.impl.ArraysGenerator.getGeneratorForType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.property.PropertyMetadata;

class ArraysGeneratorTest {

    @BeforeEach
    void before() {
        TypedGeneratorRegistry.clear();
        TypedGeneratorRegistry.registerBasicTypes();
    }

    @Test
    void shouldIgnoreInvalidTypes() {
        assertFalse(getGeneratorForType(null).isPresent());
        assertFalse(getGeneratorForType(String.class).isPresent());
        assertFalse(getGeneratorForType(PropertyMetadata.class).isPresent());
    }

    @Test
    void shouldHandleConcreteArrays() {
        final Class<?> arrayClass = new Integer[0].getClass();
        assertTrue(getGeneratorForType(arrayClass).isPresent());
        assertEquals(arrayClass, getGeneratorForType(arrayClass).get().getType());
        assertNotNull(getGeneratorForType(arrayClass).get().next());
        assertEquals(arrayClass, getGeneratorForType(arrayClass).get().next().getClass());
        final Class<?> stringArray = new String[0].getClass();
        assertTrue(getGeneratorForType(stringArray).isPresent());
        assertEquals(stringArray, getGeneratorForType(stringArray).get().getType());
        assertNotNull(getGeneratorForType(stringArray).get().next());
        assertEquals(stringArray, getGeneratorForType(stringArray).get().next().getClass());
        TypedGeneratorRegistry.clear();
    }

    @Test
    void shouldResolvePrimitiveArrayGenerator() {
        Class<? extends byte[]> arrayClass = new byte[0].getClass();
        assertTrue(getGeneratorForType(arrayClass).isPresent());
        assertEquals(arrayClass, getGeneratorForType(arrayClass).get().getType());
        assertNotNull(getGeneratorForType(arrayClass).get().next());
        assertEquals(arrayClass, getGeneratorForType(arrayClass).get().next().getClass());
    }
}
