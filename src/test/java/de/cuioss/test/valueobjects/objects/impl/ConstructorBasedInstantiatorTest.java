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

import static de.cuioss.tools.collect.CollectionLiterals.mutableList;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.testbeans.ComplexBean;
import de.cuioss.test.valueobjects.testbeans.constructor.SimpleConstructor;
import de.cuioss.test.valueobjects.util.ReflectionHelper;
import de.cuioss.tools.collect.CollectionLiterals;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConstructorBasedInstantiatorTest {

    private static final RuntimeProperties EMPTY_METADATA = new RuntimeProperties(mutableList());

    private RuntimeProperties simpleConstructorMeta;

    @AfterEach void after() {
        TypedGeneratorRegistry.clear();
    }

    @BeforeEach void before() {
        TypedGeneratorRegistry.registerBasicTypes();
        simpleConstructorMeta = new RuntimeProperties(
                ReflectionHelper.scanBeanTypeForProperties(SimpleConstructor.class, null));
    }

    @Test void shouldHandleDefaultConstructor() {
        final var instantiator = new ConstructorBasedInstantiator<>(ComplexBean.class, EMPTY_METADATA);
        assertNotNull(instantiator.newInstanceMinimal());
        assertNotNull(instantiator.newInstanceFull());
        assertNotNull(instantiator.newInstance(mutableList()));
        // Ignore invalid properties
        assertNotNull(instantiator.newInstance(simpleConstructorMeta.getAllProperties()));
    }

    @Test void shouldHandleSimpleConstructor() {
        final var instantiator = new ConstructorBasedInstantiator<>(SimpleConstructor.class, simpleConstructorMeta);
        var instance = instantiator.newInstanceMinimal();
        assertNotNull(instance);
        assertNull(instance.getAttribute1());
        assertNull(instance.getAttribute2());
        assertNotEquals(0, instance.getAttribute3());
        assertNull(instance.getAttribute5());
        instance = instantiator.newInstanceFull();
        assertNotNull(instance);
        assertNotNull(instance.getAttribute1());
        assertNotNull(instance.getAttribute2());
        assertNotEquals(0, instance.getAttribute3());
        assertNotNull(instance.getAttribute5());
        assertNotNull(instantiator.newInstance(mutableList()));
    }

    @Test void shouldFailWithInvalidConstructor() {
        var firstProperty = simpleConstructorMeta.getAllProperties().getFirst();
        var runtimeProperties = new RuntimeProperties(CollectionLiterals.immutableList(firstProperty));
        assertThrows(AssertionError.class, () -> {
            new ConstructorBasedInstantiator<>(SimpleConstructor.class, runtimeProperties);
        });
    }
}
