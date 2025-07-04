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
package de.cuioss.test.valueobjects.property.util;

import static de.cuioss.test.valueobjects.property.util.CollectionType.COLLECTION;
import static de.cuioss.test.valueobjects.property.util.CollectionType.LIST;
import static de.cuioss.test.valueobjects.property.util.CollectionType.SET;
import static de.cuioss.test.valueobjects.property.util.CollectionType.SORTED_SET;
import static de.cuioss.test.valueobjects.property.util.CollectionType.findResponsibleCollectionType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;


import de.cuioss.test.generator.Generators;
import de.cuioss.test.generator.impl.CollectionGenerator;
import org.junit.jupiter.api.Test;

class CollectionTypeTest {

    private final CollectionGenerator<String> generator = Generators.asCollectionGenerator(Generators.letterStrings());

    @Test void shouldFullfillContract() {
        assertContract(SORTED_SET, SortedSet.class);
        assertContract(SET, Set.class);
        assertContract(LIST, List.class);
        assertContract(COLLECTION, Collection.class);
    }

    @SuppressWarnings("rawtypes") private void assertContract(final CollectionType type, final Class<? extends Iterable> interfaceType) {
        assertNotNull(type.nextIterable(generator));
        Class<? extends Iterable> classType = type.nextIterable(generator).getClass();
        assertTrue(interfaceType.isAssignableFrom(classType));
        assertFalse(Map.class.isAssignableFrom(classType));
        assertNotNull(type.wrapToIterable(generator.list()));
        classType = type.wrapToIterable(generator.list()).getClass();
        assertTrue(interfaceType.isAssignableFrom(classType));
        assertFalse(Map.class.isAssignableFrom(classType));
        assertEquals(interfaceType, type.getIterableType());
        assertNotEquals(Map.class, type.getIterableType());
        assertNotNull(type.emptyCollection());
        assertFalse(type.emptyCollection().iterator().hasNext());
        classType = type.emptyCollection().getClass();
        assertTrue(interfaceType.isAssignableFrom(classType));
        assertFalse(Map.class.isAssignableFrom(classType));
    }

    @Test void shouldFindCollectionPerType() {
        assertFalse(findResponsibleCollectionType(null).isPresent());
        assertFalse(findResponsibleCollectionType(Object.class).isPresent());
        assertFalse(findResponsibleCollectionType(ArrayList.class).isPresent());
        assertEquals(SORTED_SET, findResponsibleCollectionType(SortedSet.class).get());
        assertEquals(SET, findResponsibleCollectionType(Set.class).get());
        assertEquals(LIST, findResponsibleCollectionType(List.class).get());
        assertEquals(COLLECTION, findResponsibleCollectionType(Collection.class).get());
    }

    @Test void shouldIgnoreIterable() {
        assertFalse(findResponsibleCollectionType(Iterable.class).isPresent());
    }
}
