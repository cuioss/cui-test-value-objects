package de.cuioss.test.valueobjects.testbeans.generator;

import java.io.Serializable;

import de.cuioss.test.valueobjects.api.generator.PropertyGeneratorHint;

@SuppressWarnings("javadoc")
@PropertyGeneratorHint(declaredType = Serializable.class, implementationType = Integer.class)
public class GeneratorHintSingleAnnotation {

}
