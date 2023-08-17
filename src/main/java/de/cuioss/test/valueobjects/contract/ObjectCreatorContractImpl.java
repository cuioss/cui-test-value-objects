/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.contract;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import de.cuioss.test.valueobjects.api.TestContract;
import de.cuioss.test.valueobjects.api.contracts.VerifyConstructor;
import de.cuioss.test.valueobjects.api.contracts.VerifyConstructors;
import de.cuioss.test.valueobjects.api.contracts.VerifyFactoryMethod;
import de.cuioss.test.valueobjects.api.contracts.VerifyFactoryMethods;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.impl.ConstructorBasedInstantiator;
import de.cuioss.test.valueobjects.objects.impl.FactoryBasedInstantiator;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.PropertySupport;
import de.cuioss.test.valueobjects.util.AnnotationHelper;
import de.cuioss.tools.collect.CollectionBuilder;
import de.cuioss.tools.logging.CuiLogger;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * TestContract for dealing Constructor and factories, {@link VerifyConstructor}
 * and {@link VerifyFactoryMethod} respectively
 *
 * @author Oliver Wolff
 * @param <T> identifying the objects to be tested.
 */
@RequiredArgsConstructor
public class ObjectCreatorContractImpl<T> implements TestContract<T> {

    private static final CuiLogger log = new CuiLogger(ObjectCreatorContractImpl.class);

    @Getter
    @NonNull
    private final ParameterizedInstantiator<T> instantiator;

    @Override
    public void assertContract() {
        final var builder = new StringBuilder("Verifying ");
        builder.append(getClass().getName()).append("\nWith configuration: ").append(instantiator.toString());
        log.info(builder.toString());

        shouldPersistAllParameter();
        shouldHandleRequiredAndDefaults();
        shouldFailOnMissingRequiredAttributes();
    }

    private void shouldFailOnMissingRequiredAttributes() {
        final var information = getInstantiator().getRuntimeProperties();
        final var required = information.getRequiredAsPropertySupport(true);

        for (final PropertySupport support : required) {
            if (!support.isPrimitive()) {
                final List<PropertySupport> iterating = new ArrayList<>(required);
                iterating.remove(support);
                iterating.add(support.createCopy(false));
                var failed = false;
                try {
                    getInstantiator().newInstance(iterating, false);
                    failed = true;
                } catch (final AssertionError e) {
                    // expected
                }
                if (failed) {
                    throw new AssertionError("Object Should not build due to missing required attribute " + support);
                }
            }
        }
    }

    private void shouldHandleRequiredAndDefaults() {
        final var information = getInstantiator().getRuntimeProperties();

        final var required = information.getRequiredAsPropertySupport(true);
        final var instance = getInstantiator().newInstance(required, false);

        for (final PropertySupport support : required) {
            if (support.isReadable()) {
                support.assertValueSet(instance);
            }
        }

        for (final PropertySupport support : information.getDefaultAsPropertySupport(false)) {
            if (support.isReadable()) {
                support.assertDefaultValue(instance);
            }
        }

        for (final PropertySupport support : information.getAdditionalAsPropertySupport(false)) {
            if (support.isReadable() && !support.isDefaultValue()) {
                support.assertValueSet(instance);
            }
        }

    }

    private void shouldPersistAllParameter() {
        final var properties = instantiator.getRuntimeProperties().getAllAsPropertySupport(true);

        final var instance = instantiator.newInstance(properties, false);
        for (final PropertySupport support : properties) {
            if (support.isReadable()) {
                support.assertValueSet(instance);
            }
        }
    }

    /**
     * Factory method for creating a {@link List} of instances of
     * {@link ObjectCreatorContractImpl} depending on the given parameter
     *
     * @param beanType                identifying the type to be tested. Must not be
     *                                null
     * @param annotated               the annotated unit-test-class. It is expected
     *                                to be annotated with {@link VerifyConstructor}
     *                                and / or {@link VerifyConstructors},
     *                                {@link VerifyFactoryMethod} and / or
     *                                {@link VerifyFactoryMethods} otherwise the
     *                                method will return empty list
     * @param initialPropertyMetadata identifying the complete set of
     *                                {@link PropertyMetadata}, where the actual
     *                                {@link PropertyMetadata} for the test will be
     *                                filtered by using the attributes defined
     *                                within {@link VerifyConstructor} and / or
     *                                {@link VerifyFactoryMethod}. Must not be null.
     * @return a {@link List} of instances of {@link ObjectCreatorContractImpl} in
     *         case all requirements for the parameters are correct, otherwise it
     *         will return an empty list
     */
    public static final <T> List<ObjectCreatorContractImpl<T>> createTestContracts(final Class<T> beanType,
            final Class<?> annotated, final List<PropertyMetadata> initialPropertyMetadata) {

        requireNonNull(beanType, "beantype must not be null");
        requireNonNull(initialPropertyMetadata, "initialPropertyMetadata must not be null");

        final var builder = new CollectionBuilder<ObjectCreatorContractImpl<T>>();
        // VerifyConstructor
        for (final VerifyConstructor contract : AnnotationHelper.extractConfiguredConstructorContracts(annotated)) {
            final var properties = AnnotationHelper.constructorConfigToPropertyMetadata(contract,
                    initialPropertyMetadata);
            final ParameterizedInstantiator<T> instantiator = new ConstructorBasedInstantiator<>(beanType,
                    new RuntimeProperties(properties));
            builder.add(new ObjectCreatorContractImpl<>(instantiator));
        }
        // Verify Factory method
        for (final VerifyFactoryMethod contract : AnnotationHelper.extractConfiguredFactoryContracts(annotated)) {
            final var properties = AnnotationHelper.factoryConfigToPropertyMetadata(contract, initialPropertyMetadata);
            Class<?> enclosingType = beanType;
            if (!VerifyFactoryMethod.class.equals(contract.enclosingType())) {
                enclosingType = contract.enclosingType();
            }
            final ParameterizedInstantiator<T> instantiator = new FactoryBasedInstantiator<>(beanType,
                    new RuntimeProperties(properties), enclosingType, contract.factoryMethodName());
            builder.add(new ObjectCreatorContractImpl<>(instantiator));
        }
        return builder.toImmutableList();
    }

}
