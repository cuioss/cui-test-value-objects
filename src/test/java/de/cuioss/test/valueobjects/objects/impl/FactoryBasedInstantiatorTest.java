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

import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.testbeans.factory.BadFactoryBean;
import de.cuioss.test.valueobjects.testbeans.factory.TwoFactoryBean;
import de.cuioss.tools.logging.CuiLogger;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class FactoryBasedInstantiatorTest {

    private static final CuiLogger log = new CuiLogger(FactoryBasedInstantiatorTest.class);

    private static final RuntimeProperties EMPTY_INFORMATION = new RuntimeProperties(Collections.emptyList());

    @Test
    void shouldInstantiateWithNoArgsConstructor() {
        var instantiaor = new FactoryBasedInstantiator<>(TwoFactoryBean.class, EMPTY_INFORMATION, TwoFactoryBean.class,
            TwoFactoryBean.CREATE_METHOD_NAME);
        log.info(instantiaor.toString());
        assertNotNull(instantiaor.newInstanceMinimal());
        assertNotNull(instantiaor.newInstanceFull());
        assertNotNull(instantiaor.newInstance(Collections.emptyList()));
    }

    @Test
    void shouldInstantiateWithSingleArgsConstructor() {
        var instantiaor = new FactoryBasedInstantiator<>(TwoFactoryBean.class, TwoFactoryBean.INFORMATION,
            TwoFactoryBean.class, TwoFactoryBean.CREATE_METHOD_NAME);
        log.info(instantiaor.toString());
        assertNotNull(instantiaor.newInstanceMinimal());
        assertNull(instantiaor.newInstanceMinimal().getAttribute());
        assertNotNull(instantiaor.newInstanceFull());
        assertNotNull(instantiaor.newInstanceFull().getAttribute());
        assertNotNull(instantiaor.newInstance(Collections.emptyList()));
        assertNull(instantiaor.newInstance(Collections.emptyList()).getAttribute());
        assertNotNull(instantiaor.newInstance(TwoFactoryBean.INFORMATION.getAllProperties()));
        assertNotNull(instantiaor.newInstance(TwoFactoryBean.INFORMATION.getAllProperties()).getAttribute());
    }

    @Test
    void shouldFailOnExceptionMethod() {
        var instantiator = new FactoryBasedInstantiator<>(BadFactoryBean.class, EMPTY_INFORMATION, BadFactoryBean.class,
            "boom");
        assertThrows(AssertionError.class, instantiator::newInstanceFull);
    }

    @Test
    void shouldFailOnInvalidReturnType() {
        assertThrows(AssertionError.class, () -> new FactoryBasedInstantiator<>(BadFactoryBean.class, EMPTY_INFORMATION,
            BadFactoryBean.class, "invalidType"));

    }

    @Test
    void shouldFailOnVoidReturnType() {
        assertThrows(AssertionError.class, () -> new FactoryBasedInstantiator<>(BadFactoryBean.class, EMPTY_INFORMATION,
            BadFactoryBean.class, "voidMethod"));
    }

    @Test
    void shouldFailOnNotExistingMethod() {
        assertThrows(AssertionError.class, () -> new FactoryBasedInstantiator<>(BadFactoryBean.class, EMPTY_INFORMATION,
            BadFactoryBean.class, "notThere"));

    }
}
