package de.cuioss.test.valueobjects.util;

import static de.cuioss.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator.COMPLETE_VALID_ATTRIBUTES;
import static de.cuioss.test.valueobjects.util.BuilderPropertyHelper.extractConfiguredPropertyBuilderConfigs;
import static de.cuioss.test.valueobjects.util.BuilderPropertyHelper.handleBuilderPropertyConfigAnnotations;
import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.testbeans.builder.BuilderPropertyConfigMinimal;
import de.cuioss.test.valueobjects.testbeans.builder.BuilderPropertyConfigMultiple;
import de.cuioss.test.valueobjects.testbeans.builder.InheritedBuilderPropertyConfigMultiple;
import de.cuioss.test.valueobjects.testbeans.builder.InheritedBuilderPropertyWithAdditionalConfig;

class BuilderPropertyHelperTest {

    @Test
    void shouldExtractBuilderConfigs() {
        assertTrue(extractConfiguredPropertyBuilderConfigs(getClass()).isEmpty());
        assertEquals(1, extractConfiguredPropertyBuilderConfigs(BuilderPropertyConfigMinimal.class).size());
        assertEquals(2, extractConfiguredPropertyBuilderConfigs(BuilderPropertyConfigMultiple.class).size());
        assertEquals(2, extractConfiguredPropertyBuilderConfigs(InheritedBuilderPropertyConfigMultiple.class).size());
        assertEquals(3,
                extractConfiguredPropertyBuilderConfigs(InheritedBuilderPropertyWithAdditionalConfig.class).size());
    }

    @Test
    void shouldHandleAnnotations() {
        TypedGeneratorRegistry.registerBasicTypes();
        final List<PropertyMetadata> meta = immutableList(COMPLETE_VALID_ATTRIBUTES);
        assertEquals(1, handleBuilderPropertyConfigAnnotations(BuilderPropertyConfigMinimal.class, meta).size());
        assertEquals(2, handleBuilderPropertyConfigAnnotations(BuilderPropertyConfigMultiple.class, meta).size());
        assertEquals(2,
                handleBuilderPropertyConfigAnnotations(InheritedBuilderPropertyConfigMultiple.class, meta).size());
        TypedGeneratorRegistry.clear();
    }

    @Test
    void handleBuilderPropertyConfigAnnotationsShouldHandleMissingAnnotation() {
        assertTrue(handleBuilderPropertyConfigAnnotations(getClass(), COMPLETE_VALID_ATTRIBUTES).isEmpty());
    }
}
