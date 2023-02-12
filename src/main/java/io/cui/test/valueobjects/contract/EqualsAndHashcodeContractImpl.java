package io.cui.test.valueobjects.contract;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import io.cui.test.valueobjects.api.object.ObjectTestConfig;
import io.cui.test.valueobjects.api.object.ObjectTestContract;
import io.cui.test.valueobjects.objects.ParameterizedInstantiator;
import io.cui.test.valueobjects.objects.RuntimeProperties;
import io.cui.test.valueobjects.property.PropertySupport;
import io.cui.tools.logging.CuiLogger;
import io.cui.tools.property.PropertyMemberInfo;

/**
 * Helper class providing base functionality for test for the
 * {@link Object#equals(Object)} and {@link Object#hashCode()} variants of classes.
 *
 * @author Oliver Wolff (i001428)
 */
public class EqualsAndHashcodeContractImpl implements ObjectTestContract {

    private static final Integer DEFAULT_INT_VALUE = 0;

    private static final CuiLogger log = new CuiLogger(EqualsAndHashcodeContractImpl.class);

    @Override
    public void assertContract(final ParameterizedInstantiator<?> instantiator,
            final ObjectTestConfig objectTestConfig) {

        requireNonNull(instantiator, "parameterizedInstantiator must not be null");

        final var builder = new StringBuilder("Verifying ");
        builder.append(getClass().getName()).append("\nWith configuration: ")
                .append(instantiator.toString());
        log.info(builder.toString());

        final Object target = instantiator.newInstanceMinimal();
        assertBasicContractOnEquals(target);
        ReflectionUtil.assertHashCodeMethodIsOverriden(target.getClass());
        assertBasicContractOnHashCode(target);

        if (shouldTestPropertyContract(objectTestConfig)) {
            executePropertyTests(instantiator, objectTestConfig);
        } else {
            log.info("Only checking basic contract of equals() and hasCode()");
        }

    }

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
                consideredAttributes
                        .removeAll(Arrays.asList(objectTestConfig.equalsAndHashCodeExclude()));
            }
        }
        if (consideredAttributes.isEmpty()) {
            log.debug("No configured properties to be tested. Is this intentional?");
        } else {
            log.info("Configured attributes found for equalsAndHashCode-testing: "
                    + consideredAttributes);
            assertEqualsAndHashCodeWithVariants(instantiator, consideredAttributes);
        }
    }

    private static boolean shouldTestPropertyContract(final ObjectTestConfig objectTestConfig) {
        return null == objectTestConfig || !objectTestConfig.equalsAndHashCodeBasicOnly();
    }

    /**
     * Asserts the {@link Object#equals(Object)} and {@link Object#hashCode()} contract with
     * variants of data.
     *
     * @param instantiator
     */
    private static void assertEqualsAndHashCodeWithVariants(
            final ParameterizedInstantiator<?> instantiator,
            final SortedSet<String> consideredAttributes) {

        assertEqualsAndHasCodeWithAllPropertiesSet(instantiator, consideredAttributes);

        assertEqualsAndHashCodeWithSkippingProperties(instantiator, consideredAttributes);

        assertEqualsAndHashCodeWithChangingProperties(instantiator, consideredAttributes);

    }

    /**
     * Assert the methods {@link Object#equals(Object)} and {@link Object#hashCode()} with all
     * properties set
     *
     * @param instantiator
     * @param consideredAttributes
     */
    private static void assertEqualsAndHasCodeWithAllPropertiesSet(
            final ParameterizedInstantiator<?> instantiator,
            final SortedSet<String> consideredAttributes) {
        final Collection<String> actualAttributes = new ArrayList<>(consideredAttributes);

        final Collection<String> requiredNames = RuntimeProperties
                .extractNames(instantiator.getRuntimeProperties().getRequiredProperties());

        actualAttributes.addAll(requiredNames);

        final var properties =
            instantiator.getRuntimeProperties().getWritableAsPropertySupport(true, actualAttributes);

        final Object fullObject1 = instantiator.newInstance(properties, false);
        final Object fullObject2 = instantiator.newInstance(properties, false);

        assertEquals(
                fullObject1, fullObject2, "Objects should be equal with all properties set");

        assertEquals(fullObject2, fullObject1,
                "Objects should be equal with all properties set, but are not symmetric");

        assertBasicContractOnHashCode(fullObject1);
    }

    /**
     * Assert the methods {@link Object#equals(Object)} and {@link Object#hashCode()} with the given
     * set of properties and iterates accordingly the available variants
     *
     * @param instantiator
     * @param consideredAttributes
     */
    private static void assertEqualsAndHashCodeWithSkippingProperties(
            final ParameterizedInstantiator<?> instantiator,
            final Set<String> consideredAttributes) {

        final var information = instantiator.getRuntimeProperties();

        final List<PropertySupport> allWritableProperties =
            information.getWritableAsPropertySupport(true).stream()
                    .filter(prop -> consideredAttributes.contains(prop.getName()))
                    .collect(Collectors.toList());

        final List<PropertySupport> nonDefaultProperties = allWritableProperties.stream()
                .filter(prop -> !prop.isDefaultValue())
                .collect(Collectors.toList());

        final List<PropertySupport> requiredProperties = nonDefaultProperties.stream()
                .filter(PropertySupport::isRequired)
                .collect(Collectors.toList());

        final List<PropertySupport> additionalProperties = nonDefaultProperties.stream()
                .filter(property -> !property.isRequired())
                .collect(Collectors.toList());

        final var upperBound =
            Math.min(nonDefaultProperties.size(), consideredAttributes.size()) - 2;
        if (additionalProperties.isEmpty()) {
            log.info("Only required or default properties found, therefore no further testing");
        } else {
            final Object minimalObject =
                instantiator.newInstance(requiredProperties, false);
            final Object fullObject =
                instantiator.newInstance(allWritableProperties, false);
            List<PropertySupport> iteratingProperties = new ArrayList<>(requiredProperties);
            // Common Order of properties
            for (final PropertySupport support : additionalProperties) {
                if (iteratingProperties.size() < upperBound) {
                    iteratingProperties.add(support);
                } else {
                    // Special case for the last property to be set but the objects
                    // still need to be unequal. For this last property to be iterated the value
                    // will be set to an explicit unequal value:
                    iteratingProperties.add(support.createCopyWithNonEqualValue());
                }
                final Object iterating =
                    instantiator.newInstance(iteratingProperties, false);
                final var current = support.getName();
                assertEqualObjectAreNotEqual(minimalObject, iterating,
                        current);
                assertEqualObjectAreNotEqual(fullObject, iterating,
                        current);
                assertBasicContractOnHashCode(iterating);
            }
            // reverse Order of additional properties
            iteratingProperties = new ArrayList<>(requiredProperties);
            final List<PropertySupport> reverseAddtionalProperties =
                new ArrayList<>(additionalProperties);
            Collections.reverse(reverseAddtionalProperties);
            for (final PropertySupport support : reverseAddtionalProperties) {
                if (iteratingProperties.size() < upperBound) {
                    iteratingProperties.add(support);
                } else {
                    iteratingProperties.add(support.createCopyWithNonEqualValue());
                }
                final Object iterating =
                    instantiator.newInstance(iteratingProperties, false);
                final var current = support.getName();
                assertEqualObjectAreNotEqual(minimalObject, iterating,
                        current);
                assertEqualObjectAreNotEqual(fullObject, iterating,
                        current);
                assertBasicContractOnHashCode(iterating);
            }
        }

    }

    private static void assertEqualsAndHashCodeWithChangingProperties(
            final ParameterizedInstantiator<?> instantiator,
            final SortedSet<String> consideredAttributes) {
        final Map<String, PropertySupport> allWritableProperties = new HashMap<>();

        instantiator.getRuntimeProperties()
                .getWritableAsPropertySupport(true).forEach(p -> allWritableProperties.put(p.getName(), p));

        final Object expected =
            instantiator.newInstance(new ArrayList<>(allWritableProperties.values()), false);
        for (final String name : consideredAttributes) {
            final Map<String, PropertySupport> current = new HashMap<>(allWritableProperties);
            assertTrue(
                    current.containsKey(name), "Invalid configuration found: " + name + " not defined as property.");
            current.put(name, current.get(name).createCopyWithNonEqualValue());
            final Object actual =
                instantiator.newInstance(new ArrayList<>(current.values()), false);
            assertEqualObjectAreNotEqual(expected, actual, name);
        }
        // Now reverse order
        final List<String> reverse = new ArrayList<>(consideredAttributes);
        Collections.reverse(reverse);
        for (final String name : reverse) {
            final Map<String, PropertySupport> current = new HashMap<>(allWritableProperties);
            assertTrue(current.containsKey(name), "Invalid configuration found: " + name + " not defined as property.");
            current.put(name, current.get(name).createCopyWithNonEqualValue());
            final Object actual =
                instantiator.newInstance(new ArrayList<>(current.values()), false);
            assertEqualObjectAreNotEqual(expected, actual, name);
        }
    }

    private static void assertEqualObjectAreNotEqual(final Object expected, final Object actual,
            final String deltaPropertyName) {
        final var message = new StringBuilder("The Objects of type ")
                .append(expected.getClass().getName())
                .append(" should not be equal, current property=")
                .append(deltaPropertyName).toString();
        assertNotEquals(expected, actual, message);
        assertNotEquals(actual, expected, message);
    }

    /**
     * Verify object has implemented {@link Object#equals(Object)} method. In addition it checks
     * whether the basic functionality like
     * <ul>
     * <li>equals(null) will be 'false'</li>
     * <li>equals(new Object()) will be 'false'</li>
     * <li>underTest.equals(underTest) will be 'true'</li>
     * </ul>
     * is implemented correctly
     *
     * @param underTest
     *            object under test
     */
    @SuppressWarnings({ "squid:S2159", "squid:S1764" }) // Sonar complains that the x.equals(x) is
                                                        // always true. This is
    // only the case if implemented correctly, what is checked
    // within this test
    public static void assertBasicContractOnEquals(final Object underTest) {

        ReflectionUtil.assertEqualsMethodIsOverriden(underTest.getClass());

        // basic checks to equals implementation
        final var msgNotEqualsNull =
            "Expected result for equals(null) will be 'false'. Class was : " + underTest.getClass();

        assertFalse(underTest.equals(null), msgNotEqualsNull);

        final var msgNotEqualsObject =
            "Expected result for equals(new Object()) will be 'false'. Class was : "
                    + underTest.getClass();

        assertFalse(underTest.equals(new Object()), msgNotEqualsObject);

        final var msgEqualsToSelf =
            "Expected result for equals(underTest) will be 'true'. Class was : "
                    + underTest.getClass();

        assertTrue(underTest.equals(underTest), msgEqualsToSelf);

    }

    /**
     * Verify object has implemented {@link Object#hashCode()} method.
     *
     * @param underTest
     *            object under test
     */
    public static void assertBasicContractOnHashCode(final Object underTest) {

        // basic checks to hashCode implementation
        assertNotEquals(DEFAULT_INT_VALUE, underTest.hashCode(),
                "Expected result of hashCode method is not '0'. Class was : "
                        + underTest.getClass());
    }

}
