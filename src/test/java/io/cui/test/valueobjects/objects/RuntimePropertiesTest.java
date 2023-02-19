package io.cui.test.valueobjects.objects;

import static io.cui.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator.COMPLETE_VALID_ATTRIBUTES;
import static io.cui.tools.collect.CollectionLiterals.immutableList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.ValueObjectTest;
import io.cui.test.valueobjects.api.contracts.VerifyConstructor;
import io.cui.test.valueobjects.api.object.ObjectTestContracts;
import io.cui.test.valueobjects.api.object.VetoObjectTestContract;
import io.cui.test.valueobjects.api.property.PropertyConfig;
import io.cui.test.valueobjects.api.property.PropertyReflectionConfig;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.util.CollectionType;
import io.cui.test.valueobjects.testbeans.testgenerator.PropertyMetadataGenerator;

@PropertyReflectionConfig(skip = true)
@PropertyConfig(name = "allProperties", defaultValue = true, propertyClass = PropertyMetadata.class,
        collectionType = CollectionType.LIST, generator = PropertyMetadataGenerator.class)
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
