package io.cui.test.valueobjects.testbeans.generator;

import io.cui.test.generator.impl.FloatObjectGenerator;
import io.cui.test.generator.impl.LocalDateGenerator;
import io.cui.test.generator.impl.NumberGenerator;
import io.cui.test.valueobjects.api.generator.PropertyGenerator;

@SuppressWarnings("javadoc")
@PropertyGenerator(FloatObjectGenerator.class)
@PropertyGenerator({ LocalDateGenerator.class, NumberGenerator.class })
public class ClassWithMixedGenerator {

}
