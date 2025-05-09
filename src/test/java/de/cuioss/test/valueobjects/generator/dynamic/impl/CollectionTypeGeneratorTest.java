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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.SortedSet;


import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.property.util.CollectionType;

class CollectionTypeGeneratorTest {

    @Test
    void shouldFailOnNullType() {
        assertThrows(NullPointerException.class, () -> new CollectionTypeGenerator<>(null, CollectionType.COLLECTION));
    }

    @Test
    void shouldFailOnNullCollectionType() {
        assertThrows(NullPointerException.class, () -> new CollectionTypeGenerator<>(Set.class, null));
    }

    @Test
    void shouldHandleCollection() {
        final var generator = new CollectionTypeGenerator<>(SortedSet.class, CollectionType.SORTED_SET);
        assertNotNull(generator.next());
        assertTrue(SortedSet.class.isAssignableFrom(generator.next().getClass()));
    }
}
