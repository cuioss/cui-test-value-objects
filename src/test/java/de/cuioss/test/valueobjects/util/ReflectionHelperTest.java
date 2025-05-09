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

import static de.cuioss.test.valueobjects.testbeans.property.BeanWithReadWriteProperties.ATTRIBUTE_DEFAULT_VALUE;
import static de.cuioss.test.valueobjects.testbeans.property.BeanWithReadWriteProperties.ATTRIBUTE_NOT_ACCESSIBLE;
import static de.cuioss.test.valueobjects.testbeans.property.BeanWithReadWriteProperties.ATTRIBUTE_READ_ONLY;
import static de.cuioss.test.valueobjects.testbeans.property.BeanWithReadWriteProperties.ATTRIBUTE_READ_WRITE;
import static de.cuioss.test.valueobjects.testbeans.property.BeanWithReadWriteProperties.ATTRIBUTE_TRANSIENT_VALUE;
import static de.cuioss.test.valueobjects.testbeans.property.BeanWithReadWriteProperties.ATTRIBUTE_WRITE_ONLY;
import static de.cuioss.test.valueobjects.util.ReflectionHelper.determineSupertypeFromIterable;
import static de.cuioss.test.valueobjects.util.ReflectionHelper.handlePostProcess;
import static de.cuioss.test.valueobjects.util.ReflectionHelper.handlePropertyMetadata;
import static de.cuioss.test.valueobjects.util.ReflectionHelper.scanBeanTypeForProperties;
import static de.cuioss.test.valueobjects.util.ReflectionHelper.shouldScanClass;
import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.api.property.PropertyReflectionConfig;
import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.util.CollectionType;
import de.cuioss.test.valueobjects.property.util.PropertyAccessStrategy;
import de.cuioss.test.valueobjects.testbeans.ComplexBean;
import de.cuioss.test.valueobjects.testbeans.beanproperty.BeanWithStringArray;
import de.cuioss.test.valueobjects.testbeans.property.BeanWithNestedGenerics;
import de.cuioss.test.valueobjects.testbeans.property.BeanWithNestedGenericsButFiltered;
import de.cuioss.test.valueobjects.testbeans.property.BeanWithPrimitiveByteArray;
import de.cuioss.test.valueobjects.testbeans.property.BeanWithReadWriteProperties;
import de.cuioss.test.valueobjects.testbeans.property.PropertyReflectionShouldNotSkip;
import de.cuioss.test.valueobjects.testbeans.property.PropertyReflectionShouldSkip;
import de.cuioss.test.valueobjects.testbeans.reflection.GenericTypeWithLowerBoundType;
import de.cuioss.test.valueobjects.testbeans.reflection.ReflectionPostProcessComplex;
import de.cuioss.test.valueobjects.testbeans.reflection.ReflectionPostProcessMinimal;
import de.cuioss.test.valueobjects.testbeans.reflection.StringTypedGenericType;
import de.cuioss.tools.property.PropertyMemberInfo;
import de.cuioss.tools.property.PropertyReadWrite;
import de.cuioss.tools.reflect.MoreReflection;

class ReflectionHelperTest {

    @BeforeEach
    final void before() {
        TypedGeneratorRegistry.registerBasicTypes();
    }

    @AfterEach
    final void after() {
        TypedGeneratorRegistry.clear();
    }

    @Test
    void shouldScanBeanWithReadWriteProperties() {
        final Map<String, PropertyMetadata> map = new HashMap<>();
        scanBeanTypeForProperties(BeanWithReadWriteProperties.class, null).forEach(p -> map.put(p.getName(), p));
        assertEquals(4, map.size());
        assertFalse(map.containsKey(ATTRIBUTE_NOT_ACCESSIBLE));
        assertFalse(map.containsKey(ATTRIBUTE_WRITE_ONLY));
        assertMetatada(map);
    }

