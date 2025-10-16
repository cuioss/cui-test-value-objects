/*
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

import de.cuioss.test.generator.Generators;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static de.cuioss.test.valueobjects.property.util.CollectionAsserts.assertListsAreEqualIgnoringOrder;
import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CollectionAssertsTest {

    @Test
    void shouldPassOnBothNullOrEmpty() {
        assertListsAreEqualIgnoringOrder("propertyName", null, null);
        assertListsAreEqualIgnoringOrder("propertyName", new ArrayList<>(), new ArrayList<>());
    }

    @Test
    @SuppressWarnings("java:S5778")
    // owolff: new ArrayList<>() is not a problem
    void shouldFailOnExpectedNull() {
        assertThrows(AssertionError.class,
            () -> assertListsAreEqualIgnoringOrder("propertyName", null, new ArrayList<>()));
    }

    @Test
    @SuppressWarnings("java:S5778")
    // owolff: new ArrayList<>() is not a problem
    void shouldFailOnActualNull() {
        assertThrows(AssertionError.class,
            () -> assertListsAreEqualIgnoringOrder("propertyName", new ArrayList<>(), null));
    }

    @Test
    void shouldFailOnNoExpectedCollection() {
        assertThrows(AssertionError.class, () -> assertListsAreEqualIgnoringOrder("propertyName", 1, null));
    }

    @Test
    void shouldFailOnNoActualCollection() {
        assertThrows(AssertionError.class, () -> assertListsAreEqualIgnoringOrder("propertyName", null, 1));
    }

    @Test
    void shouldFailOnNoCollection() {
        assertThrows(AssertionError.class, () -> assertListsAreEqualIgnoringOrder("propertyName", 2, 1));
    }

    @Test
    @SuppressWarnings("java:S5778")
    // owolff: List instantiation is not a problem
    void shouldFailOnDifferentSizes() {
        assertThrows(AssertionError.class, () -> assertListsAreEqualIgnoringOrder("propertyName",
            immutableList("a", "b", "b"), immutableList("a", "b")));
    }

    @Test
    void shouldPassSameList() {
        final var list = Generators.asCollectionGenerator(Generators.strings()).list();
        assertListsAreEqualIgnoringOrder("propertyName", list, list);
    }

    @Test
    void shouldPassOnListWithDifferetnOrder() {
        final var list = Generators.asCollectionGenerator(Generators.strings()).list();
        final List<String> list2 = new ArrayList<>(list);
        Collections.shuffle(list2);
        assertListsAreEqualIgnoringOrder("propertyName", list, list2);
        assertListsAreEqualIgnoringOrder("propertyName", list2, list);
    }

    @Test
    void shouldFailWithNonEqualContent() {
        final List<String> list1 = immutableList("a", "b", "b");
        final List<String> list2 = immutableList("a", "b", "c");
        assertThrows(AssertionError.class, () -> assertListsAreEqualIgnoringOrder("propertyName", list1, list2));
    }

    @Test
    void shouldFailWithNonEqualContentReverse() {
        final List<String> list1 = immutableList("a", "b", "b");
        final List<String> list2 = immutableList("a", "b", "c");
        assertThrows(AssertionError.class, () -> assertListsAreEqualIgnoringOrder("propertyName", list2, list1));
    }
}
