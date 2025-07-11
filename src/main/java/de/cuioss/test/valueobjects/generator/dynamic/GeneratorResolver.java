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
package de.cuioss.test.valueobjects.generator.dynamic;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.generator.dynamic.impl.*;
import de.cuioss.test.valueobjects.property.util.CollectionType;
import de.cuioss.tools.logging.CuiLogger;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Provides strategies for creating objects dynamically
 *
 * @author Oliver Wolff
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GeneratorResolver {

    private static final String FOUND_GENERATOR_FOR_TYPE = "Found generator {} for type {}";

    private static final String TYPE_MUST_NOT_BE_NULL = "type must not be null";

    private static final CuiLogger log = new CuiLogger(GeneratorResolver.class);

    /**
     * Central method for finding / accessing a concrete {@link TypedGenerator} for
     * the given type. It works through all existing find methods. as last resort is
     * uses {@link InterfaceProxyGenerator} or {@link DynamicProxyGenerator} that
     * will always return a valid one <em>Caution:</em> The resolving system relies
     * on {@link TypedGeneratorRegistry} being configured properly, saying
     * {@link TypedGeneratorRegistry#registerBasicTypes()} has been called prior to
     * this method
     *
     * @param type must not be null
     * @return a concrete {@link TypedGenerator} for the given type
     */
    public static <T> TypedGenerator<T> resolveGenerator(final Class<T> type) {
        requireNonNull(type, TYPE_MUST_NOT_BE_NULL);
        log.debug("resolving generator for {}", type.getName());

        Optional<TypedGenerator<T>> found = TypedGeneratorRegistry.getGenerator(type);
        if (found.isPresent()) {
            log.trace(FOUND_GENERATOR_FOR_TYPE, found.get().getClass().getName(), type.getName());
            return found.get();
        }
        found = Generators.enumValuesIfAvailable(type);
        if (found.isPresent()) {
            TypedGeneratorRegistry.registerGenerator(found.get());
            log.trace(FOUND_GENERATOR_FOR_TYPE, found.get().getClass().getName(), type.getName());
            return found.get();
        }
        found = ArraysGenerator.getGeneratorForType(type);
        if (found.isPresent()) {
            TypedGeneratorRegistry.registerGenerator(found.get());
            log.trace(FOUND_GENERATOR_FOR_TYPE, found.get().getClass().getName(), type.getName());
            return found.get();
        }
        found = resolveCollectionGenerator(type);
        if (found.isPresent()) {
            TypedGeneratorRegistry.registerGenerator(found.get());
            log.trace(FOUND_GENERATOR_FOR_TYPE, found.get().getClass().getName(), type.getName());
            return found.get();
        }
        found = ConstructorBasedGenerator.getGeneratorForType(type);
        if (found.isPresent()) {
            TypedGeneratorRegistry.registerGenerator(found.get());
            log.trace(FOUND_GENERATOR_FOR_TYPE, found.get().getClass().getName(), type.getName());
            return found.get();
        }
        return resolveProxyGenerator(type);
    }

    private static <T> TypedGenerator<T> resolveProxyGenerator(final Class<T> type) {
        log.debug("resolveProxyGenerator for type {}", type.getName());
        Optional<TypedGenerator<T>> found = InterfaceProxyGenerator.getGeneratorForType(type);
        if (found.isPresent()) {
            TypedGeneratorRegistry.registerGenerator(found.get());
            log.trace(FOUND_GENERATOR_FOR_TYPE, found.get().getClass().getName(), type.getName());
            return found.get();
        }
        found = DynamicProxyGenerator.getGeneratorForType(type);
        if (found.isPresent()) {
            TypedGeneratorRegistry.registerGenerator(found.get());
            log.trace(FOUND_GENERATOR_FOR_TYPE, found.get().getClass().getName(), type.getName());
            return found.get();
        }
        throw new IllegalArgumentException("Unable to determine generator for type=" + type);
    }

    /**
     * Provides a {@link TypedGenerator} for generating empty {@link Iterable} /
     * {@link Collection} or {@link Map}s for given interfaces
     *
     * @param type to be checked
     * @return an {@link TypedGenerator} if applicable or or <code>not
     *         {@link Optional#isPresent()}</code>
     */
    @SuppressWarnings("unchecked") // Checked beforehand
    public static <T> Optional<TypedGenerator<T>> resolveCollectionGenerator(final Class<T> type) {
        if (null == type || !type.isInterface()) {
            return Optional.empty();
        }
        final var optional = CollectionType.findResponsibleCollectionType(type);
        if (optional.isPresent()) {
            return Optional.of(new CollectionTypeGenerator<>(type, optional.get()));
        }
        if (Map.class.isAssignableFrom(type)) {
            return Optional.of((TypedGenerator<T>) new EmptyMapGenerator());
        }
        return Optional.empty();
    }
}
