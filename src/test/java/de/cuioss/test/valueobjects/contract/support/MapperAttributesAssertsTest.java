/*
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
package de.cuioss.test.valueobjects.contract.support;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.valueobjects.api.VerifyMapperConfiguration;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MapperAttributesAssertsTest {

    @VerifyMapperConfiguration(equals = "name:firstName")
    private static final class SampleConfiguration {
    }

    private static PropertyMetadata stringProperty(final String name) {
        return PropertyMetadataImpl.builder().name(name).propertyClass(String.class)
            .generator(Generators.nonEmptyStrings()).build();
    }

    @Test
    void shouldLogAndSkipSourceAttributeWithoutConfiguredAssert() {
        final var config = SampleConfiguration.class.getAnnotation(VerifyMapperConfiguration.class);
        final var sourceProperties = new RuntimeProperties(immutableList(stringProperty("name")));
        final var targetProperties = new RuntimeProperties(immutableList(stringProperty("firstName")));

        final var asserts = new MapperAttributesAsserts(config, targetProperties, sourceProperties);

        // 'unrelated' is not covered by any configured assert, exercising the info-log branch
        assertDoesNotThrow(
            () -> asserts.assertMappingForSourceAttributes(List.of("unrelated"), new Object(), new Object()));
    }
}
