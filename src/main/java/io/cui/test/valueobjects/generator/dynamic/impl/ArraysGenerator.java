package io.cui.test.valueobjects.generator.dynamic.impl;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Optional;

import io.cui.test.generator.TypedGenerator;
import io.cui.test.generator.impl.CollectionGenerator;
import io.cui.test.generator.impl.PrimitiveArrayGenerators;
import io.cui.test.valueobjects.generator.dynamic.GeneratorResolver;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Generator for different types of arrays.
 *
 * @author Oliver Wolff
 * @param <T> identifying the concrete Array-type
 * @param <C> identifying the componentType of the array
 */
@RequiredArgsConstructor
public class ArraysGenerator<T, C> implements TypedGenerator<T> {

    @NonNull
    @Getter
    private final Class<T> type;

    @NonNull
    private final Class<C> componentType;

    @NonNull
    private final CollectionGenerator<?> collectionGenerator;

    @SuppressWarnings("unchecked")
    @Override
    public T next() {
        final var collection = (List<C>) this.collectionGenerator.list();
        if (!componentType.isPrimitive()) {
            final var array = (C[]) Array.newInstance(this.componentType, collection.size());
            return (T) collection.toArray(array);
        }
        return (T) PrimitiveArrayGenerators.resolveForType(componentType).next();
    }

    /**
     * Factory method for creating an instance of {@link ArraysGenerator}.
     *
     * @param type to be, must not be an array type: {@link Class#isArray()}
     * @return an {@link Optional} on the corresponding {@link ArraysGenerator} if the requirements
     *         are met, {@link Optional#empty()} otherwise
     */
    public static final <T> Optional<TypedGenerator<T>> getGeneratorForType(
            final Class<T> type) {
        if (null == type || !type.isArray()) {
            return Optional.empty();
        }
        final Class<?> componentType = type.getComponentType();

        final TypedGenerator<?> wrappedGenerator = GeneratorResolver.resolveGenerator(componentType);

        return Optional
                .of(new ArraysGenerator<>(type, componentType,
                        new CollectionGenerator<>(wrappedGenerator)));
    }
}
