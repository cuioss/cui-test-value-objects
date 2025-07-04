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

import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.impl.ConstructorBasedInstantiator;
import de.cuioss.test.valueobjects.objects.impl.FactoryBasedInstantiator;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import de.cuioss.test.valueobjects.testbeans.constructor.BeanWithOneConstructorAnnotation;
import de.cuioss.test.valueobjects.testbeans.constructor.SimpleConstructor;
import de.cuioss.test.valueobjects.testbeans.factory.TwoFactoryBean;
import de.cuioss.test.valueobjects.util.PropertyHelper;
import de.cuioss.test.valueobjects.util.ReflectionHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static de.cuioss.test.valueobjects.contract.ObjectCreatorContractImpl.createTestContracts;
import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static org.junit.jupiter.api.Assertions.*;

class ObjectCreatorContractImplTest {

    private static final String ATTRIBUTE2 = "attribute2";

    private RuntimeProperties simpleConstructorMeta;

    private List<PropertyMetadata> complexConstructorMeta;

    @AfterEach
    void after() {
        TypedGeneratorRegistry.clear();
    }

    @BeforeEach
    void before() {
        TypedGeneratorRegistry.registerBasicTypes();
        simpleConstructorMeta = new RuntimeProperties(
            ReflectionHelper.scanBeanTypeForProperties(SimpleConstructor.class, null));
        complexConstructorMeta = new ArrayList<>(
            ReflectionHelper.scanBeanTypeForProperties(PropertyMetadataImpl.class, null));
    }

    @Test
    void shouldHandleConstructorForStandardBean() {
        final var contract = new ObjectCreatorContractImpl<>(
            new ConstructorBasedInstantiator<>(SimpleConstructor.class, simpleConstructorMeta));
        contract.assertContract();
    }

    @Test
    void shouldDetectInvalidRequired() {
        final var map = PropertyHelper.toMapView(simpleConstructorMeta.getAllProperties());
        final PropertyMetadata newAttribute2 = PropertyMetadataImpl.builder(map.get(ATTRIBUTE2)).required(true).build();
        map.put(ATTRIBUTE2, newAttribute2);
        final List<PropertyMetadata> list = new ArrayList<>();
        simpleConstructorMeta.getAllProperties().forEach(p -> list.add(map.get(p.getName())));
        final var information = new RuntimeProperties(list);
        final var contract = new ObjectCreatorContractImpl<>(
            new ConstructorBasedInstantiator<>(SimpleConstructor.class, information));

        assertThrows(AssertionError.class, contract::assertContract);
    }

    @Test
    void factoryShouldIgnoreInvalidType() {
        assertTrue(createTestContracts(ObjectCreatorContractImplTest.class, ObjectCreatorContractImplTest.class,
            Collections.emptyList()).isEmpty());
    }

    @Test
    void factoryShouldHandleSingleAnnotation() {
        assertEquals(1, createTestContracts(BeanWithOneConstructorAnnotation.class,
            BeanWithOneConstructorAnnotation.class, complexConstructorMeta).size());
    }

    @Test
    void shouldDetectFactoryType() {
        final List<ObjectCreatorContractImpl<TwoFactoryBean>> contracts = createTestContracts(TwoFactoryBean.class,
            TwoFactoryBean.class, immutableList(TwoFactoryBean.ATTRIBUTE));
        assertEquals(2, contracts.size());
        contracts.forEach(con -> assertEquals(FactoryBasedInstantiator.class, con.getInstantiator().getClass()));
    }
}
