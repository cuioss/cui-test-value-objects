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

import java.util.List;
import java.util.Optional;

import de.cuioss.test.valueobjects.api.TestContract;
import de.cuioss.test.valueobjects.api.contracts.VerifyBeanProperty;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.impl.BeanInstantiator;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.PropertySupport;
import de.cuioss.test.valueobjects.util.AnnotationHelper;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.property.PropertyReadWrite;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Tests all given properties according to the given List of
 * {@link PropertyMetadata}
 *
 * @author Oliver Wolff
 * @param <T> Rule does not apply to annotations: There is no inheritance
 */
@RequiredArgsConstructor
public class BeanPropertyContractImpl<T> implements TestContract<T> {

    private static final CuiLogger log = new CuiLogger(BeanPropertyContractImpl.class);

    @Getter
    @NonNull
    private final ParameterizedInstantiator<T> instantiator;

    @Override
    public void assertContract() {
        final var builder = new StringBuilder("Verifying ");
        builder.append(getClass().getName()).append("\nWith configuration: ").append(getInstantiator().toString());
        log.info(builder.toString());

        checkGetterAndSetterContract();
        checkDefaultContract();
    }

    private void checkGetterAndSetterContract() {
        final var readWriteProperties = getInstantiator().getRuntimeProperties().getAllProperties().stream()
                .filter(p -> PropertyReadWrite.READ_WRITE.equals(p.getPropertyReadWrite())).toList();

        if (readWriteProperties.isEmpty()) {
            log.warn(
                    "There are no properties defined that are readable and writable. Consider your configuration and/or the base class for your test.");
        } else {
            log.info("Verifying properties that are Read and Write: "
                    + RuntimeProperties.extractNames(readWriteProperties));

            final var supportList = readWriteProperties.stream().map(PropertySupport::new).toList();
            final Object target = getInstantiator().newInstanceMinimal();
            for (final PropertySupport support : supportList) {

                support.generateTestValue();

                support.apply(target);

                support.assertValueSet(target);
            }
        }
    }

    private void checkDefaultContract() {
        final var defaultProperties = getInstantiator().getRuntimeProperties().getDefaultProperties();
        if (defaultProperties.isEmpty()) {
            log.debug("No default properties configured");
        } else {
            final var defaultPropertySupport = defaultProperties.stream().map(PropertySupport::new).toList();
            final Object target = getInstantiator().newInstanceMinimal();
            for (final PropertySupport support : defaultPropertySupport) {
                support.assertDefaultValue(target);
            }
        }
    }

    /**
     * Factory method for creating an instance of {@link BeanPropertyContractImpl}
     * depending on the given parameter
     *
     * @param beanType                identifying the type to be tested. Must not be
     *                                null and must provide a no args public
     *                                constructor
     * @param annotated               the annotated unit-test-class. It is expected
     *                                to be annotated with
     *                                {@link BeanPropertyContractImpl}, otherwise
     *                                the method will return
     *                                {@link Optional#empty()}
     * @param initialPropertyMetadata identifying the complete set of
     *                                {@link PropertyMetadata}, where the actual
     *                                {@link PropertyMetadata} for the bean tests
     *                                will be filtered by using the attributes
     *                                defined within
     *                                {@link BeanPropertyContractImpl}. Must not be
     *                                null. If it is empty the method will return
     *                                {@link Optional#empty()}
     * @return an instance Of {@link BeanPropertyContractImpl} in case all
     *         requirements for the parameters are correct, otherwise it will return
     *         {@link Optional#empty()}
     */
    public static final <T> Optional<TestContract<T>> createBeanPropertyTestContract(final Class<T> beanType,
            final Class<?> annotated, final List<PropertyMetadata> initialPropertyMetadata) {

        requireNonNull(beanType, "beantype must not be null");
        requireNonNull(annotated, "annotated must not be null");
        requireNonNull(initialPropertyMetadata, "initialPropertyMetadata must not be null");

        if (!annotated.isAnnotationPresent(VerifyBeanProperty.class)) {
            log.debug("No annotation of type BeanPropertyTestContract available on class: " + annotated);
            return Optional.empty();
        }
        if (initialPropertyMetadata.isEmpty()) {
            log.warn("No configured properties found to be tested, offending class: " + annotated);
            return Optional.empty();
        }
        final var instantiator = new DefaultInstantiator<>(beanType);
        final var metadata = AnnotationHelper.handleMetadataForPropertyTest(annotated, initialPropertyMetadata);

        return Optional.of(
                new BeanPropertyContractImpl<>(new BeanInstantiator<>(instantiator, new RuntimeProperties(metadata))));
    }
}
