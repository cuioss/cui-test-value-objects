package de.cuioss.test.valueobjects.objects.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.valueobjects.objects.BuilderInstantiator;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl.PropertyMetadataBuilder;

class BuilderFactoryBasedInstantiatorTest {

    private final BuilderInstantiator<PropertyMetadataImpl> instantiator =
        new BuilderFactoryBasedInstantiator<>(PropertyMetadataImpl.class);

    @Test
    void shouldInstantiatePropertyRuntimeGeneratorBuilder() {
        final var builder = (PropertyMetadataBuilder) instantiator.newBuilderInstance();
        assertNotNull(builder);
        assertEquals(PropertyMetadataImpl.class, instantiator.getTargetClass());
        assertEquals(PropertyMetadataBuilder.class, instantiator.getBuilderClass());
    }

    @Test
    void shouldFailOnInvalidMethodNames() {
        assertThrows(AssertionError.class,
                () -> new BuilderFactoryBasedInstantiator<PropertyMetadataImpl>(PropertyMetadataImpl.class, "notThere",
                        "notThere"));
    }

    @Test
    void shouldFailToBuildWithInsufficientProperties() {
        final var builder = (PropertyMetadataBuilder) instantiator.newBuilderInstance();
        assertThrows(AssertionError.class, () -> instantiator.build(builder));
    }

    @Test
    void shouldBuildWithMinimalProperties() {
        final var builder = (PropertyMetadataBuilder) instantiator.newBuilderInstance();
        builder.name("name").generator(Generators.letterStrings());
        final PropertyMetadata built = instantiator.build(builder);
        assertNotNull(built);
        assertEquals("name", built.getName());
        assertEquals(String.class, built.getPropertyClass());
    }
}
