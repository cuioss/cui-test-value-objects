package de.cuioss.test.valueobjects.objects.impl;

import static de.cuioss.tools.collect.CollectionLiterals.immutableSortedSet;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.objects.BuilderInstantiator;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.testbeans.builder.BuilderWithCollections;

class BuilderParameterizedInstantiatorTest {

    @Test
    void shouldBuildVariantsOfPropertyMetadata() {
        final var validMetadata = new RuntimeProperties(immutableSortedSet(BuilderWithCollections.METADATA_COMPLETE));
        final BuilderInstantiator<BuilderWithCollections> builderInstantiator = new BuilderFactoryBasedInstantiator<>(
                BuilderWithCollections.class);
        final ParameterizedInstantiator<BuilderWithCollections> parameterizedInstantiator = new BuilderParameterizedInstantiator<>(
                builderInstantiator, validMetadata);
        assertNotNull(parameterizedInstantiator.newInstanceMinimal());
        assertNotNull(parameterizedInstantiator.newInstanceFull());
    }
}
