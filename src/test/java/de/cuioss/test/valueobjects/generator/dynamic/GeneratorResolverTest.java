/**
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
package de.cuioss.test.valueobjects.generator.dynamic;

import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.valueobjects.api.object.VetoObjectTestContract;
import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.generator.dynamic.impl.*;
import de.cuioss.test.valueobjects.objects.BuilderInstantiator;
import de.cuioss.test.valueobjects.testbeans.ComplexBean;
import de.cuioss.tools.property.PropertyMemberInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.*;

import static de.cuioss.test.valueobjects.generator.dynamic.GeneratorResolver.resolveCollectionGenerator;
import static de.cuioss.test.valueobjects.generator.dynamic.GeneratorResolver.resolveGenerator;
import static org.junit.jupiter.api.Assertions.*;

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
        final Class<?> stringArray = String[].class;
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
        final Class<? extends byte[]> byteArrayClass = byte[].class;
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
    void shouldFailToHandleAnnotations() {
        assertThrows(IllegalArgumentException.class, () -> resolveGenerator(VetoObjectTestContract.class));
    }
}
