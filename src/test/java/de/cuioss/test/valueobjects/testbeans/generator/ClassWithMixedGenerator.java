package de.cuioss.test.valueobjects.testbeans.generator;

import de.cuioss.test.generator.impl.FloatObjectGenerator;
import de.cuioss.test.generator.impl.LocalDateGenerator;
import de.cuioss.test.generator.impl.NumberGenerator;
import de.cuioss.test.valueobjects.api.generator.PropertyGenerator;

@SuppressWarnings("javadoc")
@PropertyGenerator(FloatObjectGenerator.class)
@PropertyGenerator({ LocalDateGenerator.class, NumberGenerator.class })
public class ClassWithMixedGenerator {

}
