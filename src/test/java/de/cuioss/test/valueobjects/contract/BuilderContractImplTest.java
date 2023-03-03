package de.cuioss.test.valueobjects.contract;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static de.cuioss.tools.collect.CollectionLiterals.immutableSortedSet;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.api.TestContract;
import de.cuioss.test.valueobjects.objects.BuilderInstantiator;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.impl.BuilderFactoryBasedInstantiator;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import de.cuioss.test.valueobjects.testbeans.ComplexBean;
import de.cuioss.test.valueobjects.testbeans.builder.BadBuilderAlwaysFails;
import de.cuioss.test.valueobjects.testbeans.builder.BadBuilderFailsOnAttributeRead;
import de.cuioss.test.valueobjects.testbeans.builder.BadBuilderFailsOnAttributeSet;
import de.cuioss.test.valueobjects.testbeans.builder.BuilderContractTestConstructor;
import de.cuioss.test.valueobjects.testbeans.builder.BuilderContractTestMinimal;
import de.cuioss.test.valueobjects.testbeans.builder.BuilderContractTestMinimalFactory;
import de.cuioss.test.valueobjects.testbeans.builder.BuilderWithCollections;
import de.cuioss.test.valueobjects.testbeans.builder.BuilderWithRequiredAttribute;
import de.cuioss.test.valueobjects.testbeans.builder.LombokBasedBuilder;
import de.cuioss.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator;

class BuilderContractImplTest {

    private final RuntimeProperties validMetadata =
        new RuntimeProperties(immutableSortedSet(PropertyMetadataTestDataGenerator.COMPLETE_VALID_ATTRIBUTES));

    private static final BuilderInstantiator<PropertyMetadataImpl> BUILDER_INSTANTIATOR =
        new BuilderFactoryBasedInstantiator<>(PropertyMetadataImpl.class);

    @Test
    void shouldAssertCorrectly() {
        final TestContract<PropertyMetadataImpl> contract =
            new BuilderContractImpl<>(BUILDER_INSTANTIATOR, validMetadata);
        contract.assertContract();
    }

    @Test
    void shouldHandleCollectionAndSingleIntersection() {
        final var runtimeInformation =
            new RuntimeProperties(immutableSortedSet(BuilderWithCollections.METADATA_COMPLETE));
        final BuilderInstantiator<BuilderWithCollections> badBuilderInstantiator =
            new BuilderFactoryBasedInstantiator<>(BuilderWithCollections.class);
        final TestContract<BuilderWithCollections> contract =
            new BuilderContractImpl<>(badBuilderInstantiator, runtimeInformation);
        contract.assertContract();
    }

    @Test
    void shouldHandleCollectionIntersection() {
        final var runtimeInformation =
            new RuntimeProperties(immutableSortedSet(BuilderWithCollections.METADATA_COLLECTION_ONLY));
        final BuilderInstantiator<BuilderWithCollections> badBuilderInstantiator =
            new BuilderFactoryBasedInstantiator<>(BuilderWithCollections.class);
        final TestContract<BuilderWithCollections> contract =
            new BuilderContractImpl<>(badBuilderInstantiator, runtimeInformation);
        contract.assertContract();
    }

    @Test
    void shouldFailOnBadBuildMethod() {
        final var runtimeInformation = new RuntimeProperties(immutableSortedSet());
        final BuilderInstantiator<BadBuilderAlwaysFails> badBuilderInstantiator =
            new BuilderFactoryBasedInstantiator<>(BadBuilderAlwaysFails.class);
        final TestContract<BadBuilderAlwaysFails> contract =
            new BuilderContractImpl<>(badBuilderInstantiator, runtimeInformation);
        assertThrows(AssertionError.class, () -> contract.assertContract());
    }

