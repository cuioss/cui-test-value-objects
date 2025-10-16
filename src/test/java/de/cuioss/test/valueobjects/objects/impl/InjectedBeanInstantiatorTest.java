/*
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

import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.objects.ConfigurationCallBackHandler;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.TestObjectProvider;
import de.cuioss.test.valueobjects.testbeans.ComplexBean;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InjectedBeanInstantiatorTest
    implements TestObjectProvider<ComplexBean>, ConfigurationCallBackHandler<ComplexBean> {

    private RuntimeProperties runtimeProperties;

    /**
     * Clears the {@link TypedGeneratorRegistry}
     */
    @BeforeEach
    void before() {
        TypedGeneratorRegistry.registerBasicTypes();
        runtimeProperties = new RuntimeProperties(ComplexBean.completeValidMetadata());
    }

    /**
     * Clears the {@link TypedGeneratorRegistry}
     */
    @AfterEach
    void tearDownGeneratorRegistry() {
        TypedGeneratorRegistry.clear();
    }

    @Override
    public ComplexBean getUnderTest() {
        return new ComplexBean();
    }

    @Test
    void shouldHandleInstantiator() {
        var instantiator = new InjectedBeanInstantiator<>(this, this, runtimeProperties);
        assertNotNull(instantiator.newInstanceFull());
        assertNotNull(instantiator.newInstanceMinimal());
        assertNotNull(instantiator.toString());
    }

    @Test
    void shouldFailWithoutRuntimeProperties() {
        assertThrows(NullPointerException.class, () -> new InjectedBeanInstantiator<>(this, this, null));
    }
}
