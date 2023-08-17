package de.cuioss.test.valueobjects.testbeans.generator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.cuioss.test.valueobjects.api.generator.PropertyGeneratorHint;

@SuppressWarnings("javadoc")
@PropertyGeneratorHint(declaredType = Serializable.class, implementationType = Integer.class)
@PropertyGeneratorHint(declaredType = List.class, implementationType = ArrayList.class)
public class GeneratorHintMultipleAnnotations {

}