    @Test
    void shouldFailOnBadPropertySetMethod() {
        final var runtimeInformation =
            new RuntimeProperties(immutableSortedSet(BadBuilderFailsOnAttributeSet.METADATA));
        final BuilderInstantiator<BadBuilderFailsOnAttributeSet> badBuilderInstantiator =
            new BuilderFactoryBasedInstantiator<>(BadBuilderFailsOnAttributeSet.class);
        final TestContract<BadBuilderFailsOnAttributeSet> contract =
            new BuilderContractImpl<>(badBuilderInstantiator, runtimeInformation);
        assertThrows(AssertionError.class, () -> contract.assertContract());
    }

    @Test
    void shouldFailOnBadPropertyReadMethod() {
        final var runtimeInformation =
            new RuntimeProperties(immutableSortedSet(BadBuilderFailsOnAttributeRead.METADATA));
        final BuilderInstantiator<BadBuilderFailsOnAttributeRead> badBuilderInstantiator =
            new BuilderFactoryBasedInstantiator<>(BadBuilderFailsOnAttributeRead.class);
        final TestContract<BadBuilderFailsOnAttributeRead> contract =
            new BuilderContractImpl<>(badBuilderInstantiator, runtimeInformation);
        assertThrows(AssertionError.class, () -> contract.assertContract());
    }

    @Test
    void shouldDetectInvalidRequiredAttribute() {
        final var runtimeInformation =
            new RuntimeProperties(BuilderWithRequiredAttribute.METADATA_COMPLETE);
        final BuilderInstantiator<BuilderWithRequiredAttribute> builderInstantiator =
            new BuilderFactoryBasedInstantiator<>(BuilderWithRequiredAttribute.class);
        final TestContract<BuilderWithRequiredAttribute> contract =
            new BuilderContractImpl<>(builderInstantiator, runtimeInformation);
        assertThrows(AssertionError.class, () -> contract.assertContract());
    }

    @Test
    void factoryMethodShouldProvideContractOnSimpleFactoryCase() {
        final Optional<BuilderContractImpl<BuilderContractTestMinimal>> contract =
            BuilderContractImpl.createBuilderTestContract(BuilderContractTestMinimal.class,
                    BuilderContractTestMinimal.class, immutableList());
        assertTrue(contract.isPresent());
        contract.get().assertContract();
        assertNotNull(contract.get().getInstantiator());
    }

    @Test
    void factoryMethodShouldProvideContractOnDeferredFactoryCase() {
        final Optional<BuilderContractImpl<BuilderContractTestMinimalFactory>> contract =
            BuilderContractImpl.createBuilderTestContract(BuilderContractTestMinimalFactory.class,
                    BuilderContractTestMinimalFactory.class, immutableList());
        assertTrue(contract.isPresent());
        contract.get().assertContract();
        assertNotNull(contract.get().getInstantiator());
    }

    @Test
    void factoryMethodShouldProvideContractOnConstructorCase() {
        final Optional<BuilderContractImpl<BuilderContractTestMinimalFactory>> contract =
            BuilderContractImpl.createBuilderTestContract(BuilderContractTestMinimalFactory.class,
                    BuilderContractTestConstructor.class, immutableList());
        assertTrue(contract.isPresent());
        contract.get().assertContract();
        assertNotNull(contract.get().getInstantiator());
    }

    @Test
    void factoryMethodShouldNotProvideContractOnInvalidParameter() {
        final Optional<BuilderContractImpl<ComplexBean>> contract =
            BuilderContractImpl.createBuilderTestContract(ComplexBean.class, ComplexBean.class, immutableList());
        assertFalse(contract.isPresent());
    }

    @Test
    void shouldHandleLombokBuilder() {
        final var runtimeInformation = new RuntimeProperties(LombokBasedBuilder.METADATA_COMPLETE);
        final TestContract<LombokBasedBuilder> contract = new BuilderContractImpl<>(
                new BuilderFactoryBasedInstantiator<>(LombokBasedBuilder.class), runtimeInformation);
        contract.assertContract();
    }
}
