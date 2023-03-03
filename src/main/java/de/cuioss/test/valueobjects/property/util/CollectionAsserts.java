package de.cuioss.test.valueobjects.property.util;

import java.util.Collection;

import lombok.experimental.UtilityClass;

/**
 * Helper class for asserts on Collection level
 *
 * @author Oliver Wolff
 */
@UtilityClass
public final class CollectionAsserts {

    private static final String NOT_EQUAL =
        "The given object for property %s are not equal: expected=%s , actual=%s";

    private static final String NO_COLLECTION =
        "The given objects for property %s are to be at least a Collection: expected=%s , actual=%s";

    private static final String DIFFERENT_SIZES =
        "The given objects for property %s do not have the same size: expected=%s , actual=%s";

    /**
     * Checks whether two Collection elements are equal ignoring the order.
     *
     * @param propertyName the name of the property, used for creating the error-message, must not
     *            be null
     * @param expected
     * @param actual
     */
    public static void assertListsAreEqualIgnoringOrder(final String propertyName,
            final Object expected,
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

    private static void fail(final String template, final String propertyName,
            final Object expected,
            final Object actual) {
        final var expectedString = String.valueOf(expected);
        final var actualString = String.valueOf(actual);
        throw new AssertionError(
                String.format(template, propertyName, expectedString, actualString));

    }
}
