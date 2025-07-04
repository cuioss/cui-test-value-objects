/**
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.contract;

import de.cuioss.test.valueobjects.api.object.ObjectTestConfig;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.impl.BeanInstantiator;
import de.cuioss.test.valueobjects.objects.impl.ConstructorBasedInstantiator;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;
import de.cuioss.test.valueobjects.testbeans.objectcontract.BadObjectBean;
import de.cuioss.test.valueobjects.testbeans.tostring.ToStringAnnotatedUseMinimalFalse;
import de.cuioss.test.valueobjects.testbeans.tostring.ToStringAnnotatedUseMinimalTrue;
import de.cuioss.tools.reflect.MoreReflection;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static de.cuioss.test.valueobjects.contract.EqualsAndHashcodeContractImplTest.*;
import static org.junit.jupiter.api.Assertions.*;

class ToStringContractImplTest {

    @Test
    void shouldHandleSimpleBeanWithAllAttributesCorrectly() {
        new ToStringContractImpl().assertContract(FULL_BEAN_INSTANIATOR, null);
    }

    @Test
    void shouldHandleSimpleBeanWithoutAttributesCorrectly() {
        new ToStringContractImpl().assertContract(EMPTY_BEAN_INSTANIATOR, null);
    }

    @Test
    void shouldFailOnBadObjectBean() {
        var instantiator = new BeanInstantiator<>(new DefaultInstantiator<>(BadObjectBean.class),
            EMPTY_RUNTIME_INFORMATION);
        var contract = new ToStringContractImpl();
        assertThrows(AssertionError.class, () -> contract.assertContract(instantiator, null));
    }

    @Test
    void shouldHandleBasicContract() {
        var objectTestConfigFalse = MoreReflection
            .extractAnnotation(ToStringAnnotatedUseMinimalFalse.class, ObjectTestConfig.class).get();
        var objectTestConfigTrue = MoreReflection
            .extractAnnotation(ToStringAnnotatedUseMinimalTrue.class, ObjectTestConfig.class).get();
        assertFalse(ToStringContractImpl.shouldUseMinimal(null));
        assertFalse(ToStringContractImpl.shouldUseMinimal(objectTestConfigFalse));
        assertTrue(ToStringContractImpl.shouldUseMinimal(objectTestConfigTrue));
        new ToStringContractImpl()
            .assertContract(new ConstructorBasedInstantiator<>(ToStringAnnotatedUseMinimalFalse.class,
                new RuntimeProperties(Collections.emptyList())), objectTestConfigFalse);
        new ToStringContractImpl()
            .assertContract(new ConstructorBasedInstantiator<>(ToStringAnnotatedUseMinimalTrue.class,
                new RuntimeProperties(Collections.emptyList())), objectTestConfigTrue);
    }
}
