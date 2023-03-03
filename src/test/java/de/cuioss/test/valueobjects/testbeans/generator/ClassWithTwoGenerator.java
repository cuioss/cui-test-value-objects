package de.cuioss.test.valueobjects.testbeans.generator;

import de.cuioss.test.generator.impl.FloatObjectGenerator;
import de.cuioss.test.generator.impl.LocalDateGenerator;
import de.cuioss.test.valueobjects.api.generator.PropertyGenerator;

@SuppressWarnings("javadoc")
@PropertyGenerator(FloatObjectGenerator.class)
@PropertyGenerator(LocalDateGenerator.class)
public class ClassWithTwoGenerator {

}
