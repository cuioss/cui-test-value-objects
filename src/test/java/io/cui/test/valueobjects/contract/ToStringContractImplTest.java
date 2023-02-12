package io.cui.test.valueobjects.contract;

import static io.cui.test.valueobjects.contract.EqualsAndHashcodeContractImplTest.EMPTY_BEAN_INSTANIATOR;
import static io.cui.test.valueobjects.contract.EqualsAndHashcodeContractImplTest.EMPTY_RUNTIME_INFORMATION;
import static io.cui.test.valueobjects.contract.EqualsAndHashcodeContractImplTest.FULL_BEAN_INSTANIATOR;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.api.object.ObjectTestConfig;
import io.cui.test.valueobjects.objects.RuntimeProperties;
import io.cui.test.valueobjects.objects.impl.BeanInstantiator;
import io.cui.test.valueobjects.objects.impl.ConstructorBasedInstantiator;
import io.cui.test.valueobjects.objects.impl.DefaultInstantiator;
import io.cui.test.valueobjects.testbeans.objectcontract.BadObjectBean;
import io.cui.test.valueobjects.testbeans.tostring.ToStringAnnotatedUseMinimalFalse;
import io.cui.test.valueobjects.testbeans.tostring.ToStringAnnotatedUseMinimalTrue;
import io.cui.tools.reflect.MoreReflection;

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
        BeanInstantiator<BadObjectBean> instantiator =
            new BeanInstantiator<>(new DefaultInstantiator<>(BadObjectBean.class), EMPTY_RUNTIME_INFORMATION);
        ToStringContractImpl contract = new ToStringContractImpl();
        assertThrows(AssertionError.class, () -> contract.assertContract(
                instantiator,
                null));
    }

    @Test
    void shouldHandleBasicContract() {
        ObjectTestConfig objectTestConfigFalse =
            MoreReflection.extractAnnotation(ToStringAnnotatedUseMinimalFalse.class, ObjectTestConfig.class).get();
        ObjectTestConfig objectTestConfigTrue =
            MoreReflection.extractAnnotation(ToStringAnnotatedUseMinimalTrue.class, ObjectTestConfig.class).get();
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
