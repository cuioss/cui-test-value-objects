package de.cuioss.test.valueobjects.util;

import static de.cuioss.test.valueobjects.util.GeneratorAnnotationHelper.extractConfiguredGeneratorHints;
import static de.cuioss.test.valueobjects.util.GeneratorAnnotationHelper.extractConfiguredPropertyGenerator;
import static de.cuioss.test.valueobjects.util.GeneratorAnnotationHelper.handleGeneratorHints;
import static de.cuioss.test.valueobjects.util.GeneratorAnnotationHelper.handlePropertyGenerator;
import static de.cuioss.test.valueobjects.util.GeneratorAnnotationHelper.handleUnitClassImplementation;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.testbeans.generator.ClassWithMixedGenerator;
import de.cuioss.test.valueobjects.testbeans.generator.ClassWithOneGenerator;
import de.cuioss.test.valueobjects.testbeans.generator.ClassWithTwoGenerator;
import de.cuioss.test.valueobjects.testbeans.generator.GeneratorHintMultipleAnnotations;
import de.cuioss.test.valueobjects.testbeans.generator.GeneratorHintSingleAnnotation;

class GeneratorAnnotationHelperTest {

    @Test
    void shouldHandleGeneratorHints() {
        assertEquals(0, extractConfiguredGeneratorHints(getClass()).size());
        assertEquals(1, extractConfiguredGeneratorHints(GeneratorHintSingleAnnotation.class).size());
        assertEquals(2, extractConfiguredGeneratorHints(GeneratorHintMultipleAnnotations.class).size());
    }

    @Test
    void shouldRegisterConfiguredGeneratorHints() {
        TypedGeneratorRegistry.registerBasicTypes();
        assertNotEquals(Integer.class, TypedGeneratorRegistry.getGenerator(Serializable.class).get().getType());
        handleGeneratorHints(GeneratorHintMultipleAnnotations.class);
        assertEquals(Serializable.class, TypedGeneratorRegistry.getGenerator(Serializable.class).get().getType());
        assertEquals(Integer.class, TypedGeneratorRegistry.getGenerator(Serializable.class).get().next().getClass());
        TypedGeneratorRegistry.clear();
    }

    @Test
    void shouldExtractPropertyGenerator() {
        assertTrue(extractConfiguredPropertyGenerator(getClass()).isEmpty());
        assertEquals(1, extractConfiguredPropertyGenerator(ClassWithOneGenerator.class).size());
        assertEquals(2, extractConfiguredPropertyGenerator(ClassWithTwoGenerator.class).size());
        assertEquals(2, extractConfiguredPropertyGenerator(ClassWithMixedGenerator.class).size());
    }

    @Test
    void shouldRegisterConfiguredPropertyGenerator() {
        TypedGeneratorRegistry.clear();
        assertFalse(TypedGeneratorRegistry.containsGenerator(LocalDate.class));
        assertFalse(TypedGeneratorRegistry.containsGenerator(Number.class));
        assertFalse(TypedGeneratorRegistry.containsGenerator(Float.class));
        handlePropertyGenerator(ClassWithMixedGenerator.class);
        assertTrue(TypedGeneratorRegistry.containsGenerator(LocalDate.class));
        assertTrue(TypedGeneratorRegistry.containsGenerator(Number.class));
        assertTrue(TypedGeneratorRegistry.containsGenerator(Float.class));
        TypedGeneratorRegistry.clear();
    }

    @Test
    void shouldNotRegisterTestObjectAsGenerator() {
        assertDoesNotThrow(() -> handleUnitClassImplementation(this));
        assertDoesNotThrow(() -> handleUnitClassImplementation(null));
    }

    @Test
    void shouldRegisterTestObjectAsGenerator() {
        TypedGeneratorRegistry.clear();
        assertFalse(TypedGeneratorRegistry.containsGenerator(Throwable.class));
        handleUnitClassImplementation(Generators.throwables());
        assertTrue(TypedGeneratorRegistry.containsGenerator(Throwable.class));
    }
}