    private void assertMetatada(final Map<String, PropertyMetadata> map) {
        var metadata = map.get(ATTRIBUTE_READ_WRITE);
        assertEquals(ATTRIBUTE_READ_WRITE, metadata.getName());
        assertEquals(Integer.class, metadata.getPropertyClass());
        assertEquals(PropertyReadWrite.READ_WRITE, metadata.getPropertyReadWrite());
        assertEquals(PropertyAccessStrategy.BEAN_PROPERTY, metadata.getPropertyAccessStrategy());
        assertEquals(PropertyMemberInfo.DEFAULT, metadata.getPropertyMemberInfo());
        assertFalse(metadata.isDefaultValue());
        metadata = map.get(ATTRIBUTE_READ_ONLY);
        assertEquals(ATTRIBUTE_READ_ONLY, metadata.getName());
        assertEquals(String.class, metadata.getPropertyClass());
        assertEquals(PropertyReadWrite.READ_ONLY, metadata.getPropertyReadWrite());
        assertEquals(PropertyAccessStrategy.BEAN_PROPERTY, metadata.getPropertyAccessStrategy());
        assertEquals(PropertyMemberInfo.DEFAULT, metadata.getPropertyMemberInfo());
        assertFalse(metadata.isDefaultValue());
        metadata = map.get(ATTRIBUTE_TRANSIENT_VALUE);
        assertEquals(ATTRIBUTE_TRANSIENT_VALUE, metadata.getName());
        assertEquals(String.class, metadata.getPropertyClass());
        assertEquals(PropertyReadWrite.READ_WRITE, metadata.getPropertyReadWrite());
        assertEquals(PropertyAccessStrategy.BEAN_PROPERTY, metadata.getPropertyAccessStrategy());
        assertEquals(PropertyMemberInfo.TRANSIENT, metadata.getPropertyMemberInfo());
        assertFalse(metadata.isDefaultValue());
        metadata = map.get(ATTRIBUTE_DEFAULT_VALUE);
        assertEquals(ATTRIBUTE_DEFAULT_VALUE, metadata.getName());
        assertEquals(String.class, metadata.getPropertyClass());
        assertEquals(PropertyReadWrite.READ_WRITE, metadata.getPropertyReadWrite());
        assertEquals(PropertyAccessStrategy.BEAN_PROPERTY, metadata.getPropertyAccessStrategy());
        assertEquals(PropertyMemberInfo.DEFAULT, metadata.getPropertyMemberInfo());
        assertFalse(metadata.isDefaultValue());
    }

    @Test
    void shouldScanBeanWithCollections() {
        final Map<String, PropertyMetadata> map = new HashMap<>();
        scanBeanTypeForProperties(ComplexBean.class, null).forEach(p -> map.put(p.getName(), p));
        assertEquals(13, map.size(), "Wrong count of class properties detected.");
        var metadata = map.get(ComplexBean.ATTRIBUTE_STRING_COLLECTION);
        assertEquals(CollectionType.COLLECTION, metadata.getCollectionType());
        assertEquals(String.class, metadata.getPropertyClass());
        metadata = map.get(ComplexBean.ATTRIBUTE_STRING_LIST);
        assertEquals(CollectionType.LIST, metadata.getCollectionType());
        assertEquals(String.class, metadata.getPropertyClass());
        metadata = map.get(ComplexBean.ATTRIBUTE_STRING_SET);
        assertEquals(CollectionType.SET, metadata.getCollectionType());
        assertEquals(String.class, metadata.getPropertyClass());
        metadata = map.get(ComplexBean.ATTRIBUTE_STRING_SORTED_SET);
        assertEquals(CollectionType.SORTED_SET, metadata.getCollectionType());
        assertEquals(String.class, metadata.getPropertyClass());
    }

    @Test
    void shouldScanBeanWithArrayTypes() {
        final var types = scanBeanTypeForProperties(BeanWithStringArray.class, null);
        assertFalse(types.isEmpty());
        final var type = types.first();
        assertEquals(String.class, type.getPropertyClass());
        assertEquals(new String[1].getClass(), type.resolveActualClass());
    }

