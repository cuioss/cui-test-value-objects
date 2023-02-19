package io.cui.test.valueobjects.junit5.extension;

import static io.cui.tools.collect.CollectionLiterals.immutableList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.cui.test.generator.Generators;
import io.cui.test.generator.TypedGenerator;
import io.cui.test.valueobjects.generator.TypedGeneratorRegistry;
import io.cui.test.valueobjects.junit5.EnableGeneratorRegistry;
import io.cui.tools.property.PropertyReadWrite;

@EnableGeneratorRegistry
class GeneratorControllerWithoutRegistryTest {

    @Test
    void registryShouldProvideBasicTypes() {
        assertTrue(TypedGeneratorRegistry.containsGenerator(String.class));
    }

    @Test
    void registryShouldProvideImplementationType() {
        assertFalse(TypedGeneratorRegistry.containsGenerator(PropertyReadWrite.class));
    }

    public List<TypedGenerator<?>> registerAdditionalGenerators() {
        return immutableList(Generators.enumValues(PropertyReadWrite.class));
    }

}
