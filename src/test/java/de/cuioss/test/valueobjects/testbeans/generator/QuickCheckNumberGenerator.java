package de.cuioss.test.valueobjects.testbeans.generator;

import de.cuioss.test.generator.internal.net.java.quickcheck.Generator;
import de.cuioss.test.generator.internal.net.java.quickcheck.generator.PrimitiveGenerators;

@SuppressWarnings("javadoc")
public class QuickCheckNumberGenerator implements Generator<Number> {

    @Override
    public Number next() {
        return PrimitiveGenerators.integers().next();
    }

}
