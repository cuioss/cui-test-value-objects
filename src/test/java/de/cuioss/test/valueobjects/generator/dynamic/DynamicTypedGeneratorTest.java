/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.generator.dynamic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.AbstractList;
import java.util.Formattable;
import java.util.SortedSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;

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
        final var generator = new DynamicTypedGenerator<>(Integer.class);
        assertEquals(Integer.class, generator.getType());
        assertNotNull(generator.next());
        assertTrue(Integer.class.isAssignableFrom(generator.next().getClass()));
    }

    @Test
    void shouldHandleInterfaces() {
        final var generator = new DynamicTypedGenerator<>(Formattable.class);
        assertEquals(Formattable.class, generator.getType());
        assertNotNull(generator.next());
        assertTrue(Formattable.class.isAssignableFrom(generator.next().getClass()));
    }

    @Test
    void shouldHandleAbstractClasses() {
        final var generator = new DynamicTypedGenerator<>(AbstractList.class);
        assertEquals(AbstractList.class, generator.getType());
        assertNotNull(generator.next());
        assertTrue(AbstractList.class.isAssignableFrom(generator.next().getClass()));
    }

    @Test
    void shouldHandleDifficultTypes() {
        final var generator = new DynamicTypedGenerator<>(PropertyMetadataImpl.class);
        assertEquals(PropertyMetadataImpl.class, generator.getType());
        assertNotNull(generator.next());
        assertTrue(PropertyMetadataImpl.class.isAssignableFrom(generator.next().getClass()));
    }

    @Test
    void shouldHandleSortedSets() {
        final var generator = new DynamicTypedGenerator<>(SortedSet.class);
        assertEquals(SortedSet.class, generator.getType());
        assertNotNull(generator.next());
        assertTrue(SortedSet.class.isAssignableFrom(generator.next().getClass()));
    }
}
