/**
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.junit5.extension;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.junit5.EnableGeneratorRegistry;
import de.cuioss.tools.property.PropertyReadWrite;
import org.junit.jupiter.api.Test;

import java.util.List;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnableGeneratorRegistry
class GeneratorControllerWithoutRegistryTest {

    @Test
    void registryShouldProvideBasicTypes() {
        assertTrue(TypedGeneratorRegistry.containsGenerator(String.class));
    }

    @Test
    void registryShouldProvideImplementationType() {
        assertFalse(TypedGeneratorRegistry.containsGenerator(PropertyReadWrite.class));
    }

    public List<TypedGenerator<?>> registerAdditionalGenerators() {
        return immutableList(Generators.enumValues(PropertyReadWrite.class));
    }

}
