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

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for asserts on Collection level
 *
 * @author Oliver Wolff
 */
@UtilityClass
public final class CollectionAsserts {

    private static final String NOT_EQUAL = "The given object for property %s are not equal: expected=%s , actual=%s";

    private static final String NO_COLLECTION = "The given objects for property %s are to be at least a Collection: expected=%s , actual=%s";

    private static final String DIFFERENT_SIZES = "The given objects for property %s do not have the same size: expected=%s , actual=%s";

    /**
     * Checks whether two Collection elements are equal ignoring the order.
     *
     * @param propertyName the name of the property, used for creating the
     *                     error-message, must not be null
     * @param expected     the expected collection content, may be {@code null}
     * @param actual       the actual collection content to be compared against
     *                     {@code expected}, may be {@code null}
     */
    public static void assertListsAreEqualIgnoringOrder(final String propertyName, final Object expected,
        final Object actual) {
        // Same instance or both null
        if (expected == actual) {
            return;
        }

        if (expected == null || actual == null) {
            fail(NOT_EQUAL, propertyName, expected, actual);
        } else {
            if (!(expected instanceof Collection) || !(actual instanceof Collection)) {
                fail(NO_COLLECTION, propertyName, expected, actual);
            }
            handleAssert(propertyName, expected, actual);
        }
    }

    private static void handleAssert(final String propertyName, final Object expected, final Object actual) {
        final Collection<?> expectedCollection = (Collection<?>) expected;
        final Collection<?> actualCollection = (Collection<?>) actual;

        if (expectedCollection.size() != actualCollection.size()) {
            fail(DIFFERENT_SIZES, propertyName, expected, actual);
        }
        if (expectedCollection.isEmpty()) {
            return;
        }
        // Compare element frequencies so that multiplicity is respected, e.g.
        // [a, a, b] must not be treated as equal to [a, b, b].
        if (!frequencyMap(expectedCollection).equals(frequencyMap(actualCollection))) {
            fail(NOT_EQUAL, propertyName, expected, actual);
        }
    }

    private static Map<Object, Long> frequencyMap(final Collection<?> collection) {
        final Map<Object, Long> counts = new HashMap<>();
        for (final Object element : collection) {
            counts.merge(element, 1L, Long::sum);
        }
        return counts;
    }

    private static void fail(final String template, final String propertyName, final Object expected,
        final Object actual) {
        final var expectedString = String.valueOf(expected);
        final var actualString = String.valueOf(actual);
        throw new AssertionError(template.formatted(propertyName, expectedString, actualString));

    }
}
