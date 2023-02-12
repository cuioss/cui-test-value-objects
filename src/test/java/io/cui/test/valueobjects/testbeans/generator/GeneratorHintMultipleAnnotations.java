package io.cui.test.valueobjects.testbeans.generator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.cui.test.valueobjects.api.generator.PropertyGeneratorHint;

@SuppressWarnings("javadoc")
@PropertyGeneratorHint(declaredType = Serializable.class,
        implementationType = Integer.class)
@PropertyGeneratorHint(declaredType = List.class,
        implementationType = ArrayList.class)
public class GeneratorHintMultipleAnnotations {

}
