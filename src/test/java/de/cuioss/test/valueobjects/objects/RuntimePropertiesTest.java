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
package de.cuioss.test.valueobjects.objects;

import static de.cuioss.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator.COMPLETE_VALID_ATTRIBUTES;
import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;


import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.ValueObjectTest;
import de.cuioss.test.valueobjects.api.contracts.VerifyConstructor;
import de.cuioss.test.valueobjects.api.object.ObjectTestContracts;
import de.cuioss.test.valueobjects.api.object.VetoObjectTestContract;
import de.cuioss.test.valueobjects.api.property.PropertyConfig;
import de.cuioss.test.valueobjects.api.property.PropertyReflectionConfig;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.util.CollectionType;
import de.cuioss.test.valueobjects.testbeans.testgenerator.PropertyMetadataGenerator;

@PropertyReflectionConfig(skip = true)
@PropertyConfig(name = "allProperties", defaultValue = true, propertyClass = PropertyMetadata.class, collectionType = CollectionType.LIST, generator = PropertyMetadataGenerator.class)
@VerifyConstructor(of = "allProperties")
@VetoObjectTestContract(ObjectTestContracts.SERIALIZABLE)
class RuntimePropertiesTest extends ValueObjectTest<RuntimeProperties> {

    @Test
    void shouldFilterAllProperties() {
        final var properties = new RuntimeProperties(COMPLETE_VALID_ATTRIBUTES);
        final var extractNames = RuntimeProperties.extractNames(properties.getAllProperties());
        assertEquals(COMPLETE_VALID_ATTRIBUTES.size(), properties.getAllAsPropertySupport(true, extractNames).size());
        assertEquals(0, properties.getAllAsPropertySupport(true, Collections.emptyList()).size());
        final List<String> filtered = immutableList(extractNames.iterator().next());
        assertEquals(1, properties.getAllAsPropertySupport(true, filtered).size());
        assertEquals(filtered.iterator().next(),
                properties.getAllAsPropertySupport(true, filtered).iterator().next().getName());
    }

    @Test
    void shouldFilterRequiredProperties() {
        final var properties = new RuntimeProperties(COMPLETE_VALID_ATTRIBUTES);
        final var extractNames = RuntimeProperties.extractNames(properties.getRequiredProperties());
        assertEquals(properties.getRequiredProperties().size(),
                properties.getRequiredAsPropertySupport(true, extractNames).size());
        assertEquals(0, properties.getRequiredAsPropertySupport(true, Collections.emptyList()).size());
        final List<String> filtered = immutableList(extractNames.iterator().next());
        assertEquals(1, properties.getRequiredAsPropertySupport(true, filtered).size());
        assertEquals(filtered.iterator().next(),
                properties.getRequiredAsPropertySupport(true, filtered).iterator().next().getName());
    }

    @Test
    void shouldFilterDefaultProperties() {
        final var properties = new RuntimeProperties(COMPLETE_VALID_ATTRIBUTES);
        final var extractNames = RuntimeProperties.extractNames(properties.getDefaultProperties());
        assertEquals(properties.getDefaultProperties().size(),
                properties.getDefaultAsPropertySupport(true, extractNames).size());
        assertEquals(0, properties.getDefaultAsPropertySupport(true, Collections.emptyList()).size());
        final List<String> filtered = immutableList(extractNames.iterator().next());
        assertEquals(1, properties.getDefaultAsPropertySupport(true, filtered).size());
        assertEquals(filtered.iterator().next(),
                properties.getDefaultAsPropertySupport(true, filtered).iterator().next().getName());
    }

    @Test
    void shouldFilterAdditionalProperties() {
        final var properties = new RuntimeProperties(COMPLETE_VALID_ATTRIBUTES);
        final var extractNames = RuntimeProperties.extractNames(properties.getAdditionalProperties());
        assertEquals(properties.getAdditionalProperties().size(),
                properties.getAdditionalAsPropertySupport(true, extractNames).size());
        assertEquals(0, properties.getAdditionalAsPropertySupport(true, Collections.emptyList()).size());
        final List<String> filtered = immutableList(extractNames.iterator().next());
        assertEquals(1, properties.getAdditionalAsPropertySupport(true, filtered).size());
        assertEquals(filtered.iterator().next(),
                properties.getAdditionalAsPropertySupport(true, filtered).iterator().next().getName());
    }
}
