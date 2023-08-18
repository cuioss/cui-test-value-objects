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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.contract.ToStringContractImpl;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.property.PropertySupport;
import de.cuioss.test.valueobjects.testbeans.ComplexBean;

class CallbackAwareInstantiatorTest {

    private MockConfigurationCallbackHandler<ComplexBean> callback;

    private CallbackAwareInstantiator<ComplexBean> instantiator;

    @BeforeEach
    void before() {
        callback = new MockConfigurationCallbackHandler<>();
        ParameterizedInstantiator<ComplexBean> beanInstantiator = new BeanInstantiator<>(
                new DefaultInstantiator<>(ComplexBean.class),
                new RuntimeProperties(ComplexBean.completeValidMetadata()));
        instantiator = new CallbackAwareInstantiator<>(beanInstantiator, callback);
    }

    @Test
    void shouldHandleCallbacks() {
        assertFalse(callback.isConfigureCalled());
        instantiator.newInstanceFull();
        assertTrue(callback.isConfigureCalled());
        callback.reset();
        instantiator.newInstanceMinimal();
        assertTrue(callback.isConfigureCalled());
        callback.reset();
        instantiator.newInstance(ComplexBean.completeValidMetadata());
        assertTrue(callback.isConfigureCalled());
        callback.reset();
        instantiator.newInstance(ComplexBean.completeValidMetadata().stream().map(PropertySupport::new).toList(), true);
        assertTrue(callback.isConfigureCalled());
    }

    @Test
    void shouldImplementBasicContract() {
        new ToStringContractImpl().assertContract(instantiator, null);
        assertNotNull(instantiator.getRuntimeProperties());
    }
}
