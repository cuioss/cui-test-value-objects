package io.cui.test.valueobjects.testbeans.generator;

import java.io.Serializable;

import io.cui.test.valueobjects.api.generator.PropertyGeneratorHint;

@SuppressWarnings("javadoc")
@PropertyGeneratorHint(declaredType = Serializable.class,
        implementationType = Integer.class)
public class GeneratorHintSingleAnnotation {

}
