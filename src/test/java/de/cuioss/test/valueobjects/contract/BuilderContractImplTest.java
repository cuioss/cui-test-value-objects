/**
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.contract;

import de.cuioss.test.valueobjects.api.TestContract;
import de.cuioss.test.valueobjects.objects.BuilderInstantiator;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.impl.BuilderFactoryBasedInstantiator;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import de.cuioss.test.valueobjects.testbeans.ComplexBean;
import de.cuioss.test.valueobjects.testbeans.builder.*;
import de.cuioss.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static de.cuioss.tools.collect.CollectionLiterals.immutableSortedSet;
import static org.junit.jupiter.api.Assertions.*;

class BuilderContractImplTest {

    private final RuntimeProperties validMetadata = new RuntimeProperties(
        immutableSortedSet(PropertyMetadataTestDataGenerator.COMPLETE_VALID_ATTRIBUTES));

    private static final BuilderInstantiator<PropertyMetadataImpl> BUILDER_INSTANTIATOR = new BuilderFactoryBasedInstantiator<>(
        PropertyMetadataImpl.class);

    @Test
    void shouldAssertCorrectly() {
        final TestContract<PropertyMetadataImpl> contract = new BuilderContractImpl<>(BUILDER_INSTANTIATOR,
            validMetadata);
        contract.assertContract();
    }

    @Test
    void shouldHandleCollectionAndSingleIntersection() {
        final var runtimeInformation = new RuntimeProperties(
            immutableSortedSet(BuilderWithCollections.METADATA_COMPLETE));
        final BuilderInstantiator<BuilderWithCollections> badBuilderInstantiator = new BuilderFactoryBasedInstantiator<>(
            BuilderWithCollections.class);
        final TestContract<BuilderWithCollections> contract = new BuilderContractImpl<>(badBuilderInstantiator,
            runtimeInformation);
        contract.assertContract();
    }

    @Test
    void shouldHandleCollectionIntersection() {
        final var runtimeInformation = new RuntimeProperties(
            immutableSortedSet(BuilderWithCollections.METADATA_COLLECTION_ONLY));
        final BuilderInstantiator<BuilderWithCollections> badBuilderInstantiator = new BuilderFactoryBasedInstantiator<>(
            BuilderWithCollections.class);
        final TestContract<BuilderWithCollections> contract = new BuilderContractImpl<>(badBuilderInstantiator,
            runtimeInformation);
        contract.assertContract();
    }

    @Test
    void shouldFailOnBadBuildMethod() {
        final var runtimeInformation = new RuntimeProperties(immutableSortedSet());
        final BuilderInstantiator<BadBuilderAlwaysFails> badBuilderInstantiator = new BuilderFactoryBasedInstantiator<>(
            BadBuilderAlwaysFails.class);
        final TestContract<BadBuilderAlwaysFails> contract = new BuilderContractImpl<>(badBuilderInstantiator,
            runtimeInformation);
        assertThrows(AssertionError.class, contract::assertContract);
    }

    @Test
    void shouldFailOnBadPropertySetMethod() {
        final var runtimeInformation = new RuntimeProperties(
            immutableSortedSet(BadBuilderFailsOnAttributeSet.METADATA));
        final BuilderInstantiator<BadBuilderFailsOnAttributeSet> badBuilderInstantiator = new BuilderFactoryBasedInstantiator<>(
            BadBuilderFailsOnAttributeSet.class);
        final TestContract<BadBuilderFailsOnAttributeSet> contract = new BuilderContractImpl<>(badBuilderInstantiator,
            runtimeInformation);
        assertThrows(AssertionError.class, contract::assertContract);
    }

    @Test
    void shouldFailOnBadPropertyReadMethod() {
        final var runtimeInformation = new RuntimeProperties(
            immutableSortedSet(BadBuilderFailsOnAttributeRead.METADATA));
        final BuilderInstantiator<BadBuilderFailsOnAttributeRead> badBuilderInstantiator = new BuilderFactoryBasedInstantiator<>(
            BadBuilderFailsOnAttributeRead.class);
        final TestContract<BadBuilderFailsOnAttributeRead> contract = new BuilderContractImpl<>(badBuilderInstantiator,
            runtimeInformation);
        assertThrows(AssertionError.class, contract::assertContract);
    }

    @Test
    void shouldDetectInvalidRequiredAttribute() {
        final var runtimeInformation = new RuntimeProperties(BuilderWithRequiredAttribute.METADATA_COMPLETE);
        final BuilderInstantiator<BuilderWithRequiredAttribute> builderInstantiator = new BuilderFactoryBasedInstantiator<>(
            BuilderWithRequiredAttribute.class);
        final TestContract<BuilderWithRequiredAttribute> contract = new BuilderContractImpl<>(builderInstantiator,
            runtimeInformation);
        assertThrows(AssertionError.class, contract::assertContract);
    }

    @Test
    void factoryMethodShouldProvideContractOnSimpleFactoryCase() {
        final Optional<BuilderContractImpl<BuilderContractTestMinimal>> contract = BuilderContractImpl
            .createBuilderTestContract(BuilderContractTestMinimal.class, BuilderContractTestMinimal.class,
                immutableList());
        assertTrue(contract.isPresent());
        contract.get().assertContract();
        assertNotNull(contract.get().getInstantiator());
    }

    @Test
    void factoryMethodShouldProvideContractOnDeferredFactoryCase() {
        final Optional<BuilderContractImpl<BuilderContractTestMinimalFactory>> contract = BuilderContractImpl
            .createBuilderTestContract(BuilderContractTestMinimalFactory.class,
                BuilderContractTestMinimalFactory.class, immutableList());
        assertTrue(contract.isPresent());
        contract.get().assertContract();
        assertNotNull(contract.get().getInstantiator());
    }

    @Test
    void factoryMethodShouldProvideContractOnConstructorCase() {
        final Optional<BuilderContractImpl<BuilderContractTestMinimalFactory>> contract = BuilderContractImpl
            .createBuilderTestContract(BuilderContractTestMinimalFactory.class,
                BuilderContractTestConstructor.class, immutableList());
        assertTrue(contract.isPresent());
        contract.get().assertContract();
        assertNotNull(contract.get().getInstantiator());
    }

    @Test
    void factoryMethodShouldNotProvideContractOnInvalidParameter() {
        final Optional<BuilderContractImpl<ComplexBean>> contract = BuilderContractImpl
            .createBuilderTestContract(ComplexBean.class, ComplexBean.class, immutableList());
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
