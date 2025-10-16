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

import de.cuioss.test.valueobjects.api.object.VetoObjectTestContract;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.testbeans.beanproperty.*;
import de.cuioss.test.valueobjects.testbeans.builder.BuilderMinimalTestClassComplexSample;
import de.cuioss.test.valueobjects.testbeans.builder.BuilderMinimalTestClassSimple;
import de.cuioss.test.valueobjects.testbeans.constructor.BeanWithAllRequriedConstructorAnnotation;
import de.cuioss.test.valueobjects.testbeans.constructor.BeanWithMultipleConstructorAnnotation;
import de.cuioss.test.valueobjects.testbeans.constructor.BeanWithOneConstructorAnnotation;
import de.cuioss.test.valueobjects.testbeans.constructor.SimpleConstructor;
import de.cuioss.test.valueobjects.testbeans.factory.OneFactoryBean;
import de.cuioss.test.valueobjects.testbeans.factory.TwoFactoryBean;
import de.cuioss.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator;
import de.cuioss.test.valueobjects.testbeans.veto.ClassWithMixedVetoes;
import de.cuioss.test.valueobjects.testbeans.veto.ClassWithOneVeto;
import de.cuioss.test.valueobjects.testbeans.veto.ClassWithTwoVetoes;
import de.cuioss.test.valueobjects.testbeans.veto.InheritedVeto;
import de.cuioss.tools.property.PropertyMemberInfo;
import de.cuioss.tools.property.PropertyReadWrite;
import de.cuioss.tools.reflect.MoreReflection;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.cuioss.test.valueobjects.util.AnnotationHelper.*;
import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static de.cuioss.tools.collect.CollectionLiterals.mutableList;
import static org.junit.jupiter.api.Assertions.*;

class AnnotationHelperTest {

    private static final List<PropertyMetadata> FULL_PROPERTY_LIST = mutableList(
        PropertyMetadataTestDataGenerator.COMPLETE_VALID_ATTRIBUTES);

    // Tests for
    // de.cuioss.test.valueobjects.util.AnnotationHelper.extractConfiguredConstructorContracts
    @Test
    void extractConfiguredConstructorContractsShouldHandleVariants() {
        assertTrue(extractConfiguredConstructorContracts(SimpleConstructor.class).isEmpty());
        assertEquals(1, extractConfiguredConstructorContracts(BeanWithOneConstructorAnnotation.class).size());
        assertEquals(2, extractConfiguredConstructorContracts(BeanWithMultipleConstructorAnnotation.class).size());
    }

    @Test
    void shouldExtractVerifyFActoryMethodAnnotations() {
        assertTrue(extractConfiguredFactoryContracts(SimpleConstructor.class).isEmpty());
        assertEquals(2, extractConfiguredFactoryContracts(TwoFactoryBean.class).size());
        assertEquals(1, extractConfiguredFactoryContracts(OneFactoryBean.class).size());
    }

    @Test
    void constructorConfigToPropertyMetadataShouldMapCorrectly() {
        final var meta = constructorConfigToPropertyMetadata(
            extractConfiguredConstructorContracts(BeanWithAllRequriedConstructorAnnotation.class).iterator().next(),
            FULL_PROPERTY_LIST);
        assertNotNull(meta);
        assertEquals(2, meta.size());
        meta.forEach(p -> assertTrue(p.isRequired(), p.getName() + " must be required"));
    }

    @Test
    void constructorConfigToPropertyMetadataShouldHAndleAllRequried() {
        final var meta = constructorConfigToPropertyMetadata(
            extractConfiguredConstructorContracts(BeanWithOneConstructorAnnotation.class).iterator().next(),
            FULL_PROPERTY_LIST);
        assertNotNull(meta);
        assertEquals(1, meta.size());
    }

    // Tests for
    // de.cuioss.test.valueobjects.util.AnnotationHelper.handleMetadataForPropertyTest
    @Test
    void handleMetadataForPropertyTestShouldHandleEmptySet() {
        assertTrue(handleMetadataForPropertyTest(BeanPropertyTestClassSimple.class, mutableList()).isEmpty());
    }

    @Test
    void handleMetadataForPropertyTestShouldNotFilter() {
        assertEquals(8, handleMetadataForPropertyTest(BeanPropertyTestClassSimple.class, FULL_PROPERTY_LIST).size());
    }

    @Test
    void handleMetadataForPropertyTestShouldExcludeName() {
        final var filtered = handleMetadataForPropertyTest(BeanPropertyTestClassExcludeName.class, FULL_PROPERTY_LIST);
        assertEquals(7, filtered.size());
        filtered.forEach(p -> assertNotEquals("name", p.getName()));
    }

