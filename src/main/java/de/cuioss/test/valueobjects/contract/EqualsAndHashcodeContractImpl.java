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
package de.cuioss.test.valueobjects.contract;

import de.cuioss.test.valueobjects.api.object.ObjectTestConfig;
import de.cuioss.test.valueobjects.api.object.ObjectTestContract;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.property.PropertySupport;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.property.PropertyMemberInfo;

import java.util.*;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Helper class providing base functionality for test for the
 * {@link Object#equals(Object)} and {@link Object#hashCode()} variants of
 * classes.
 *
 * @author Oliver Wolff
 */
public class EqualsAndHashcodeContractImpl implements ObjectTestContract {

    private static final CuiLogger LOGGER = new CuiLogger(EqualsAndHashcodeContractImpl.class);

    /**
     * The two most recently added properties are the ones deliberately mutated to an unequal
     * value while iterating; the remainder is used to build the variant objects. This offset
     * captures that "keep the last two out of the growing prefix" intent.
     */
    private static final int UNEQUAL_VARIANT_OFFSET = 2;

    @Override
    public void assertContract(final ParameterizedInstantiator<?> instantiator,
        final ObjectTestConfig objectTestConfig) {

        requireNonNull(instantiator, "parameterizedInstantiator must not be null");

        LOGGER.info("Verifying " + getClass().getName() + "\nWith configuration: " + instantiator);

        final Object target = instantiator.newInstanceMinimal();
        assertBasicContractOnEquals(target);
        ReflectionUtil.assertHashCodeMethodIsOverriden(target.getClass());
        assertBasicContractOnHashCode(target);

        if (shouldTestPropertyContract(objectTestConfig)) {
            executePropertyTests(instantiator, objectTestConfig);
        } else {
            LOGGER.info("Only checking basic contract of equals() and hashCode()");
        }

    }

    /**
     * Executes property-based tests for equals and hashCode methods.
     *
     * @param instantiator the instantiator used to create test objects
     * @param objectTestConfig the configuration for object testing
     */
    private static void executePropertyTests(final ParameterizedInstantiator<?> instantiator,
        final ObjectTestConfig objectTestConfig) {
        final SortedSet<String> consideredAttributes = new TreeSet<>();
        instantiator.getRuntimeProperties().getWritableProperties().stream()
            .filter(p -> PropertyMemberInfo.DEFAULT.equals(p.getPropertyMemberInfo()))
            .forEach(p -> consideredAttributes.add(p.getName()));

        if (null != objectTestConfig) {
            // Whitelist takes precedence
            if (objectTestConfig.equalsAndHashCodeOf().length > 0) {
                consideredAttributes.clear();
                consideredAttributes.addAll(Arrays.asList(objectTestConfig.equalsAndHashCodeOf()));
            } else {
                Arrays.asList(objectTestConfig.equalsAndHashCodeExclude()).forEach(consideredAttributes::remove);
            }
        }
        if (consideredAttributes.isEmpty()) {
            LOGGER.debug("No configured properties to be tested. Is this intentional?");
        } else {
            LOGGER.info("Configured attributes found for equalsAndHashCode-testing: " + consideredAttributes);
            assertEqualsAndHashCodeWithVariants(instantiator, consideredAttributes);
        }
    }

    /**
     * Determines whether property contract testing should be performed.
     *
     * @param objectTestConfig the configuration for object testing
     * @return true if property contract testing should be performed, false otherwise
     */
    private static boolean shouldTestPropertyContract(final ObjectTestConfig objectTestConfig) {
        return null == objectTestConfig || !objectTestConfig.equalsAndHashCodeBasicOnly();
    }

    /**
     * Asserts the {@link Object#equals(Object)} and {@link Object#hashCode()}
     * contract with variants of data.
     *
     * @param instantiator the instantiator used to create test objects
     * @param consideredAttributes the set of attribute names to consider for testing
     */
    private static void assertEqualsAndHashCodeWithVariants(final ParameterizedInstantiator<?> instantiator,
        final SortedSet<String> consideredAttributes) {

        assertEqualsAndHasCodeWithAllPropertiesSet(instantiator, consideredAttributes);

        assertEqualsAndHashCodeWithSkippingProperties(instantiator, consideredAttributes);

        assertEqualsAndHashCodeWithChangingProperties(instantiator, consideredAttributes);

    }

    /**
     * Assert the methods {@link Object#equals(Object)} and
     * {@link Object#hashCode()} with all properties set
     *
     * @param instantiator the instantiator used to create test objects
     * @param consideredAttributes the set of attribute names to consider for testing
     */
    private static void assertEqualsAndHasCodeWithAllPropertiesSet(final ParameterizedInstantiator<?> instantiator,
        final SortedSet<String> consideredAttributes) {
        final Collection<String> actualAttributes = new ArrayList<>(consideredAttributes);

        final Collection<String> requiredNames = RuntimeProperties
            .extractNames(instantiator.getRuntimeProperties().getRequiredProperties());

        actualAttributes.addAll(requiredNames);

        final var properties = instantiator.getRuntimeProperties().getWritableAsPropertySupport(true, actualAttributes);

        final Object fullObject1 = instantiator.newInstance(properties, false);
        final Object fullObject2 = instantiator.newInstance(properties, false);

        assertEquals(fullObject1, fullObject2, "Objects should be equal with all properties set");

        assertEquals(fullObject2, fullObject1,
            "Objects should be equal with all properties set, but are not symmetric");

        assertBasicContractOnHashCode(fullObject1);
    }

    /**
     * Assert the methods {@link Object#equals(Object)} and
     * {@link Object#hashCode()} with the given set of properties and iterates
     * accordingly the available variants
     *
     * @param instantiator the instantiator used to create test objects
     * @param consideredAttributes the set of attribute names to consider for testing
     */
    private static void assertEqualsAndHashCodeWithSkippingProperties(final ParameterizedInstantiator<?> instantiator,
        final Set<String> consideredAttributes) {

        final var information = instantiator.getRuntimeProperties();

        final var allWritableProperties = information.getWritableAsPropertySupport(true).stream()
            .filter(prop -> consideredAttributes.contains(prop.getName())).toList();

        final var nonDefaultProperties = allWritableProperties.stream().filter(prop -> !prop.isDefaultValue()).toList();

        final var requiredProperties = nonDefaultProperties.stream().filter(PropertySupport::isRequired).toList();

        final var additionalProperties = nonDefaultProperties.stream().filter(property -> !property.isRequired())
            .toList();

        final var upperBound = Math.max(0,
            Math.min(nonDefaultProperties.size(), consideredAttributes.size()) - UNEQUAL_VARIANT_OFFSET);
        if (additionalProperties.isEmpty()) {
            LOGGER.info("Only required or default properties found, therefore no further testing");
        } else {
            final Object minimalObject = instantiator.newInstance(requiredProperties, false);
            final Object fullObject = instantiator.newInstance(allWritableProperties, false);
            // Common Order of properties
            verifyProperties(instantiator, additionalProperties, upperBound, minimalObject, fullObject,
                new ArrayList<>(requiredProperties));
            // reverse Order of additional properties
            verifyProperties(instantiator, additionalProperties.reversed(), upperBound, minimalObject, fullObject,
                new ArrayList<>(requiredProperties));
        }

    }

    /**
     * Verifies properties by iterating through the given (already ordered) additional
     * properties. Callers control the iteration direction by passing the list in the
     * desired order (e.g. {@link List#reversed()} for reverse iteration).
     *
     * @param instantiator the instantiator used to create test objects
     * @param additionalProperties the list of additional properties to verify, in iteration order
     * @param upperBound the upper bound for the number of properties to add
     * @param minimalObject the minimal object for comparison
     * @param fullObject the full object for comparison
     * @param iteratingProperties the list of properties being iterated
     */
    private static void verifyProperties(ParameterizedInstantiator<?> instantiator,
        List<PropertySupport> additionalProperties, int upperBound, Object minimalObject, Object fullObject,
        List<PropertySupport> iteratingProperties) {
        for (final PropertySupport support : additionalProperties) {
            if (iteratingProperties.size() < upperBound) {
                iteratingProperties.add(support);
            } else {
                // Special case for the last property to be set but the objects
                // still need to be unequal. For this last property to be iterated the value
                // will be set to an explicit unequal value:
                iteratingProperties.add(support.createCopyWithNonEqualValue());
            }
            final Object iterating = instantiator.newInstance(iteratingProperties, false);
            final var current = support.getName();
            assertEqualObjectAreNotEqual(minimalObject, iterating, current);
            assertEqualObjectAreNotEqual(fullObject, iterating, current);
            assertBasicContractOnHashCode(iterating);
        }
    }

    /**
     * Asserts equals and hashCode behavior when changing individual properties.
     *
     * @param instantiator the instantiator used to create test objects
     * @param consideredAttributes the set of attribute names to consider for testing
     */
    private static void assertEqualsAndHashCodeWithChangingProperties(final ParameterizedInstantiator<?> instantiator,
        final SortedSet<String> consideredAttributes) {
        final Map<String, PropertySupport> allWritableProperties = new HashMap<>();

        instantiator.getRuntimeProperties().getWritableAsPropertySupport(true)
            .forEach(p -> allWritableProperties.put(p.getName(), p));

        final Object expected = instantiator.newInstance(new ArrayList<>(allWritableProperties.values()), false);
        for (final String name : consideredAttributes) {
            final Map<String, PropertySupport> current = new HashMap<>(allWritableProperties);
            assertTrue(current.containsKey(name), "Invalid configuration found: " + name + " not defined as property.");
            current.put(name, current.get(name).createCopyWithNonEqualValue());
            final Object actual = instantiator.newInstance(new ArrayList<>(current.values()), false);
            assertEqualObjectAreNotEqual(expected, actual, name);
        }
        // Now reverse order, using the SequencedCollection#reversed() view (Java 21)
        for (final String name : consideredAttributes.reversed()) {
            final Map<String, PropertySupport> current = new HashMap<>(allWritableProperties);
            assertTrue(current.containsKey(name), "Invalid configuration found: " + name + " not defined as property.");
            current.put(name, current.get(name).createCopyWithNonEqualValue());
            final Object actual = instantiator.newInstance(new ArrayList<>(current.values()), false);
            assertEqualObjectAreNotEqual(expected, actual, name);
        }
    }

    /**
     * Asserts that two objects are not equal and provides detailed error message.
     *
     * @param expected the expected object
     * @param actual the actual object
     * @param deltaPropertyName the name of the property that differs between objects
     */
    private static void assertEqualObjectAreNotEqual(final Object expected, final Object actual,
        final String deltaPropertyName) {
        final var message = "The Objects of type " + expected.getClass().getName() +
            " should not be equal, current property=" + deltaPropertyName;
        assertNotEquals(expected, actual, message);
        assertNotEquals(actual, expected, message);
    }

    /**
     * Verify object has implemented {@link Object#equals(Object)} method. In
     * addition, it checks whether the basic functionality like
     * <ul>
     * <li>equals(null) will be 'false'</li>
     * <li>equals(new Object()) will be 'false'</li>
     * <li>underTest.equals(underTest) will be 'true'</li>
     * </ul>
     * is implemented correctly
     *
     * @param underTest object under test
     */
    // squid:S2159 / squid:S1764 / PMD.EqualsNull: the x.equals(x), x.equals(null) and
    // x.equals(new Object()) calls are intentional — verifying the equals contract requires
    // invoking the object's own equals implementation directly (JUnit's assertNotEquals would
    // short-circuit and never call it). These hold only when equals is implemented correctly,
    // which is exactly what this method checks.
    @SuppressWarnings({"squid:S2159", "squid:S1764", "PMD.EqualsNull"})
    public static void assertBasicContractOnEquals(final Object underTest) {

        ReflectionUtil.assertEqualsMethodIsOverriden(underTest.getClass());

        // basic checks to equals implementation
        final var msgNotEqualsNull = "Expected result for equals(null) will be 'false'. Class was : "
            + underTest.getClass();

        // Note: the argument order matters. assertNotEquals(null, underTest) / assertNotEquals(new Object(),
        // underTest) would short-circuit on the JUnit side and never invoke underTest.equals(...), so a broken
        // equals would pass. We therefore assert directly on underTest.equals(...).
        assertFalse(underTest.equals(null), msgNotEqualsNull);

        final var msgNotEqualsObject = "Expected result for equals(new Object()) will be 'false'. Class was : "
            + underTest.getClass();

        assertFalse(underTest.equals(new Object()), msgNotEqualsObject);

        final var msgEqualsToSelf = "Expected result for equals(underTest) will be 'true'. Class was : "
            + underTest.getClass();

        assertEquals(underTest, underTest, msgEqualsToSelf);

    }

    /**
     * Verify object has implemented {@link Object#hashCode()} method. In addition, it
     * checks the basic self-consistency requirement of {@link Object#hashCode()}: repeated
     * invocations on the same object must yield the same result. A {@code hashCode()} of
     * {@code 0} is perfectly legal (e.g. an object with all-null fields under
     * {@link java.util.Objects#hash(Object...)}) and must therefore not be rejected.
     *
     * @param underTest object under test
     */
    public static void assertBasicContractOnHashCode(final Object underTest) {

        // basic checks to hashCode implementation: it must be consistent across invocations.
        // A value of 0 is a valid hashCode per the Object#hashCode contract and must not fail.
        final var firstResult = underTest.hashCode();
        assertEquals(firstResult, underTest.hashCode(),
            "hashCode() must consistently return the same value on repeated invocations. Class was : "
                + underTest.getClass());
    }

}
