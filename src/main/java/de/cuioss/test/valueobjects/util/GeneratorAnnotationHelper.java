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
package de.cuioss.test.valueobjects.util;

import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.valueobjects.api.contracts.VerifyConstructor;
import de.cuioss.test.valueobjects.api.generator.PropertyGenerator;
import de.cuioss.test.valueobjects.api.generator.PropertyGeneratorHint;
import de.cuioss.test.valueobjects.api.generator.PropertyGeneratorHints;
import de.cuioss.test.valueobjects.api.generator.PropertyGenerators;
import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.generator.dynamic.GeneratorResolver;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;
import de.cuioss.tools.collect.CollectionBuilder;
import de.cuioss.tools.reflect.MoreReflection;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * Provides utility methods for dealing with the creation of
 * {@link TypedGenerator} usually in conjunction with annotations
 *
 * @author Oliver Wolff
 */
@UtilityClass
public final class GeneratorAnnotationHelper {

    /**  */
    public static final String UNABLE_TO_INSTANTIATE_GENERATOR = "Unable to instantiate generator, You must provide a no-arg public constructor: ";

    /**
     * Convenience method for handling all Generator specific aspects like initially
     * calling {@link TypedGeneratorRegistry#registerBasicTypes()} and calling of
     * the individual registering methods like
     * {@link #handleUnitClassImplementation(Object)},
     * {@link #handlePropertyGenerator(Class)} and
     * {@link #handleGeneratorHints(Class)} in case there are additionalGenerator
     * given they will be registered as well
     *
     * @param testClass           must not null
     * @param additionalGenerator
     */
    public static void handleGeneratorsForTestClass(final Object testClass,
        final List<TypedGenerator<?>> additionalGenerator) {
        requireNonNull(testClass);
        // Handle Generator
        TypedGeneratorRegistry.registerBasicTypes();
        handleUnitClassImplementation(testClass);
        handlePropertyGenerator(testClass.getClass());
        handleGeneratorHints(testClass.getClass());
        if (null != additionalGenerator) {
            for (final TypedGenerator<?> additional : additionalGenerator) {
                TypedGeneratorRegistry.registerGenerator(additional);
            }
        }
    }

    /**
     * Checks the given type for the annotation {@link PropertyGeneratorHint} and
     * {@link PropertyGeneratorHints} and registers all found to the
     * {@link TypedGeneratorRegistry}
     *
     * @param annotated the class that may or may not provide the annotations, must
     *                  not be null
     */
    public static void handleGeneratorHints(final Class<?> annotated) {
        for (final PropertyGeneratorHint hint : extractConfiguredGeneratorHints(annotated)) {

            final TypedGenerator<?> resolved = GeneratorResolver.resolveGenerator(hint.implementationType());
            TypedGeneratorRegistry.registerTypedGenerator(hint.declaredType(),
                new WildcardDecoratorGenerator(hint.declaredType(), resolved));
        }
    }

    /**
     * Checks the given type for the annotation {@link PropertyGenerator} and
     * {@link PropertyGenerators} and registers all found to the
     * {@link TypedGeneratorRegistry}
     *
     * @param annotated the class that may or may not provide the annotations, must
     *                  not be null
     */
    public static void handlePropertyGenerator(final Class<?> annotated) {
        for (final PropertyGenerator config : extractConfiguredPropertyGenerator(annotated)) {
            for (final Class<?> typedClass : config.value()) {
                TypedGeneratorRegistry
                    .registerGenerator((TypedGenerator<?>) new DefaultInstantiator<>(typedClass).newInstance());
            }
        }
    }

    /**
     * Checks whether the actual implementation of the test implements
     * {@link TypedGenerator}. If so it will be registered to the
     * {@link TypedGeneratorRegistry}
     *
     * @param testClass the actual test-object
     */
    public static void handleUnitClassImplementation(final Object testClass) {
        if (testClass instanceof TypedGenerator<?> generator) {
            TypedGeneratorRegistry.registerGenerator(generator);
        }
    }

    /**
     * Checks the given type for the annotation {@link PropertyGeneratorHint} and
     * {@link PropertyGeneratorHints} and puts all found in the returned list
     *
     * @param annotated the class that may or may not provide the annotations, must
     *                  not be null
     * @return a {@link Set} of {@link VerifyConstructor} extracted from the
     *         annotations of the given type. May be empty but never null
     */
    public static Set<PropertyGeneratorHint> extractConfiguredGeneratorHints(final Class<?> annotated) {
        requireNonNull(annotated);
        final var builder = new CollectionBuilder<PropertyGeneratorHint>();

        MoreReflection.extractAllAnnotations(annotated, PropertyGeneratorHints.class)
            .forEach(contract -> builder.add(Arrays.asList(contract.value())));
        MoreReflection.extractAllAnnotations(annotated, PropertyGeneratorHint.class).forEach(builder::add);

        return builder.toImmutableSet();
    }

    /**
     * Checks the given type for the annotation {@link PropertyGenerator} and
     * {@link PropertyGenerators} and puts all found in the returned list
     *
     * @param annotated the class that may or may not provide the annotations, must
     *                  not be null
     * @return a {@link Set} of {@link PropertyGenerator} extract from the
     *         annotations of the given type. May be empty but never null
     */
    public static Set<PropertyGenerator> extractConfiguredPropertyGenerator(final Class<?> annotated) {
        requireNonNull(annotated);
        final var builder = new CollectionBuilder<PropertyGenerator>();

        MoreReflection.extractAllAnnotations(annotated, PropertyGenerators.class)
            .forEach(contract -> builder.add(Arrays.asList(contract.value())));
        MoreReflection.extractAllAnnotations(annotated, PropertyGenerator.class).forEach(builder::add);

        return builder.toImmutableSet();
    }

}
