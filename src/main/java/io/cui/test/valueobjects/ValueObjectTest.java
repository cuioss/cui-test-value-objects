package io.cui.test.valueobjects;

import static io.cui.tools.collect.CollectionLiterals.immutableList;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.cui.test.generator.TypedGenerator;
import io.cui.test.valueobjects.api.ObjectContractTestSupport;
import io.cui.test.valueobjects.api.TestContract;
import io.cui.test.valueobjects.api.contracts.VerifyBeanProperty;
import io.cui.test.valueobjects.api.contracts.VerifyFactoryMethod;
import io.cui.test.valueobjects.api.object.ObjectTestConfig;
import io.cui.test.valueobjects.api.object.ObjectTestContracts;
import io.cui.test.valueobjects.api.object.VetoObjectTestContract;
import io.cui.test.valueobjects.contract.ContractRegistry;
import io.cui.test.valueobjects.objects.ParameterizedInstantiator;
import io.cui.test.valueobjects.objects.impl.AbstractInlineInstantiator;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.util.ObjectContractHelper;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Base-class for running tests on value-objects. It runs two type of tests:
 * <ul>
 * <li>Tests for the canonical {@link Object} methods {@link Object#equals(Object)},
 * {@link Object#hashCode()} and {@link Object#toString()} with each
 * test can be vetoed using {@link VetoObjectTestContract} at type-level. See
 * {@link io.cui.test.valueobjects.api.object} for details</li>
 * <li>Individual contract-testing like {@link VerifyBeanProperty} or {@link VerifyFactoryMethod},
 * see {@link io.cui.test.valueobjects.api.contracts} for details.
 * </li>
 * </ul>
 * <h2>Configuration</h2>
 * <p>
 * See {@link PropertyAwareTest} for details on configuring {@link PropertyMetadata} and
 * {@link TypedGenerator}
 * </p>
 * Usage examples can be found at the package-documentation:
 * {@link io.cui.test.valueobjects.junit5}
 *
 * @author Oliver Wolff
 * @param <T> identifying the type to be tested is usually but not necessarily at least
 *            {@link Serializable}.
 */
public class ValueObjectTest<T> extends PropertyAwareTest<T> implements ObjectContractTestSupport {

    /** The active object-contracts to be tested */
    private Set<ObjectTestContracts> activeObjectContracts;

    /**
     * Needed {@link ParameterizedInstantiator} for creating test Objects or
     * {@link #shouldImplementObjectContracts()}
     */
    @Getter(AccessLevel.PROTECTED)
    private List<ParameterizedInstantiator<T>> objectContractInstantiator;

    @Getter
    private List<TestContract<T>> testContracts;

    /**
     * Initializes all contracts
     */
    @BeforeEach
    public void initializeBaseClass() {
        activeObjectContracts = ObjectContractHelper.handleVetoedContracts(getClass());

        testContracts = resolveTestContracts(getPropertyMetadata());

        objectContractInstantiator = new ArrayList<>();
        testContracts.forEach(contract -> this.objectContractInstantiator
                .add(contract.getInstantiator()));
    }

    /**
     * Resolves the concrete {@link TestContract}s to be tested. They are derived by the
     * corresponding annotations
     *
     * @param initialMetadata
     * @return
     */
    protected List<TestContract<T>> resolveTestContracts(
            final List<PropertyMetadata> initialMetadata) {
        return ContractRegistry.resolveTestContracts(getTargetBeanClass(), getClass(), initialMetadata);
    }

    @Override
    @Test
    public void shouldImplementObjectContracts() {
        var instantiators = getObjectContractInstantiator();
        if (instantiators.isEmpty()) {
            assertNotNull(
                    anyValueObject(),
                    "You need to configure either at least one io.cui.test.valueobjects.api.contracts or implement #anyValueObject()");
            instantiators = immutableList(new AbstractInlineInstantiator<>() {

                @Override
                protected T any() {
                    return anyValueObject();
                }
            });
        }
        final var objectTestConfig =
            this.getClass().getAnnotation(ObjectTestConfig.class);
        for (final ParameterizedInstantiator<T> instantiator : instantiators) {
            for (final ObjectTestContracts objectTestContracts : this.activeObjectContracts) {
                objectTestContracts.newObjectTestInstance().assertContract(instantiator,
                        objectTestConfig);
            }
        }
    }

    /**
     * <p>
     * Tests all configured {@link TestContract}s. The individual contracts are to be configured
     * using class level annotations or overwriting resolveTestContracts(SortedSet)
     * </p>
     */
    @Test
    public final void shouldVerifyTestContracts() {
        for (final TestContract<T> contract : getTestContracts()) {
            contract.assertContract();
        }
    }

    /**
     * This method can be used in two ways:
     * <ul>
     * <li>In case you have at least io.cui.test.valueobjects.api.contracts configured
     * this method will implicitly return an arbitrary {@link Object} of the implicitly created
     * underlying {@link ParameterizedInstantiator}</li>
     * <li>In case you have no {@link ParameterizedInstantiator} configured you can implement this
     * method for feeding the {@link #shouldImplementObjectContracts()}</li>
     * </ul>
     *
     * @return a concrete valueObject, or null
     */
    protected T anyValueObject() {
        if (!getObjectContractInstantiator().isEmpty()) {
            return getObjectContractInstantiator().iterator().next().newInstanceFull();
        }
        return null;
    }
}
