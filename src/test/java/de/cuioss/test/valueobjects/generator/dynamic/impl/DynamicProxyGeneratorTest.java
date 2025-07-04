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
package de.cuioss.test.valueobjects.generator.dynamic.impl;

import de.cuioss.test.valueobjects.api.object.VetoObjectTestContract;
import de.cuioss.tools.property.PropertyMemberInfo;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;

import static de.cuioss.test.valueobjects.generator.dynamic.impl.DynamicProxyGenerator.getGeneratorForType;
import static org.junit.jupiter.api.Assertions.*;

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
