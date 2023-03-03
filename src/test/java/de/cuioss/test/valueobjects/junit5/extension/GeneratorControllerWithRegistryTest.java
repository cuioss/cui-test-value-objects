package de.cuioss.test.valueobjects.junit5.extension;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.util.GeneratorRegistry;
import de.cuioss.tools.property.PropertyReadWrite;

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
