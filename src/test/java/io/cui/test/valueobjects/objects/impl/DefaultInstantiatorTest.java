package io.cui.test.valueobjects.objects.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.testbeans.ComplexBean;
import io.cui.test.valueobjects.testbeans.constructor.BeanWithSingleArgumentConstructor;

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
        var instantiator =
            new DefaultInstantiator<>(BeanWithSingleArgumentConstructor.class);
        assertThrows(AssertionError.class,
                () -> instantiator.newInstance());
    }

    @Test
    void shouldfailToConstructUsingNull() {
        assertThrows(NullPointerException.class,
                () -> new DefaultInstantiator<BeanWithSingleArgumentConstructor>(null));
    }
}
