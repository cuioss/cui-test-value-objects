package io.cui.test.valueobjects.generator.dynamic;

import static io.cui.test.valueobjects.generator.dynamic.GeneratorResolver.resolveCollectionGenerator;
import static io.cui.test.valueobjects.generator.dynamic.GeneratorResolver.resolveGenerator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.cui.test.generator.TypedGenerator;
import io.cui.test.valueobjects.api.object.VetoObjectTestContract;
import io.cui.test.valueobjects.generator.TypedGeneratorRegistry;
import io.cui.test.valueobjects.generator.dynamic.impl.CollectionTypeGenerator;
import io.cui.test.valueobjects.generator.dynamic.impl.ConstructorBasedGenerator;
import io.cui.test.valueobjects.generator.dynamic.impl.DynamicProxyGenerator;
import io.cui.test.valueobjects.generator.dynamic.impl.EmptyMapGenerator;
import io.cui.test.valueobjects.generator.dynamic.impl.InterfaceProxyGenerator;
import io.cui.test.valueobjects.objects.BuilderInstantiator;
import io.cui.test.valueobjects.testbeans.ComplexBean;
import io.cui.tools.property.PropertyMemberInfo;

class GeneratorResolverTest {

    @BeforeEach
    void before() {
        TypedGeneratorRegistry.clear();
    }

    @Test
    void shouldProvideAnyGenerator() {
        TypedGeneratorRegistry.registerBasicTypes();
        assertNotNull(resolveGenerator(String.class));
        // enum types
        assertFalse(TypedGeneratorRegistry.containsGenerator(PropertyMemberInfo.class));
        assertNotNull(resolveGenerator(PropertyMemberInfo.class));
        assertTrue(TypedGeneratorRegistry.containsGenerator(PropertyMemberInfo.class));
        // array types
        final Class<?> stringArray = new String[0].getClass();
        assertFalse(TypedGeneratorRegistry.containsGenerator(stringArray));
        assertNotNull(resolveGenerator(stringArray));
        assertTrue(TypedGeneratorRegistry.containsGenerator(stringArray));
        // constructor based
        assertFalse(TypedGeneratorRegistry.containsGenerator(ComplexBean.class));
        assertNotNull(resolveGenerator(ComplexBean.class));
        assertTrue(TypedGeneratorRegistry.containsGenerator(ComplexBean.class));
        assertEquals(ConstructorBasedGenerator.class,
                TypedGeneratorRegistry.getGenerator(ComplexBean.class).get().getClass());
        // Proxy based for interfaces
        assertFalse(TypedGeneratorRegistry.containsGenerator(BuilderInstantiator.class));
        assertNotNull(resolveGenerator(BuilderInstantiator.class));
        assertTrue(TypedGeneratorRegistry.containsGenerator(BuilderInstantiator.class));
        assertEquals(InterfaceProxyGenerator.class,
                TypedGeneratorRegistry.getGenerator(BuilderInstantiator.class).get().getClass());
        // Proxy based for Abstract-classes
        assertFalse(TypedGeneratorRegistry.containsGenerator(AbstractList.class));
        assertNotNull(resolveGenerator(AbstractList.class));
        assertTrue(TypedGeneratorRegistry.containsGenerator(AbstractList.class));
        assertEquals(DynamicProxyGenerator.class,
                TypedGeneratorRegistry.getGenerator(AbstractList.class).get().getClass());
    }

    @Test
    void shouldResolvePrimitiveArrayGenerator() {
        TypedGeneratorRegistry.registerBasicTypes();
        final Class<? extends byte[]> byteArrayClass = new byte[0].getClass();
        final TypedGenerator<? extends byte[]> resolved = resolveGenerator(byteArrayClass);
        assertNotNull(resolved);
        final byte[] generated = resolved.next();
        assertNotNull(generated);
    }

    @Test
    void collectionStrategyShouldHandleCollectionOnly() {
        assertFalse(resolveCollectionGenerator(null).isPresent());
        assertFalse(resolveCollectionGenerator(PropertyMemberInfo.class).isPresent());
        assertFalse(resolveCollectionGenerator(Serializable.class).isPresent());
        assertFalse(resolveCollectionGenerator(HashMap.class).isPresent());
        assertTrue(resolveCollectionGenerator(Set.class).isPresent());
        assertEquals(CollectionTypeGenerator.class, resolveCollectionGenerator(List.class).get().getClass());
        assertTrue(resolveCollectionGenerator(Map.class).isPresent());
        assertEquals(EmptyMapGenerator.class, resolveCollectionGenerator(Map.class).get().getClass());
    }

    @Test
    void shouldFailToHandleAnnoations() {
        assertThrows(IllegalArgumentException.class, () -> resolveGenerator(VetoObjectTestContract.class));
    }
}
