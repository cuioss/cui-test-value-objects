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

import de.cuioss.test.valueobjects.api.TestContract;
import de.cuioss.test.valueobjects.api.contracts.VerifyConstructor;
import de.cuioss.test.valueobjects.api.contracts.VerifyCopyConstructor;
import de.cuioss.test.valueobjects.api.contracts.VerifyFactoryMethod;
import de.cuioss.test.valueobjects.generator.impl.DummyGenerator;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.impl.ConstructorBasedInstantiator;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.PropertySupport;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import de.cuioss.test.valueobjects.util.DeepCopyTestHelper;
import de.cuioss.test.valueobjects.util.PropertyHelper;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.reflect.MoreReflection;
import de.cuioss.tools.string.Joiner;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.*;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * TestContract for dealing Constructor and factories, {@link VerifyConstructor}
 * and {@link VerifyFactoryMethod} respectively
 *
 * @author Oliver Wolff
 * @param <T> identifying the of objects to be tested.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CopyConstructorContractImpl<T> implements TestContract<T> {

    private static final CuiLogger LOGGER = new CuiLogger(CopyConstructorContractImpl.class);

    private static final String PROPERTY_NAME_COPY_FROM = "copyFrom";

    /** This instantiator represents the Copy-Constructor. */
    private final ParameterizedInstantiator<T> copyInstantiator;

    /** represents the underlying Instantiator. */
    @NonNull
    @Getter
    private final ParameterizedInstantiator<T> instantiator;

    private final Set<String> consideredAttributes;
    private final boolean useObjectEquals;

    private final boolean verifyDeepCopy;
    private final Collection<String> verifyDeepCopyIgnore;

    @Override
    public void assertContract() {
        var builder = new StringBuilder("Verifying ");
        builder.append(getClass().getName()).append("\nWith instantiator: ").append(copyInstantiator.toString())
            .append("\nWith sourceInstantiator: ").append(instantiator);
        LOGGER.info(builder.toString());

        final var sourceAttributeNames = RuntimeProperties
            .extractNames(instantiator.getRuntimeProperties().getAllProperties());

        final Set<String> compareAttributes = new HashSet<>(consideredAttributes);

        if (!sourceAttributeNames.containsAll(consideredAttributes)) {
            builder = new StringBuilder("Not all attributes can be checked at field level:");
            builder.append("\nSource attributes are: ").append(sourceAttributeNames);
            builder.append("\nCompare attributes are: ").append(consideredAttributes);
            LOGGER.warn(builder.toString());
            compareAttributes.retainAll(sourceAttributeNames);
        }
        LOGGER.info("Attributes being compared at field level are: " + Joiner.on(", ").join(compareAttributes));

        assertCopyConstructor(compareAttributes);
        assertDeepCopy();
    }

    private void assertDeepCopy() {
        if (!verifyDeepCopy) {
            LOGGER.debug("Not checking deep-copy, disabled by configuration");
            return;
        }
        LOGGER.info("Verifying deep-copy, ignoring properties: {}" + verifyDeepCopyIgnore);

        final var all = instantiator.getRuntimeProperties().getAllAsPropertySupport(true);

        final var copyAttribute = copyInstantiator.getRuntimeProperties().getAllAsPropertySupport(false).getFirst();
        copyAttribute.setGeneratedValue(instantiator.newInstance(all, false));
        var original = copyAttribute.getGeneratedValue();
        Object copy = copyInstantiator.newInstance(immutableList(copyAttribute), false);
        DeepCopyTestHelper.verifyDeepCopy(original, copy, verifyDeepCopyIgnore);
    }

    private void assertCopyConstructor(final Set<String> compareAttributes) {

        final var all = instantiator.getRuntimeProperties().getAllAsPropertySupport(true);

        final var copyAttribute = copyInstantiator.getRuntimeProperties().getAllAsPropertySupport(false).getFirst();
        copyAttribute.setGeneratedValue(instantiator.newInstance(all, false));

        final var copy = copyInstantiator.newInstance(immutableList(copyAttribute), false);

        final var fieldLevelCheck = all.stream().filter(p -> compareAttributes.contains(p.getName())).toList();

        for (final PropertySupport field : fieldLevelCheck) {
            if (field.isReadable()) {
                field.assertValueSet(copy);
            }
        }
        if (useObjectEquals) {
            assertEquals(copyAttribute.getGeneratedValue(), copy);
        }
    }

    /**
     * Factory method for creating a an {@link CopyConstructorContractImpl}
     * depending on the given parameter
     *
     * @param beanType                identifying the type to be tested. Must not be
     *                                null
     * @param annotated               the annotated unit-test-class. It is expected
     *                                to be annotated with
     *                                {@link VerifyCopyConstructor} method will
     *                                return {@link Optional#empty()}
     * @param initialPropertyMetadata identifying the complete set of
     *                                {@link PropertyMetadata}, where the actual
     *                                {@link PropertyMetadata} for the test will be
     *                                filtered by using the attributes defined
     *                                within {@link VerifyCopyConstructor}. Must not
     *                                be null.
     * @param existingContracts       identifying the already configured contracts.
     *                                Must not be null nor empty.
     * @return an {@link Optional} of {@link CopyConstructorContractImpl} in case
     *         all requirements for the parameters are correct, otherwise it will
     *         return {@link Optional#empty()}
     */
    public static final <T> Optional<CopyConstructorContractImpl<T>> createTestContract(final Class<T> beanType,
        final Class<?> annotated, final List<PropertyMetadata> initialPropertyMetadata,
        final List<TestContract<T>> existingContracts) {

        requireNonNull(annotated, "annotated must not be null");

        final Optional<VerifyCopyConstructor> configOption = MoreReflection.extractAnnotation(annotated,
            VerifyCopyConstructor.class);

        if (configOption.isEmpty()) {
            return Optional.empty();
        }
        requireNonNull(beanType, "beantype must not be null");
        requireNonNull(initialPropertyMetadata, "initialPropertyMetadata must not be null");
        requireNonNull(existingContracts, "existingContracts must not be null");
        assertFalse(existingContracts.isEmpty(), "There must be at least one VerifyContract defined");

        final var config = configOption.get();

        final var filtered = PropertyHelper.handleWhiteAndBlacklistAsList(config.of(), config.exclude(),
            initialPropertyMetadata);
        final var filteredNames = RuntimeProperties.extractNames(filtered);

        final ParameterizedInstantiator<T> sourceInstantiator = findFittingInstantiator(existingContracts,
            filteredNames);

        final ParameterizedInstantiator<T> copyInstantiator = createCopyInstantiator(config, beanType);

        return Optional.of(new CopyConstructorContractImpl<>(copyInstantiator, sourceInstantiator, filteredNames,
            config.useObjectEquals(), config.verifyDeepCopy(), Arrays.asList(config.verifyDeepCopyIgnore())));
    }

    private static <T> ParameterizedInstantiator<T> createCopyInstantiator(final VerifyCopyConstructor config,
        final Class<T> beanType) {
        Class<?> target = beanType;
        if (!VerifyCopyConstructor.class.equals(config.argumentType())) {
            target = config.argumentType();
        }
        final var meta = PropertyMetadataImpl.builder().name(PROPERTY_NAME_COPY_FROM)
            .generator(new DummyGenerator<>(target)).propertyClass(target).build();

        return new ConstructorBasedInstantiator<>(beanType, new RuntimeProperties(immutableList(meta)));
    }

    static <T> ParameterizedInstantiator<T> findFittingInstantiator(final List<TestContract<T>> existingContracts,
        final Set<String> filteredNames) {
        for (final TestContract<T> contract : existingContracts) {
            final var contractNames = RuntimeProperties
                .extractNames(contract.getInstantiator().getRuntimeProperties().getAllProperties());
            if (contractNames.containsAll(filteredNames)) {
                return contract.getInstantiator();
            }
        }
        LOGGER.warn("No fitting ParameterizedInstantiator found, using best-fit");
        return existingContracts.getFirst().getInstantiator();
    }

}
