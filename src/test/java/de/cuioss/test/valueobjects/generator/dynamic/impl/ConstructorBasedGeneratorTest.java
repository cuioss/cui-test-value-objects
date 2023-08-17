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
package de.cuioss.test.valueobjects.generator.dynamic.impl;

import static de.cuioss.test.valueobjects.generator.dynamic.impl.ConstructorBasedGenerator.getGeneratorForType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import java.util.AbstractList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.api.object.VetoObjectTestContract;
import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import de.cuioss.test.valueobjects.testbeans.ComplexBean;
import de.cuioss.test.valueobjects.testbeans.constructor.BeanWithMultipleArgumentConstructor;
import de.cuioss.test.valueobjects.testbeans.constructor.BeanWithMultiplePublicConstructor;
import de.cuioss.test.valueobjects.testbeans.constructor.BeanWithSingleArgumentConstructor;
import de.cuioss.tools.property.PropertyMemberInfo;

class ConstructorBasedGeneratorTest {

    @BeforeEach
    void before() {
        TypedGeneratorRegistry.registerBasicTypes();
    }

    @AfterEach
    void after() {
        TypedGeneratorRegistry.clear();
    }

    @Test
    void shouldHandleDefaultConstructor() {
        assertTrue(getGeneratorForType(ComplexBean.class).isPresent());
        final var generator = getGeneratorForType(ComplexBean.class).get();
        assertEquals(ComplexBean.class, generator.getType());
        final var next = generator.next();
        assertNotNull(next);
        assertEquals(ComplexBean.class, next.getClass());
    }

    @Test
    void shouldHandledOneArgumentConstructor() {
        assertTrue(getGeneratorForType(BeanWithSingleArgumentConstructor.class).isPresent());
        final var generator = getGeneratorForType(BeanWithSingleArgumentConstructor.class).get();
        assertEquals(BeanWithSingleArgumentConstructor.class, generator.getType());
        final var next = generator.next();
        assertNotNull(next);
        assertEquals(BeanWithSingleArgumentConstructor.class, next.getClass());
        assertNotNull(next.getName());
    }

    @Test
    void shouldHandledMultipleConstructor() {
        assertTrue(getGeneratorForType(BeanWithMultiplePublicConstructor.class).isPresent());
        final var generator = getGeneratorForType(BeanWithMultiplePublicConstructor.class).get();
        assertEquals(BeanWithMultiplePublicConstructor.class, generator.getType());
        final var next = generator.next();
        assertNotNull(next);
        assertEquals(BeanWithMultiplePublicConstructor.class, next.getClass());
        assertNull(next.getName());
    }

    @Test
    void shouldHandleComplexPublicConstructor() {
        assertTrue(getGeneratorForType(BeanWithMultipleArgumentConstructor.class).isPresent());
        final var generator = getGeneratorForType(BeanWithMultipleArgumentConstructor.class).get();
        assertEquals(BeanWithMultipleArgumentConstructor.class, generator.getType());
        final var next = generator.next();
        assertNotNull(next);
        assertEquals(BeanWithMultipleArgumentConstructor.class, next.getClass());
        assertNotNull(next.getName());
        assertNotNull(next.getAbstractList());
        assertNotNull(next.getBuilderInstantiator());
        assertNotNull(next.getNameSet());
        assertNotNull(next.getPropertyMemberInfo());
        assertNotNull(next.getFormattable());
    }

    @Test
    void shouldHandleComplexPackagePrivateConstructor() {
        assertTrue(getGeneratorForType(PropertyMetadataImpl.class).isPresent());
        final var generator = getGeneratorForType(PropertyMetadataImpl.class).get();
        assertEquals(PropertyMetadataImpl.class, generator.getType());
        final PropertyMetadata next = generator.next();
        assertNotNull(next);
        assertEquals(PropertyMetadataImpl.class, next.getClass());
        assertNotNull(next.getName());
        assertNotNull(next.getGenerator());
        assertNotNull(next.getPropertyMemberInfo());
        assertNotNull(next.getPropertyClass());
    }

    @Test
    void shouldNotHandleInvalidTypes() {
        assertFalse(getGeneratorForType(null).isPresent());
        assertFalse(getGeneratorForType(PropertyMemberInfo.class).isPresent());
        assertFalse(getGeneratorForType(Serializable.class).isPresent());
        assertFalse(getGeneratorForType(AbstractList.class).isPresent());
        assertFalse(getGeneratorForType(VetoObjectTestContract.class).isPresent());
    }
}
