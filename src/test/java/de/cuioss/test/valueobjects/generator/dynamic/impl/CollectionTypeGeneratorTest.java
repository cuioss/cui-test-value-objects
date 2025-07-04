/**
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.generator.dynamic.impl;

import de.cuioss.test.valueobjects.property.util.CollectionType;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.SortedSet;

import static org.junit.jupiter.api.Assertions.*;

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
