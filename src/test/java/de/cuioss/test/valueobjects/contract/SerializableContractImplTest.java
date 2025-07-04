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

import de.cuioss.test.valueobjects.api.object.ObjectTestConfig;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.impl.BeanInstantiator;
import de.cuioss.test.valueobjects.objects.impl.ConstructorBasedInstantiator;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;
import de.cuioss.test.valueobjects.testbeans.objectcontract.BadObjectBean;
import de.cuioss.test.valueobjects.testbeans.serializable.*;
import de.cuioss.tools.reflect.MoreReflection;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static de.cuioss.test.valueobjects.contract.EqualsAndHashcodeContractImplTest.*;
import static org.junit.jupiter.api.Assertions.*;

class SerializableContractImplTest {

    @Test
    void shouldHandleSimpleBeanWithAllAttributesCorrectly() {
        new SerializableContractImpl().assertContract(FULL_BEAN_INSTANIATOR, null);
    }

    @Test
    void shouldHandleSimpleBeanWithoutAttributesCorrectly() {
        new SerializableContractImpl().assertContract(EMPTY_BEAN_INSTANIATOR, null);
    }

    @Test
    void shouldFailWithNullAsArgument() {
        var contract = new SerializableContractImpl();
        assertThrows(NullPointerException.class, () ->
            contract.assertContract(null, null));
    }

    @Test
    void shouldFailOnBadObjectBean() {
        var instantiator = new BeanInstantiator<>(new DefaultInstantiator<>(BadObjectBean.class),
            EMPTY_RUNTIME_INFORMATION);
        var contract = new SerializableContractImpl();
        assertThrows(AssertionError.class, () -> contract.assertContract(instantiator, null));
    }

    @Test
    void shouldFailOnSerializableReadFailure() {
        var instantiator = new ConstructorBasedInstantiator<>(SerializationReadFailure.class,
            new RuntimeProperties(Collections.emptyList()));
        var contract = new SerializableContractImpl();
        assertThrows(AssertionError.class, () -> contract.assertContract(instantiator, null));
    }

    @Test
    void shouldFailOnSerializableWriteFailure() {
        var instantiator = new ConstructorBasedInstantiator<>(SerializationWriteFailure.class,
            new RuntimeProperties(Collections.emptyList()));
        var contract = new SerializableContractImpl();
        assertThrows(AssertionError.class, () -> contract.assertContract(instantiator, null));
    }

    @Test
    void shouldHandleBasicContract() {
        var objectTestConfigFalse = MoreReflection
            .extractAnnotation(SerializationBasicOnlyFalseContract.class, ObjectTestConfig.class).get();
        var objectTestConfigTrue = MoreReflection
            .extractAnnotation(SerializationBasicOnlyTrueContract.class, ObjectTestConfig.class).get();
        assertFalse(SerializableContractImpl.checkTestBasicOnly(null));
        assertFalse(SerializableContractImpl.checkTestBasicOnly(objectTestConfigFalse));
        assertTrue(SerializableContractImpl.checkTestBasicOnly(objectTestConfigTrue));
        new SerializableContractImpl()
            .assertContract(new ConstructorBasedInstantiator<>(SerializationBasicOnlyFalseContract.class,
                new RuntimeProperties(Collections.emptyList())), objectTestConfigFalse);
        new SerializableContractImpl()
            .assertContract(new ConstructorBasedInstantiator<>(SerializationBasicOnlyTrueContract.class,
                new RuntimeProperties(Collections.emptyList())), objectTestConfigTrue);
    }

    @Test
    void shouldHandleEqualsOptOutContract() {
        var objectTestConfigFalse = MoreReflection
            .extractAnnotation(SerializationEqualsFalseContract.class, ObjectTestConfig.class).get();
        var objectTestConfigTrue = MoreReflection
            .extractAnnotation(SerializationEqualsTrueContract.class, ObjectTestConfig.class).get();
        assertTrue(SerializableContractImpl.checkForEqualsComparison(null));
        assertFalse(SerializableContractImpl.checkForEqualsComparison(objectTestConfigFalse));
        assertTrue(SerializableContractImpl.checkForEqualsComparison(objectTestConfigTrue));

        new SerializableContractImpl()
            .assertContract(new ConstructorBasedInstantiator<>(SerializationEqualsTrueContract.class,
                new RuntimeProperties(Collections.emptyList())), objectTestConfigTrue);
    }

    @Test
    void shouldHandleEqualsExcludeAndOfContract() {
        var objectTestConfigExclude = MoreReflection
            .extractAnnotation(SerializationExclude.class, ObjectTestConfig.class).get();
        var objectTestConfigOf = MoreReflection.extractAnnotation(SerializationOf.class, ObjectTestConfig.class).get();
        var properties = FULL_BEAN_INSTANIATOR.getRuntimeProperties().getAllProperties();
        assertEquals(properties, SerializableContractImpl.filterProperties(properties, null));
        assertEquals(properties.size() - 1,
            SerializableContractImpl.filterProperties(properties, objectTestConfigExclude).size());
        assertEquals(1, SerializableContractImpl.filterProperties(properties, objectTestConfigOf).size());
        new SerializableContractImpl().assertContract(new ConstructorBasedInstantiator<>(SerializationExclude.class,
            new RuntimeProperties(Collections.emptyList())), objectTestConfigExclude);
        new SerializableContractImpl().assertContract(new ConstructorBasedInstantiator<>(SerializationOf.class,
            new RuntimeProperties(Collections.emptyList())), objectTestConfigOf);
    }
}
