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
package de.cuioss.test.valueobjects.objects.impl;

import de.cuioss.test.valueobjects.objects.BuilderInstantiator;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.testbeans.builder.BuilderWithCollections;
import org.junit.jupiter.api.Test;

import static de.cuioss.tools.collect.CollectionLiterals.immutableSortedSet;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
