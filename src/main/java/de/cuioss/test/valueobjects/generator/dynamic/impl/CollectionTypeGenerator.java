package de.cuioss.test.valueobjects.generator.dynamic.impl;

import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.valueobjects.property.util.CollectionType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Generator for Iterable / Collection interfaces
 *
 * @author Oliver Wolff
 * @param <T> identifying the type to be generated
 */
@RequiredArgsConstructor
public class CollectionTypeGenerator<T> implements TypedGenerator<T> {

    @NonNull
    private final Class<T> type;

    @NonNull
    private final CollectionType collectionType;

    @SuppressWarnings("unchecked")
    @Override
    public T next() {
        return (T) collectionType.emptyCollection();
    }

    @Override
    public Class<T> getType() {
        return type;
    }
}
