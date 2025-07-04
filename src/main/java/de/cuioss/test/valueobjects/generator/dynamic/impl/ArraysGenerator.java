/**
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.generator.dynamic.impl;

import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.generator.impl.CollectionGenerator;
import de.cuioss.test.generator.impl.PrimitiveArrayGenerators;
import de.cuioss.test.valueobjects.generator.dynamic.GeneratorResolver;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Optional;

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
        final var collection = (List<C>) collectionGenerator.list();
        if (!componentType.isPrimitive()) {
            final var array = (C[]) Array.newInstance(componentType, collection.size());
            return (T) collection.toArray(array);
        }
        return (T) PrimitiveArrayGenerators.resolveForType(componentType).next();
    }

    /**
     * Factory method for creating an instance of {@link ArraysGenerator}.
     *
     * @param type to be, must not be an array type: {@link Class#isArray()}
     * @return an {@link Optional} on the corresponding {@link ArraysGenerator} if
     *         the requirements are met, {@link Optional#empty()} otherwise
     */
    public static final <T> Optional<TypedGenerator<T>> getGeneratorForType(final Class<T> type) {
        if (null == type || !type.isArray()) {
            return Optional.empty();
        }
        final Class<?> componentType = type.getComponentType();

        final TypedGenerator<?> wrappedGenerator = GeneratorResolver.resolveGenerator(componentType);

        return Optional.of(new ArraysGenerator<>(type, componentType, new CollectionGenerator<>(wrappedGenerator)));
    }
}
