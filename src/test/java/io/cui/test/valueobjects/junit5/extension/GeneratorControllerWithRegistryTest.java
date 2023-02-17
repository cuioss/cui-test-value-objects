package io.cui.test.valueobjects.junit5.extension;

import static io.cui.tools.collect.CollectionLiterals.immutableList;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.cui.test.generator.Generators;
import io.cui.test.generator.TypedGenerator;
import io.cui.test.valueobjects.generator.TypedGeneratorRegistry;
import io.cui.test.valueobjects.util.GeneratorRegistry;
import io.cui.tools.property.PropertyReadWrite;

@ExtendWith(GeneratorRegistryController.class)
class GeneratorControllerWithRegistryTest implements GeneratorRegistry {

    @Test
    void registryShouldProvideBasicTypes() {
        assertTrue(TypedGeneratorRegistry.containsGenerator(String.class));
    }

    @Test
    void registryShouldProvideImplementationType() {
        assertTrue(TypedGeneratorRegistry.containsGenerator(PropertyReadWrite.class));
    }

    @Override
    public List<TypedGenerator<?>> registerAdditionalGenerators() {
        return immutableList(Generators.enumValues(PropertyReadWrite.class));
    }

}
