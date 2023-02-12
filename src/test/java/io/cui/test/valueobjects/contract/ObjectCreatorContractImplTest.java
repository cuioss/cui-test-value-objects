package io.cui.test.valueobjects.contract;

import static io.cui.test.valueobjects.contract.ObjectCreatorContractImpl.createTestContracts;
import static io.cui.tools.collect.CollectionLiterals.immutableList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.generator.TypedGeneratorRegistry;
import io.cui.test.valueobjects.objects.RuntimeProperties;
import io.cui.test.valueobjects.objects.impl.ConstructorBasedInstantiator;
import io.cui.test.valueobjects.objects.impl.FactoryBasedInstantiator;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.impl.PropertyMetadataImpl;
import io.cui.test.valueobjects.testbeans.constructor.BeanWithOneConstructorAnnotation;
import io.cui.test.valueobjects.testbeans.constructor.SimpleConstructor;
import io.cui.test.valueobjects.testbeans.factory.TwoFactoryBean;
import io.cui.test.valueobjects.util.PropertyHelper;
import io.cui.test.valueobjects.util.ReflectionHelper;

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
        simpleConstructorMeta =
            new RuntimeProperties(ReflectionHelper.scanBeanTypeForProperties(SimpleConstructor.class, null));
        complexConstructorMeta =
            new ArrayList<>(ReflectionHelper.scanBeanTypeForProperties(PropertyMetadataImpl.class, null));
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
        final var contract =
            new ObjectCreatorContractImpl<>(
                    new ConstructorBasedInstantiator<>(SimpleConstructor.class, information));

        assertThrows(AssertionError.class, () -> contract.assertContract());
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
        final List<ObjectCreatorContractImpl<TwoFactoryBean>> contracts =
            createTestContracts(TwoFactoryBean.class, TwoFactoryBean.class, immutableList(TwoFactoryBean.ATTRIBUTE));
        assertEquals(2, contracts.size());
        contracts.forEach(con -> assertEquals(FactoryBasedInstantiator.class, con.getInstantiator().getClass()));
    }
}
