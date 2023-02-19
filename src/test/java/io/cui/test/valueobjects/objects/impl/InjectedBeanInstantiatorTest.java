package io.cui.test.valueobjects.objects.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.generator.TypedGeneratorRegistry;
import io.cui.test.valueobjects.objects.ConfigurationCallBackHandler;
import io.cui.test.valueobjects.objects.RuntimeProperties;
import io.cui.test.valueobjects.objects.TestObjectProvider;
import io.cui.test.valueobjects.testbeans.ComplexBean;

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
        var instantiator =
            new InjectedBeanInstantiator<>(this, this, runtimeProperties);
        assertNotNull(instantiator.newInstanceFull());
        assertNotNull(instantiator.newInstanceMinimal());
        assertNotNull(instantiator.toString());
    }

    @Test
    void shouldFailWithoutRuntimeProperties() {
        assertThrows(NullPointerException.class, () -> new InjectedBeanInstantiator<>(this, this, null));
    }
}
