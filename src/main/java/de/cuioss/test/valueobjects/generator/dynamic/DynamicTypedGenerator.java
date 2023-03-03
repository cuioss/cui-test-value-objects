package de.cuioss.test.valueobjects.generator.dynamic;

import de.cuioss.test.generator.TypedGenerator;
import lombok.Getter;

/**
 * This generator acts always dynamically
 *
 * @author Oliver Wolff
 * @param <T> the type of objects to be generated
 */
public class DynamicTypedGenerator<T> implements TypedGenerator<T> {

    @Getter
    private final Class<T> type;

    private final TypedGenerator<T> generator;

    /**
     * @param type must not be null
     */
    public DynamicTypedGenerator(final Class<T> type) {
        this.type = type;

        generator = GeneratorResolver.resolveGenerator(type);
    }

    @Override
    public T next() {
        return generator.next();
    }

}