    @Test
    void handleMetadataForPropertyTestShouldWhitelist() {
        final var filtered = handleMetadataForPropertyTest(BeanPropertyTestClassOf.class, FULL_PROPERTY_LIST);
        assertEquals(2, filtered.size());
        final List<String> whitelist = immutableList("name", "generator");
        filtered.forEach(p -> assertTrue(whitelist.contains(p.getName())));
    }

    @Test
    void handleMetadataForPropertyTestShouldExecuteComplexSample() {
        final var filtered = handleMetadataForPropertyTest(BeanPropertyTestClassComplexSample.class,
            FULL_PROPERTY_LIST);
        final Map<String, PropertyMetadata> map = new HashMap<>();
        filtered.forEach(p -> map.put(p.getName(), p));
        assertFalse(map.containsKey("name"));
        assertTrue(map.get("generator").isDefaultValue());
        assertEquals(PropertyReadWrite.READ_ONLY, map.get("generator").getPropertyReadWrite());
        assertTrue(map.get("collectionType").isRequired());
        assertEquals(PropertyMemberInfo.TRANSIENT, map.get("propertyMemberInfo").getPropertyMemberInfo());
    }

    @Test
    void handleMetadataForPropertyTestShouldExcludeMultiple() {
        final var filtered = handleMetadataForPropertyTest(BeanPropertyTestClassExcludeNameAndDefaultValue.class,
            FULL_PROPERTY_LIST);
        assertEquals(6, filtered.size());
        filtered.forEach(p -> assertNotEquals("name", p.getName()));
        filtered.forEach(p -> assertNotEquals("defaultValue", p.getName()));
    }

    @Test
    void handleMetadataForPropertyTestShouldFailWithoutAnnotation() {
        assertThrows(IllegalArgumentException.class,
            () -> handleMetadataForPropertyTest(ClassWithOneVeto.class, FULL_PROPERTY_LIST));
    }

    @Test
    void handleMetadataForBuilderTestShouldHandleEmptySet() {
        assertTrue(handleMetadataForBuilderTest(BuilderMinimalTestClassSimple.class, mutableList()).isEmpty());
    }

    @Test
    void handleMetadataForBuilderTestShouldNotFilter() {
        assertEquals(8, handleMetadataForBuilderTest(BuilderMinimalTestClassSimple.class, FULL_PROPERTY_LIST).size());
    }

    @Test
    void handleMetadataForBuilderTestShouldExecuteComplexSample() {
        final var filtered = handleMetadataForBuilderTest(BuilderMinimalTestClassComplexSample.class,
            FULL_PROPERTY_LIST);
        final Map<String, PropertyMetadata> map = new HashMap<>();
        filtered.forEach(p -> map.put(p.getName(), p));
        assertFalse(map.containsKey("name"));
        assertTrue(map.get("generator").isDefaultValue());
        assertEquals(PropertyReadWrite.READ_ONLY, map.get("generator").getPropertyReadWrite());
        assertTrue(map.get("collectionType").isRequired());
        assertEquals(PropertyMemberInfo.TRANSIENT, map.get("propertyMemberInfo").getPropertyMemberInfo());
    }

    @Test
    void shouldExtractAllAnnotations() {
        assertTrue(MoreReflection.extractAllAnnotations(null, VetoObjectTestContract.class).isEmpty());
        assertTrue(MoreReflection.extractAllAnnotations(Object.class, VetoObjectTestContract.class).isEmpty());
        assertTrue(MoreReflection.extractAllAnnotations(List.class, VetoObjectTestContract.class).isEmpty());
        assertEquals(1,
            MoreReflection.extractAllAnnotations(ClassWithOneVeto.class, VetoObjectTestContract.class).size());
        assertEquals(2,
            MoreReflection.extractAllAnnotations(ClassWithTwoVetoes.class, VetoObjectTestContract.class).size());
        assertEquals(2,
            MoreReflection.extractAllAnnotations(ClassWithMixedVetoes.class, VetoObjectTestContract.class).size());
        assertEquals(2, MoreReflection.extractAllAnnotations(InheritedVeto.class, VetoObjectTestContract.class).size());
    }

    @Test
    void modifyPropertyMetadatashouldHAndleLists() {
        assertNotNull(AnnotationHelper.modifyPropertyMetadata(new HashMap<>(), Collections.emptyList(),
            Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
            Collections.emptyList()));
    }
}
