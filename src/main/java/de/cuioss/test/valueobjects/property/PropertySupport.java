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
package de.cuioss.test.valueobjects.property;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.valueobjects.property.util.AssertionStrategy;
import de.cuioss.test.valueobjects.property.util.CollectionAsserts;
import de.cuioss.test.valueobjects.property.util.PropertyAccessStrategy;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Stateful wrapper around instances of {@link PropertyMetadata} that provides
 * methods for reading / asserting properties according to the configured
 * {@link PropertyAccessStrategy}, see {@link #apply(Object)},
 * {@link #assertValueSet(Object)} and {@link #assertDefaultValue(Object)} . In
 * addition it can create / store an instant specific generatedValue, see
 * {@link #generateTestValue()}
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
@ToString
public class PropertySupport {

    /**
     * Used in conjunction with {@link #createCopyWithNonEqualValue()}. Defines the
     * upper bound of retries for generating non-equal values from a given
     * {@link TypedGenerator}
     */
    private static final int ENTROPY_GUARD = 50;

    private static final String TARGET_MUST_NOT_BE_NULL = "target must not be null";

    @NonNull
    @Getter
    private final PropertyMetadata propertyMetadata;

    @Getter
    @Setter
    private Object generatedValue;

    // Shortcuts to PropertyMetadata
    /**
     * @return boolean indicating whether the current property is readable
     */
    public boolean isReadable() {
        return propertyMetadata.getPropertyReadWrite().isReadable();
    }

    /**
     * @return boolean indicating whether the current property is defaultValued
     */
    public boolean isDefaultValue() {
        return propertyMetadata.isDefaultValue();
    }

    /**
     * @return boolean indicating whether the current property is required
     */
    public boolean isRequired() {
        return propertyMetadata.isRequired();
    }

    /**
     * @return the name of the property
     */
    public String getName() {
        return propertyMetadata.getName();
    }

    /**
     * @return boolean indicating whether the current property is a primitive
     */
    public boolean isPrimitive() {
        return propertyMetadata.resolveActualClass().isPrimitive();
    }

    /**
     * Generates a value from the contained generator and sets it to
     * {@link #generatedValue}.
     *
     * @return the generated test value
     */
    public Object generateTestValue() {
        setGeneratedValue(propertyMetadata.next());
        return getGeneratedValue();
    }

    /**
     * Resets the generated value
     */
    public void reset() {
        setGeneratedValue(null);
    }

    /**
     * Writes the property, previously initialized with {@link #generateTestValue()}
     * to the given target, using the configured {@link PropertyAccessStrategy}
     *
     * @param target must not be null
     */
    public void apply(final Object target) {
        writeProperty(target, generatedValue);
    }

    /**
     * Asserts that the value previously set by {@link #apply(Object)} is accessible
     * by the corresponding read-method, e.g getProperty
     *
     * @param target must not be null
     */
    public void assertValueSet(final Object target) {
        assertValueSet(target, generatedValue);
    }

    /**
     * Asserts that the expected value is accessible by the corresponding
     * read-method, e.g getProperty
     *
     * @param target   must not be null
     * @param expected the expected content of the attribute
     */
    public void assertValueSet(final Object target, Object expected) {
        final var actual = readProperty(target);

        if (AssertionStrategy.COLLECTION_IGNORE_ORDER.equals(propertyMetadata.getAssertionStrategy())) {
            CollectionAsserts.assertListsAreEqualIgnoringOrder(propertyMetadata.getName(), expected, actual);
        } else {
            assertEquals(expected, actual, "Invalid content found for property " + propertyMetadata.getName()
                    + ", expected=" + expected + ", actual=" + actual);
        }

    }

    /**
     * Reads and returns the read property from the given target
     *
     * @param target to be read from, must not be null
     * @return he read Property, may be {@code null}
     */
    public Object readProperty(Object target) {
        assertNotNull(target, TARGET_MUST_NOT_BE_NULL);
        return propertyMetadata.getPropertyAccessStrategy().readProperty(target, propertyMetadata);
    }

    /**
     * Writes the given property the given target
     *
     * @param target        to be written to, must not be null
     * @param propertyValue to be written
     * @return he read Property, may be {@code null}
     */
    public Object writeProperty(Object target, Object propertyValue) {
        assertNotNull(target, TARGET_MUST_NOT_BE_NULL);
        return propertyMetadata.getPropertyAccessStrategy().writeProperty(target, propertyMetadata, propertyValue);
    }

    /**
     * Asserts that the given object provides a default value.
     *
     * @param target must not be null
     */
    public void assertDefaultValue(final Object target) {

        assertNotNull(target, TARGET_MUST_NOT_BE_NULL);

        assertTrue(propertyMetadata.isDefaultValue(),
                "There is no default value set: Invalid configuration for property " + propertyMetadata.getName());

        assertNotNull(readProperty(target), "No defaultValue found for property " + propertyMetadata.getName());
    }

    /**
     * Creates a copy of this instance
     *
     * @param withGeneratedValue indicating whether to copy the currently
     *                           {@link #generatedValue}
     * @return the created copy
     */
    public PropertySupport createCopy(final boolean withGeneratedValue) {
        final var support = new PropertySupport(propertyMetadata);
        if (withGeneratedValue) {
            support.setGeneratedValue(generatedValue);
        }
        return support;
    }

    /**
     * Creates a copy of this instance. In addition it tries to create a
     * generatedValue that is not equal to the contained one. It will try this 50
     * times and will then throw an {@link AssertionError}
     *
     * @return the created copy
     */
    public PropertySupport createCopyWithNonEqualValue() {
        final var support = new PropertySupport(propertyMetadata);
        if (null == getGeneratedValue()) {
            support.generateTestValue();
            return support;
        }
        var times = 0;
        final var initialTestValue = getGeneratedValue();
        while (true) {
            final var otherValue = support.generateTestValue();
            if (!initialTestValue.equals(otherValue)) {
                break;
            }
            times++;
            if (ENTROPY_GUARD == times) {
                throw new AssertionError("Unable to create non equal test-value for " + propertyMetadata);
            }
        }
        return support;
    }
}
