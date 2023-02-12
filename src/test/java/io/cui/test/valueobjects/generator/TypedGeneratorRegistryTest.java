package io.cui.test.valueobjects.generator;

import static io.cui.test.valueobjects.generator.TypedGeneratorRegistry.containsGenerator;
import static io.cui.test.valueobjects.generator.TypedGeneratorRegistry.getGenerator;
import static io.cui.test.valueobjects.generator.TypedGeneratorRegistry.registerBasicTypes;
import static io.cui.test.valueobjects.generator.TypedGeneratorRegistry.registerGenerator;
import static io.cui.test.valueobjects.generator.TypedGeneratorRegistry.removeGenerator;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.cui.test.generator.Generators;

class TypedGeneratorRegistryTest {

    @BeforeEach
    void before() {
        TypedGeneratorRegistry.clear();
    }

    @Test
    void shouldHandleNotExistingGenerator() {
        assertFalse(containsGenerator(String.class));
        assertFalse(getGenerator(String.class).isPresent());
    }

    @Test
    void shouldHandleRegisterAndRemove() {
        registerGenerator(Generators.serializables());
        assertFalse(containsGenerator(String.class));
        assertFalse(getGenerator(String.class).isPresent());
        assertTrue(containsGenerator(Serializable.class));
        assertTrue(getGenerator(Serializable.class).isPresent());
        removeGenerator(Serializable.class);
        assertFalse(containsGenerator(Serializable.class));
        assertFalse(getGenerator(Serializable.class).isPresent());
    }

    @Test
    void shouldRegisterBasicTypes() {
        assertFalse(containsGenerator(String.class));
        registerBasicTypes();
        assertTrue(containsGenerator(String.class));
        assertTrue(containsGenerator(boolean.class));
    }
}
