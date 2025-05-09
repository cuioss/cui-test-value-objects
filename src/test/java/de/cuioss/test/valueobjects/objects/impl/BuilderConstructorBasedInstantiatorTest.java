/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

class BuilderConstructorBasedInstantiatorTest {

    private final BuilderInstantiator<PropertyMetadataImpl> instantiator = new BuilderConstructorBasedInstantiator<>(
            PropertyMetadataBuilder.class);

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
                () -> new BuilderConstructorBasedInstantiator<PropertyMetadataImpl>(PropertyMetadataBuilder.class,
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
