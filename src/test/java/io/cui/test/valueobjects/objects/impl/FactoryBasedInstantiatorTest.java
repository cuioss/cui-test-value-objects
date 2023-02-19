package io.cui.test.valueobjects.objects.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.objects.RuntimeProperties;
import io.cui.test.valueobjects.testbeans.factory.BadFactoryBean;
import io.cui.test.valueobjects.testbeans.factory.TwoFactoryBean;
import io.cui.tools.logging.CuiLogger;

class FactoryBasedInstantiatorTest {

    private static final CuiLogger log = new CuiLogger(FactoryBasedInstantiatorTest.class);

    private static final RuntimeProperties EMPTY_INFORMATION = new RuntimeProperties(Collections.emptyList());

    @Test
    void shouldInstantiateWithNoArgsConstructor() {
        var instantiaor = new FactoryBasedInstantiator<>(TwoFactoryBean.class,
                EMPTY_INFORMATION, TwoFactoryBean.class, TwoFactoryBean.CREATE_METHOD_NAME);
        log.info(instantiaor.toString());
        assertNotNull(instantiaor.newInstanceMinimal());
        assertNotNull(instantiaor.newInstanceFull());
        assertNotNull(instantiaor.newInstance(Collections.emptyList()));
    }

    @Test
    void shouldInstantiateWithSingleArgsConstructor() {
        var instantiaor = new FactoryBasedInstantiator<>(TwoFactoryBean.class,
                TwoFactoryBean.INFORMATION, TwoFactoryBean.class, TwoFactoryBean.CREATE_METHOD_NAME);
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
        var instantiator =
            new FactoryBasedInstantiator<>(BadFactoryBean.class, EMPTY_INFORMATION, BadFactoryBean.class,
                    "boom");
        assertThrows(AssertionError.class, () -> instantiator.newInstanceFull());
    }

    @Test
    void shouldFailOnInvalidReturnType() {
        assertThrows(AssertionError.class, () -> new FactoryBasedInstantiator<>(BadFactoryBean.class,
                EMPTY_INFORMATION, BadFactoryBean.class, "invalidType"));

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
