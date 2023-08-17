package de.cuioss.test.valueobjects.generator;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;

import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.valueobjects.generator.dynamic.impl.CollectionTypeGenerator;
import de.cuioss.test.valueobjects.property.util.CollectionType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
