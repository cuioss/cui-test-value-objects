/**
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.util;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.testbeans.generator.*;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.time.LocalDate;

import static de.cuioss.test.valueobjects.util.GeneratorAnnotationHelper.*;
import static org.junit.jupiter.api.Assertions.*;

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