    @Test
    void shouldScanBeanWithPrimitiveArrayTypes() {
        final var types = scanBeanTypeForProperties(BeanWithPrimitiveByteArray.class, null);
        assertFalse(types.isEmpty());
        final var type = types.first();
        assertEquals(byte.class, type.getPropertyClass());
        assertEquals(new byte[0].getClass(), type.resolveActualClass());
        // Primitive Arrays should not be set as default
        assertFalse(type.isDefaultValue());
    }

    @Test
    void handlePostProcessShouldIgnoreOnEmpty() {
        final var scanned = scanBeanTypeForProperties(BeanWithReadWriteProperties.class, null);
        final Map<String, PropertyMetadata> map = new HashMap<>();
        handlePostProcess(ReflectionPostProcessMinimal.class, scanned).forEach(p -> map.put(p.getName(), p));
        assertMetatada(map);
        final Map<String, PropertyMetadata> map2 = new HashMap<>();
        handlePostProcess(this.getClass(), scanned).forEach(p -> map2.put(p.getName(), p));
        assertMetatada(map2);
    }

    @Test
    void handlePostProcessShouldHandleComplex() {
        final var scanned = scanBeanTypeForProperties(BeanWithReadWriteProperties.class, null);
        final Map<String, PropertyMetadata> map = new HashMap<>();
        handlePostProcess(ReflectionPostProcessComplex.class, scanned).forEach(p -> map.put(p.getName(), p));
        assertEquals(3, map.size());
        assertFalse(map.containsKey(ATTRIBUTE_DEFAULT_VALUE));
    }

    @Test
    void shouldHandleSkip() {
        assertTrue(shouldScanClass(getClass()));
        assertFalse(shouldScanClass(PropertyReflectionShouldSkip.class));
        assertTrue(shouldScanClass(PropertyReflectionShouldNotSkip.class));
    }

    @Test
    void shouldHandlMetadataExtraction() {
        assertNotNull(handlePropertyMetadata(getClass(), ComplexBean.class));
        assertTrue(handlePropertyMetadata(PropertyReflectionShouldSkip.class, PropertyReflectionShouldSkip.class)
                .isEmpty());
    }

    @Test
    void shouldFailOnNestedGenerics() {
        assertThrows(IllegalStateException.class, () -> scanBeanTypeForProperties(BeanWithNestedGenerics.class, null));
    }

    @Test
    @SuppressWarnings("java:S2699") // owolff not throwing an exception is the actual test
    void shouldSkipNestedGenerics() {
        scanBeanTypeForProperties(BeanWithNestedGenericsButFiltered.class, MoreReflection
                .extractAnnotation(BeanWithNestedGenericsButFiltered.class, PropertyReflectionConfig.class).get());
    }

    @Test
    void shouldExtractTypeArgument() {
        assertEquals(String.class, determineSupertypeFromIterable(immutableList("String")));
        assertEquals(Integer.class, determineSupertypeFromIterable(immutableList(1, 2, 4)));
    }

    @Test
    void shouldFailToExtractTypeArgumentForNull() {
        assertThrows(NullPointerException.class, () -> determineSupertypeFromIterable(null));
    }

    @Test
    @SuppressWarnings("java:S5778") // owolff
    void shouldFailToExtractTypeArgumentForEmptyIterable() {
        assertThrows(IllegalArgumentException.class, () -> determineSupertypeFromIterable(Collections.emptyList()));
    }

    @Test
    void test() {
        assertFalse("ss".getClass().isPrimitive());
    }

    @Test
    void shouldScanGenericType() {
        var scanned = scanBeanTypeForProperties(GenericTypeWithLowerBoundType.class, null);
        assertEquals(2, scanned.size());
        assertEquals(Serializable.class, scanned.first().getPropertyClass(),
                "Should resolve lower bound parameter at least");
    }

    @Test
    void shouldScanStringTypedGenericType() {
        var scanned = scanBeanTypeForProperties(StringTypedGenericType.class, null);
        assertEquals(2, scanned.size());
        assertEquals(String.class, scanned.first().getPropertyClass(), "Should resolve to the actual type 'String'");
    }
}
