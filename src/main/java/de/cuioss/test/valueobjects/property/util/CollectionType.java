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
package de.cuioss.test.valueobjects.property.util;

import de.cuioss.test.generator.impl.CollectionGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

import static de.cuioss.tools.collect.CollectionLiterals.*;

/**
 * Used for static intersection of collection-types.
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("squid:S1452") // owolff: No type information available at this level,
// therefore the wildcard is needed
public enum CollectionType {

    /** Represents a {@link Collection}. The implementation will return a list. */
    COLLECTION(Collection.class) {
        @Override
        public Iterable<?> nextIterable(final CollectionGenerator<?> collectionGenerator) {
            return collectionGenerator.list();
        }

        @Override
        public Iterable<?> wrapToIterable(final Iterable<?> iterable) {
            return mutableList(iterable);
        }

        @Override
        public Iterable<?> emptyCollection() {
            return immutableList();
        }

    },
    /** Represents a {@link List}. */
    LIST(List.class) {
        @Override
        public Iterable<?> nextIterable(final CollectionGenerator<?> collectionGenerator) {
            return collectionGenerator.list();
        }

        @Override
        public Iterable<?> wrapToIterable(final Iterable<?> iterable) {
            return mutableList(iterable);
        }

        @Override
        public Iterable<?> emptyCollection() {
            return immutableList();
        }

    },
    /** Represents a {@link Set}. */
    SET(Set.class) {
        @Override
        public Iterable<?> nextIterable(final CollectionGenerator<?> collectionGenerator) {
            return collectionGenerator.set();
        }

        @Override
        public Iterable<?> wrapToIterable(final Iterable<?> iterable) {
            return mutableSet(iterable);
        }

        @Override
        public Iterable<?> emptyCollection() {
            return immutableSet();
        }

    },
    /** Represents a {@link SortedSet}. */
    SORTED_SET(SortedSet.class) {
        @Override
        public Iterable<?> nextIterable(final CollectionGenerator<?> collectionGenerator) {
            return collectionGenerator.sortedSet();
        }

        @Override
        public Iterable<?> wrapToIterable(final Iterable<?> iterable) {
            return immutableSortedSet(iterable);
        }

        @Override
        public Iterable<?> emptyCollection() {
            return immutableSortedSet();
        }

    },
    /**
     * 'Marker' type for arrays. The operations will always throw an
     * {@link UnsupportedOperationException}. It is designed as a marker and can not
     * be used directly
     */
    ARRAY_MARKER(Iterable.class) {
        @Override
        public Iterable<?> nextIterable(final CollectionGenerator<?> collectionGenerator) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Iterable<?> wrapToIterable(final Iterable<?> iterable) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Iterable<?> emptyCollection() {
            throw new UnsupportedOperationException();
        }

    },
    /**
     * 'null' type for this enum. The operations will always throw an
     * {@link UnsupportedOperationException}
     */
    NO_ITERABLE(Iterable.class) {
        @Override
        public Iterable<?> nextIterable(final CollectionGenerator<?> collectionGenerator) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Iterable<?> wrapToIterable(final Iterable<?> iterable) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Iterable<?> emptyCollection() {
            throw new UnsupportedOperationException();
        }

    };

    @SuppressWarnings("rawtypes")
    @Getter
    private final Class<? extends Iterable> iterableType;

    /**
     * Will create a corresponding type out of the given Generator
     *
     * @param collectionGenerator to be used, must not be null
     * @return a type-corresponding iterable with random size between 4 and 12
     */
    public abstract Iterable<?> nextIterable(CollectionGenerator<?> collectionGenerator);

    /**
     * Will wrap the given iterable in to the corresponding collection-type
     *
     * @param iterable to be used, must not be null
     * @return a type-corresponding iterable
     */
    public abstract Iterable<?> wrapToIterable(Iterable<?> iterable);

    /**
     * Returns an empty immutable instance of the corresponding collection-type
     *
     * @return a type-corresponding iterable
     */
    public abstract Iterable<?> emptyCollection();

    /**
     * Finds a concrete implementation for a given <em>Interface</em> that is to at
     * least {@link Collection}. A special case is the dealing with Arrays
     *
     * @param type to be checked
     * @return {@link Optional} of the type
     */
    public static final Optional<CollectionType> findResponsibleCollectionType(final Class<?> type) {
        if (null == type || type.isEnum()) {
            return Optional.empty();
        }
        if (type.isArray()) {
            return Optional.of(ARRAY_MARKER);
        }

        if (!type.isInterface() || !Iterable.class.isAssignableFrom(type)) {
            return Optional.empty();
        }
        for (final CollectionType collectionType : SEARCH_ORDER) {
            if (collectionType.iterableType.isAssignableFrom(type)) {
                return Optional.of(collectionType);
            }
        }
        return Optional.empty();
    }

    private static final List<CollectionType> SEARCH_ORDER = immutableList(SORTED_SET, SET, LIST, COLLECTION);
}
