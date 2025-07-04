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

import lombok.experimental.UtilityClass;

import java.util.Collection;

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
     * @param expected
     * @param actual
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
            if (!(expected instanceof Iterable) || !(actual instanceof Iterable)) {
                fail(NO_COLLECTION, propertyName, expected, actual);
            }
            handleAssert(propertyName, expected, actual);
        }
    }

    private static void handleAssert(final String propertyName, final Object expected, final Object actual) {
        final Collection<?> expectedIterable = (Collection<?>) expected;
        final Collection<?> actualIterable = (Collection<?>) actual;

        if (expectedIterable.size() != actualIterable.size()) {
            fail(DIFFERENT_SIZES, propertyName, expected, actual);
        }
        if (expectedIterable.isEmpty()) {
            return;
        }
        for (final Object object : expectedIterable) {
            if (!actualIterable.contains(object)) {
                fail(NOT_EQUAL, propertyName, expected, actual);
            }
        }
        for (final Object object : actualIterable) {
            if (!expectedIterable.contains(object)) {
                fail(NOT_EQUAL, propertyName, expected, actual);
            }
        }
    }

    private static void fail(final String template, final String propertyName, final Object expected,
        final Object actual) {
        final var expectedString = String.valueOf(expected);
        final var actualString = String.valueOf(actual);
        throw new AssertionError(template.formatted(propertyName, expectedString, actualString));

    }
}
