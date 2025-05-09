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

import static de.cuioss.test.valueobjects.generator.dynamic.impl.DynamicProxyGenerator.getGeneratorForType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;


import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.api.object.VetoObjectTestContract;
import de.cuioss.tools.property.PropertyMemberInfo;

class DynamicProxyGeneratorTest {

    @Test
    void shouldHandleAbstractType() {
        assertTrue(getGeneratorForType(AbstractList.class).isPresent());
        final var generator = getGeneratorForType(AbstractList.class).get();
        assertEquals(AbstractList.class, generator.getType());
        final var next = generator.next();
        assertNotNull(next);
        assertTrue(AbstractList.class.isAssignableFrom(next.getClass()));
    }

    @Test
    void shouldHandleAnyType() {
        assertTrue(getGeneratorForType(ArrayList.class).isPresent());
        final var generator = getGeneratorForType(ArrayList.class).get();
        assertEquals(ArrayList.class, generator.getType());
        final var next = generator.next();
        assertNotNull(next);
        assertTrue(ArrayList.class.isAssignableFrom(next.getClass()));
        // here should happen something
        assertNotNull(next.iterator());
    }

    @Test
    void shouldNotHandleInvalidTypes() {
        assertFalse(getGeneratorForType(null).isPresent());
        assertFalse(getGeneratorForType(PropertyMemberInfo.class).isPresent());
        assertFalse(getGeneratorForType(Serializable.class).isPresent());
        assertFalse(getGeneratorForType(VetoObjectTestContract.class).isPresent());
    }
}
