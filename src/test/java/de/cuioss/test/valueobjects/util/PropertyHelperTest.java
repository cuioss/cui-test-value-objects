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
package de.cuioss.test.valueobjects.util;

import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import de.cuioss.test.valueobjects.testbeans.property.PropertyConfigMinimal;
import de.cuioss.test.valueobjects.testbeans.property.PropertyConfigMultiple;
import de.cuioss.test.valueobjects.testbeans.property.PropertyConfigPropertyClassAndGenerator;
import de.cuioss.test.valueobjects.testbeans.veto.ClassWithOneVeto;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static de.cuioss.test.valueobjects.util.PropertyHelper.*;
import static de.cuioss.tools.collect.CollectionLiterals.mutableList;
import static org.junit.jupiter.api.Assertions.*;

class PropertyHelperTest {

    private static final String NAME_ATTRIBUTE = "name";

    @Test
    void shouldHandlePrimitives() {
        TypedGeneratorRegistry.registerBasicTypes();
        assertTrue(handlePrimitiveAsDefaults(Collections.emptyList()).isEmpty());
        final var metadata = handlePrimitiveAsDefaults(
            mutableList(ReflectionHelper.scanBeanTypeForProperties(PropertyMetadataImpl.class, null)));
        assertNotNull(metadata);
        assertFalse(metadata.isEmpty());
        final var map = toMapView(metadata);
        assertTrue(map.get("required").isDefaultValue());
        assertTrue(map.get("defaultValue").isDefaultValue());
        assertFalse(map.get(NAME_ATTRIBUTE).isDefaultValue());
        TypedGeneratorRegistry.clear();
    }

    // Tests for
    // de.cuioss.test.valueobjects.util.AnnotationHelper.handlePropertyConfigAnnotations
    @Test
    void handlePropertyConfigAnnotationsShouldHandleMissingAnnotation() {
        assertTrue(handlePropertyConfigAnnotations(ClassWithOneVeto.class).isEmpty());
    }

    @Test
    void handlePropertyConfigAnnotationsShouldHandleAnnotations() {
        TypedGeneratorRegistry.registerBasicTypes();
        assertEquals(1, handlePropertyConfigAnnotations(PropertyConfigMinimal.class).size());
        assertEquals(2, handlePropertyConfigAnnotations(PropertyConfigMultiple.class).size());
        var propertyClassAndGenerator = handlePropertyConfigAnnotations(PropertyConfigPropertyClassAndGenerator.class);
        assertEquals(1, propertyClassAndGenerator.size());
        var config = propertyClassAndGenerator.iterator().next();
        assertEquals(Integer.class, config.getPropertyClass());
        TypedGeneratorRegistry.clear();
    }

    @Test
    void shouldFilterBlackAndWhitelistAsMap() {
        TypedGeneratorRegistry.registerBasicTypes();
        final var metadata = handlePrimitiveAsDefaults(
            mutableList(ReflectionHelper.scanBeanTypeForProperties(PropertyMetadataImpl.class, null)));
        var map = handleWhiteAndBlacklist(new String[0], new String[0], metadata);
        assertEquals(metadata.size(), map.size());
        // White-listing
        map = handleWhiteAndBlacklist(new String[]{NAME_ATTRIBUTE}, new String[0], metadata);
        assertEquals(1, map.size());
        assertTrue(map.containsKey(NAME_ATTRIBUTE));
        // Black-listing
        map = handleWhiteAndBlacklist(new String[0], new String[]{NAME_ATTRIBUTE}, metadata);
        assertEquals(metadata.size() - 1, map.size());
        assertFalse(map.containsKey(NAME_ATTRIBUTE));
        TypedGeneratorRegistry.clear();
    }

    @Test
    void shouldFilterBlackAndWhitelistAsList() {
        TypedGeneratorRegistry.registerBasicTypes();
        final var metadata = handlePrimitiveAsDefaults(
            mutableList(ReflectionHelper.scanBeanTypeForProperties(PropertyMetadataImpl.class, null)));
        var resultList = handleWhiteAndBlacklistAsList(new String[0], new String[0], new ArrayList<>(metadata));
        assertEquals(metadata.size(), resultList.size());
        // White-listing
        resultList = handleWhiteAndBlacklistAsList(new String[]{NAME_ATTRIBUTE}, new String[0],
            new ArrayList<>(metadata));
        assertEquals(1, resultList.size());
        assertTrue(toMapView(resultList).containsKey(NAME_ATTRIBUTE));
        // Black-listing
        resultList = handleWhiteAndBlacklistAsList(new String[0], new String[]{NAME_ATTRIBUTE},
            new ArrayList<>(metadata));
        assertEquals(metadata.size() - 1, resultList.size());
        assertFalse(toMapView(resultList).containsKey(NAME_ATTRIBUTE));
        TypedGeneratorRegistry.clear();
    }
}
