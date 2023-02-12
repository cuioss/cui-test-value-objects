package io.cui.test.valueobjects.objects.impl;

import static io.cui.tools.collect.CollectionLiterals.immutableSortedSet;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.objects.BuilderInstantiator;
import io.cui.test.valueobjects.objects.ParameterizedInstantiator;
import io.cui.test.valueobjects.objects.RuntimeProperties;
import io.cui.test.valueobjects.testbeans.builder.BuilderWithCollections;

class BuilderParameterizedInstantiatorTest {

    @Test
    void shouldBuildVariantsOfPropertyMetadata() {
        final var validMetadata =
            new RuntimeProperties(immutableSortedSet(BuilderWithCollections.METADATA_COMPLETE));
        final BuilderInstantiator<BuilderWithCollections> builderInstantiator =
            new BuilderFactoryBasedInstantiator<>(BuilderWithCollections.class);
        final ParameterizedInstantiator<BuilderWithCollections> parameterizedInstantiator =
            new BuilderParameterizedInstantiator<>(builderInstantiator, validMetadata);
        assertNotNull(parameterizedInstantiator.newInstanceMinimal());
        assertNotNull(parameterizedInstantiator.newInstanceFull());
    }
}
