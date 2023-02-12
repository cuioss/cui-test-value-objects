package io.cui.test.valueobjects.util;

import static io.cui.test.valueobjects.testbeans.property.BeanWithReadWriteProperties.ATTRIBUTE_DEFAULT_VALUE;
import static io.cui.test.valueobjects.testbeans.property.BeanWithReadWriteProperties.ATTRIBUTE_NOT_ACCESSIBLE;
import static io.cui.test.valueobjects.testbeans.property.BeanWithReadWriteProperties.ATTRIBUTE_READ_ONLY;
import static io.cui.test.valueobjects.testbeans.property.BeanWithReadWriteProperties.ATTRIBUTE_READ_WRITE;
import static io.cui.test.valueobjects.testbeans.property.BeanWithReadWriteProperties.ATTRIBUTE_TRANSIENT_VALUE;
import static io.cui.test.valueobjects.testbeans.property.BeanWithReadWriteProperties.ATTRIBUTE_WRITE_ONLY;
import static io.cui.test.valueobjects.util.ReflectionHelper.determineSupertypeFromIterable;
import static io.cui.test.valueobjects.util.ReflectionHelper.handlePostProcess;
import static io.cui.test.valueobjects.util.ReflectionHelper.handlePropertyMetadata;
import static io.cui.test.valueobjects.util.ReflectionHelper.scanBeanTypeForProperties;
import static io.cui.test.valueobjects.util.ReflectionHelper.shouldScanClass;
import static io.cui.tools.collect.CollectionLiterals.immutableList;
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

import io.cui.test.valueobjects.api.property.PropertyReflectionConfig;
import io.cui.test.valueobjects.generator.TypedGeneratorRegistry;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.util.CollectionType;
import io.cui.test.valueobjects.property.util.PropertyAccessStrategy;
import io.cui.test.valueobjects.testbeans.ComplexBean;
import io.cui.test.valueobjects.testbeans.beanproperty.BeanWithStringArray;
import io.cui.test.valueobjects.testbeans.property.BeanWithNestedGenerics;
import io.cui.test.valueobjects.testbeans.property.BeanWithNestedGenericsButFiltered;
import io.cui.test.valueobjects.testbeans.property.BeanWithPrimitiveByteArray;
import io.cui.test.valueobjects.testbeans.property.BeanWithReadWriteProperties;
import io.cui.test.valueobjects.testbeans.property.PropertyReflectionShouldNotSkip;
import io.cui.test.valueobjects.testbeans.property.PropertyReflectionShouldSkip;
import io.cui.test.valueobjects.testbeans.reflection.GenericTypeWithLowerBoundType;
import io.cui.test.valueobjects.testbeans.reflection.ReflectionPostProcessComplex;
import io.cui.test.valueobjects.testbeans.reflection.ReflectionPostProcessMinimal;
import io.cui.test.valueobjects.testbeans.reflection.StringTypedGenericType;
import io.cui.tools.property.PropertyMemberInfo;
import io.cui.tools.property.PropertyReadWrite;
import io.cui.tools.reflect.MoreReflection;

class ReflectionHelperTest {

    @BeforeEach
    public final void before() {
        TypedGeneratorRegistry.registerBasicTypes();
    }

    @AfterEach
    public final void after() {
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
        assertThrows(IllegalStateException.class,
                () -> scanBeanTypeForProperties(BeanWithNestedGenerics.class, null));
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
        assertThrows(NullPointerException.class,
                () -> determineSupertypeFromIterable(null));
    }

    @Test
    @SuppressWarnings("java:S5778") // owolff
    void shouldFailToExtractTypeArgumentForEmptyIterable() {
        assertThrows(IllegalArgumentException.class,
                () -> determineSupertypeFromIterable(Collections.emptyList()));
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
