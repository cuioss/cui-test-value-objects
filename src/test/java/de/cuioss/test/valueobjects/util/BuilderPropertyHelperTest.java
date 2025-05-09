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
