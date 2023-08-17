package de.cuioss.test.valueobjects.generator.impl;

import de.cuioss.test.generator.TypedGenerator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Dummy variant of {@link TypedGenerator} that are used in some corner cases,
 * where you need a {@link TypedGenerator} for a contract, but do not need the
 * generated values.
 *
 * @author Oliver Wolff
 * @param <T> identifying the type of objects to be generated
 */
@RequiredArgsConstructor
public class DummyGenerator<T> implements TypedGenerator<T> {

    @Getter
    private final Class<T> type;

    @Override
    public T next() {
        return null;
    }

}
