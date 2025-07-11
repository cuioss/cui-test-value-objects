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

import de.cuioss.test.valueobjects.api.TestContract;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.impl.BeanInstantiator;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.testbeans.ComplexBean;
import de.cuioss.test.valueobjects.testbeans.beanproperty.BeanPropertyTestClassSimple;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.SortedSet;

import static de.cuioss.test.valueobjects.generator.JavaTypesGenerator.STRINGS;
import static de.cuioss.test.valueobjects.testbeans.ComplexBean.ATTRIBUTE_BAD_STRING;
import static de.cuioss.tools.collect.CollectionLiterals.*;
import static org.junit.jupiter.api.Assertions.*;

class BeanPropertyContractImplTest {

    private static final SortedSet<PropertyMetadata> COMPLETE_METADATA = immutableSortedSet(
        ComplexBean.completeValidMetadata());

    private static final List<PropertyMetadata> COMPLETE_METADATA_AS_LIST = immutableList(COMPLETE_METADATA);

    private static final BeanInstantiator<ComplexBean> BEAN_INSTANTIATOR = new BeanInstantiator<>(
        new DefaultInstantiator<>(ComplexBean.class), new RuntimeProperties(COMPLETE_METADATA));

    @Test
    void shouldHandleComplexSetup() {
        final var support = new BeanPropertyContractImpl<>(BEAN_INSTANTIATOR);
        support.assertContract();
    }

    @Test
    void shouldFailOnInvalidBean() {
        final SortedSet<PropertyMetadata> generators = mutableSortedSet();
        generators.add(STRINGS.metadata(ATTRIBUTE_BAD_STRING));
        final var instantiator = new BeanInstantiator<>(new DefaultInstantiator<>(ComplexBean.class),
            new RuntimeProperties(generators));
        final var support = new BeanPropertyContractImpl<>(instantiator);
        assertThrows(AssertionError.class, support::assertContract);
    }

    @Test
    void factoryMethodShouldProvideContractOnValidParameter() {
        final Optional<TestContract<ComplexBean>> contract = BeanPropertyContractImpl.createBeanPropertyTestContract(
            ComplexBean.class, BeanPropertyTestClassSimple.class, COMPLETE_METADATA_AS_LIST);
        assertTrue(contract.isPresent());
        contract.get().assertContract();
        assertNotNull(contract.get().getInstantiator());
    }

    @Test
    void factoryMethodShouldNotProvideContractOnInvalidParameter() {
        Optional<TestContract<ComplexBean>> contract = BeanPropertyContractImpl
            .createBeanPropertyTestContract(ComplexBean.class, this.getClass(), COMPLETE_METADATA_AS_LIST);
        assertFalse(contract.isPresent());
        contract = BeanPropertyContractImpl.createBeanPropertyTestContract(ComplexBean.class,
            BeanPropertyTestClassSimple.class, immutableList());
        assertFalse(contract.isPresent());
    }
}
