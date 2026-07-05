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
package de.cuioss.test.valueobjects.contract;

import de.cuioss.test.valueobjects.api.TestContract;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.testbeans.ComplexBean;
import de.cuioss.test.valueobjects.testbeans.beanproperty.BeanPropertyTestClassSimple;
import org.junit.jupiter.api.Test;

import java.util.List;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContractRegistryTest {

    private static final List<PropertyMetadata> COMPLETE_METADATA = immutableList(
        ComplexBean.completeValidMetadata());

    @Test
    void shouldResolveContractForAnnotatedTestClass() {
        // BeanPropertyTestClassSimple carries @VerifyBeanProperty
        final List<TestContract<ComplexBean>> contracts = ContractRegistry.resolveTestContracts(ComplexBean.class,
            BeanPropertyTestClassSimple.class, COMPLETE_METADATA);
        assertFalse(contracts.isEmpty());
    }

    @Test
    void shouldResolveNoContractsForUnannotatedTestClass() {
        // This test class declares none of the @Verify* annotations
        final List<TestContract<ComplexBean>> contracts = ContractRegistry.resolveTestContracts(ComplexBean.class,
            ContractRegistryTest.class, COMPLETE_METADATA);
        assertTrue(contracts.isEmpty());
    }
}
