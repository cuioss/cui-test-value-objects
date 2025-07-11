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
package de.cuioss.test.valueobjects.generator.dynamic.impl;

import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static de.cuioss.test.valueobjects.generator.dynamic.impl.ArraysGenerator.getGeneratorForType;
import static org.junit.jupiter.api.Assertions.*;

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
        final Class<?> arrayClass = Integer[].class;
        assertTrue(getGeneratorForType(arrayClass).isPresent());
        assertEquals(arrayClass, getGeneratorForType(arrayClass).get().getType());
        assertNotNull(getGeneratorForType(arrayClass).get().next());
        assertEquals(arrayClass, getGeneratorForType(arrayClass).get().next().getClass());
        final Class<?> stringArray = String[].class;
        assertTrue(getGeneratorForType(stringArray).isPresent());
        assertEquals(stringArray, getGeneratorForType(stringArray).get().getType());
        assertNotNull(getGeneratorForType(stringArray).get().next());
        assertEquals(stringArray, getGeneratorForType(stringArray).get().next().getClass());
        TypedGeneratorRegistry.clear();
    }

    @Test
    void shouldResolvePrimitiveArrayGenerator() {
        Class<? extends byte[]> arrayClass = byte[].class;
        assertTrue(getGeneratorForType(arrayClass).isPresent());
        assertEquals(arrayClass, getGeneratorForType(arrayClass).get().getType());
        assertNotNull(getGeneratorForType(arrayClass).get().next());
        assertEquals(arrayClass, getGeneratorForType(arrayClass).get().next().getClass());
    }
}
