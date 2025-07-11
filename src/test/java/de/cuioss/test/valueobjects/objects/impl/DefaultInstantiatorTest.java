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

import de.cuioss.test.valueobjects.testbeans.ComplexBean;
import de.cuioss.test.valueobjects.testbeans.constructor.BeanWithSingleArgumentConstructor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultInstantiatorTest {

    @Test
    void shouldInstantiateSimpleBean() {
        final var instantiator = new DefaultInstantiator<>(ComplexBean.class);
        final var bean = instantiator.newInstance();
        assertNotNull(bean);
        assertEquals(ComplexBean.class, instantiator.getTargetClass());
    }

    @Test
    void shouldfailToInstantiateBeanWithoutDefaultConstructor() {
        var instantiator = new DefaultInstantiator<>(BeanWithSingleArgumentConstructor.class);
        assertThrows(AssertionError.class, instantiator::newInstance);
    }

    @Test
    void shouldfailToConstructUsingNull() {
        assertThrows(NullPointerException.class,
            () -> new DefaultInstantiator<BeanWithSingleArgumentConstructor>(null));
    }
}
