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
package de.cuioss.test.valueobjects.generator;

import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.valueobjects.generator.dynamic.impl.CollectionTypeGenerator;
import de.cuioss.test.valueobjects.property.util.CollectionType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

/**
 * Registry for instances of {@link TypedGenerator}
 *
 * @author Oliver Wolff
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TypedGeneratorRegistry {

    private static final String GENERATOR_MUST_NOT_BE_NULL = "generator must not be null";
    private static final String TYPE_MUST_NOT_BE_NULL = "type must not be null";
    private static final Map<Class<?>, TypedGenerator<?>> REGISTRY = new ConcurrentHashMap<>();

    /**
     * Checks whether the registry already contains a {@link TypedGenerator} for the
     * given type
     *
     * @param type identifying the generator, must not be null
     * @return boolean indicating whether the registry already contains a
     *         {@link TypedGenerator} for the given type
     */
    public static boolean containsGenerator(final Class<?> type) {
        requireNonNull(type, TYPE_MUST_NOT_BE_NULL);
        return REGISTRY.containsKey(type);
    }

    /**
     * @param type identifying the generator, must not be null
     * @return the registered {@link TypedGenerator} for the given type or
     *         {@link Optional#empty()} if none could be found. Consider checking
     *         first: {@link #containsGenerator(Class)}
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<TypedGenerator<T>> getGenerator(final Class<T> type) {
        requireNonNull(type, TYPE_MUST_NOT_BE_NULL);
        if (!REGISTRY.containsKey(type)) {
            return Optional.empty();
        }
        return Optional.of((TypedGenerator<T>) REGISTRY.get(type));
    }

    /**
     * @param typedGenerator to be added to the registry, must not be null.
     */
    public static <T> void registerGenerator(final TypedGenerator<T> typedGenerator) {
        requireNonNull(typedGenerator, GENERATOR_MUST_NOT_BE_NULL);
        REGISTRY.put(typedGenerator.getType(), typedGenerator);
    }

    /**
     * @param type      identifying the generator, must not be null
     * @param generator to be set, must not be null
     */
    public static void registerTypedGenerator(final Class<?> type, final TypedGenerator<?> generator) {
        requireNonNull(type, TYPE_MUST_NOT_BE_NULL);
        requireNonNull(generator, GENERATOR_MUST_NOT_BE_NULL);
        REGISTRY.put(type, generator);
    }

    /**
     * Removes the corresponding {@link TypedGenerator} for the given type
     *
     * @param type identifying the generator, must not be null
     */
    public static <T> void removeGenerator(final Class<T> type) {
        requireNonNull(type, TYPE_MUST_NOT_BE_NULL);
        REGISTRY.remove(type);
    }

    /**
     * Clears the registry.
     */
    public static void clear() {
        REGISTRY.clear();
    }

    /**
     * Add all basic types provided by {@link JavaTypesGenerator}. In addition it
     * adds {@link TypedGenerator} for basic Collection-types returning an empty
     * collection.
     */
    public static void registerBasicTypes() {
        JavaTypesGenerator.allGenerators().forEach(g -> REGISTRY.put(g.getType(), g));
        REGISTRY.put(Collection.class, new CollectionTypeGenerator<>(Collection.class, CollectionType.COLLECTION));
        REGISTRY.put(List.class, new CollectionTypeGenerator<>(List.class, CollectionType.LIST));
        REGISTRY.put(Set.class, new CollectionTypeGenerator<>(Set.class, CollectionType.SET));
        REGISTRY.put(SortedSet.class, new CollectionTypeGenerator<>(SortedSet.class, CollectionType.SORTED_SET));
    }
}
