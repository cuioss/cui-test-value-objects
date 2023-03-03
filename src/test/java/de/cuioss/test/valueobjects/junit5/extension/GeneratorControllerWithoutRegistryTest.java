package de.cuioss.test.valueobjects.junit5.extension;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.junit5.EnableGeneratorRegistry;
import de.cuioss.tools.property.PropertyReadWrite;

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
