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
package de.cuioss.test.valueobjects;

import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.valueobjects.api.ObjectContractTestSupport;
import de.cuioss.test.valueobjects.api.TestContract;
import de.cuioss.test.valueobjects.api.contracts.VerifyBeanProperty;
import de.cuioss.test.valueobjects.api.contracts.VerifyFactoryMethod;
import de.cuioss.test.valueobjects.api.object.ObjectTestConfig;
import de.cuioss.test.valueobjects.api.object.ObjectTestContracts;
import de.cuioss.test.valueobjects.api.object.VetoObjectTestContract;
import de.cuioss.test.valueobjects.contract.ContractRegistry;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.objects.impl.AbstractInlineInstantiator;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.util.ObjectContractHelper;
import lombok.AccessLevel;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Base-class for running tests on value-objects. It runs two type of tests:
 * <ul>
 * <li>Tests for the canonical {@link Object} methods
 * {@link Object#equals(Object)}, {@link Object#hashCode()} and
 * {@link Object#toString()} with each test can be vetoed using
 * {@link VetoObjectTestContract} at type-level. See
 * {@link de.cuioss.test.valueobjects.api.object} for details</li>
 * <li>Individual contract-testing like {@link VerifyBeanProperty} or
 * {@link VerifyFactoryMethod}, see
 * {@link de.cuioss.test.valueobjects.api.contracts} for details.</li>
 * </ul>
 * <h2>Configuration</h2>
 * <p>
 * See {@link PropertyAwareTest} for details on configuring
 * {@link PropertyMetadata} and {@link TypedGenerator}
 * </p>
 * Usage examples can be found at the package-documentation:
 * {@link de.cuioss.test.valueobjects.junit5}
 *
 * @param <T> identifying the type to be tested is usually but not necessarily
 *            at least {@link Serializable}.
 * @author Oliver Wolff
 */
public class ValueObjectTest<T> extends PropertyAwareTest<T> implements ObjectContractTestSupport {

    public static final String ANY_VALUE_OBJECT_NEEDED = "You need to configure either at least one de.cuioss.test.valueobjects.api.contracts or implement #anyValueObject()";

    /**
     * The active object-contracts to be tested
     */
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
    void initializeBaseClass() {
        activeObjectContracts = ObjectContractHelper.handleVetoedContracts(getClass());

        testContracts = resolveTestContracts(getPropertyMetadata());

        objectContractInstantiator = new ArrayList<>();
        testContracts.forEach(contract -> objectContractInstantiator.add(contract.getInstantiator()));
    }

    /**
     * Resolves the concrete {@link TestContract}s to be tested. They are derived by
     * the corresponding annotations
     *
     * @param initialMetadata
     * @return
     */
    protected List<TestContract<T>> resolveTestContracts(final List<PropertyMetadata> initialMetadata) {
        return ContractRegistry.resolveTestContracts(getTargetBeanClass(), getClass(), initialMetadata);
    }

    @Override
    @Test
    public void shouldImplementObjectContracts() {
        var instantiators = getObjectContractInstantiator();
        if (instantiators.isEmpty()) {
            assertNotNull(anyValueObject(), ANY_VALUE_OBJECT_NEEDED);
            instantiators = immutableList(new AbstractInlineInstantiator<>() {

                @Override
                protected T any() {
                    return anyValueObject();
                }
            });
        }
        final var objectTestConfig = this.getClass().getAnnotation(ObjectTestConfig.class);
        for (final ParameterizedInstantiator<T> instantiator : instantiators) {
            for (final ObjectTestContracts objectTestContracts : activeObjectContracts) {
                objectTestContracts.newObjectTestInstance().assertContract(instantiator, objectTestConfig);
            }
        }
    }

    /**
     * <p>
     * Tests all configured {@link TestContract}s. The individual contracts are to
     * be configured using class level annotations or overwriting
     * resolveTestContracts(SortedSet)
     * </p>
     */
    @Test
    final void shouldVerifyTestContracts() {
        for (final TestContract<T> contract : getTestContracts()) {
            contract.assertContract();
        }
    }

    /**
     * This method can be used in two ways:
     * <ul>
     * <li>In case you have at least de.cuioss.test.valueobjects.api.contracts
     * configured this method will implicitly return an arbitrary {@link Object} of
     * the implicitly created underlying {@link ParameterizedInstantiator}</li>
     * <li>In case you have no {@link ParameterizedInstantiator} configured you can
     * implement this method for feeding the
     * {@link #shouldImplementObjectContracts()}</li>
     * </ul>
     *
     * @return a concrete valueObject, or null
     */
    protected T anyValueObject() {
        if (!getObjectContractInstantiator().isEmpty()) {
            return getObjectContractInstantiator().getFirst().newInstanceFull();
        }
        return null;
    }
}
