package io.cui.test.valueobjects.objects.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.contract.ToStringContractImpl;
import io.cui.test.valueobjects.objects.ParameterizedInstantiator;
import io.cui.test.valueobjects.objects.RuntimeProperties;
import io.cui.test.valueobjects.property.PropertySupport;
import io.cui.test.valueobjects.testbeans.ComplexBean;

class CallbackAwareInstantiatorTest {

    private MockConfigurationCallbackHandler<ComplexBean> callback;

    private CallbackAwareInstantiator<ComplexBean> instantiator;

    @BeforeEach
    void before() {
        callback = new MockConfigurationCallbackHandler<>();
        ParameterizedInstantiator<ComplexBean> beanInstantiator =
            new BeanInstantiator<>(new DefaultInstantiator<>(ComplexBean.class),
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
        instantiator.newInstance(
                ComplexBean.completeValidMetadata().stream().map(PropertySupport::new).collect(Collectors.toList()),
                true);
        assertTrue(callback.isConfigureCalled());
    }

    @Test
    void shouldImplementBasicContract() {
        new ToStringContractImpl().assertContract(instantiator, null);
        assertNotNull(instantiator.getRuntimeProperties());
    }
}
