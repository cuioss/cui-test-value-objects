package de.cuioss.test.valueobjects.generator.dynamic.impl;

import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.generator.internal.net.java.quickcheck.Generator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Hard-core implementation for reflection based corner-cases
 *
 * @author Oliver Wolff
 */
@SuppressWarnings("rawtypes")
@RequiredArgsConstructor
@ToString
public class WildcardGenerator implements TypedGenerator {

    @Getter
    private final Class<?> type;
    private final Generator<?> generator;

    @Override
    public Object next() {
        return generator.next();
    }

}
