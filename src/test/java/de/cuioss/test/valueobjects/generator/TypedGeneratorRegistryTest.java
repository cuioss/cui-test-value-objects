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
package de.cuioss.test.valueobjects.generator;

import de.cuioss.test.generator.Generators;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
