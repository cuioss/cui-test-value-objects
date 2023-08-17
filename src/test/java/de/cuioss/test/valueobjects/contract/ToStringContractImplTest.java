package de.cuioss.test.valueobjects.contract;

import static de.cuioss.test.valueobjects.contract.EqualsAndHashcodeContractImplTest.EMPTY_BEAN_INSTANIATOR;
import static de.cuioss.test.valueobjects.contract.EqualsAndHashcodeContractImplTest.EMPTY_RUNTIME_INFORMATION;
import static de.cuioss.test.valueobjects.contract.EqualsAndHashcodeContractImplTest.FULL_BEAN_INSTANIATOR;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.api.object.ObjectTestConfig;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.impl.BeanInstantiator;
import de.cuioss.test.valueobjects.objects.impl.ConstructorBasedInstantiator;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;
import de.cuioss.test.valueobjects.testbeans.objectcontract.BadObjectBean;
import de.cuioss.test.valueobjects.testbeans.tostring.ToStringAnnotatedUseMinimalFalse;
import de.cuioss.test.valueobjects.testbeans.tostring.ToStringAnnotatedUseMinimalTrue;
import de.cuioss.tools.reflect.MoreReflection;

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
        var instantiator = new BeanInstantiator<>(new DefaultInstantiator<>(BadObjectBean.class),
                EMPTY_RUNTIME_INFORMATION);
        var contract = new ToStringContractImpl();
        assertThrows(AssertionError.class, () -> contract.assertContract(instantiator, null));
    }

    @Test
    void shouldHandleBasicContract() {
        var objectTestConfigFalse = MoreReflection
                .extractAnnotation(ToStringAnnotatedUseMinimalFalse.class, ObjectTestConfig.class).get();
        var objectTestConfigTrue = MoreReflection
                .extractAnnotation(ToStringAnnotatedUseMinimalTrue.class, ObjectTestConfig.class).get();
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
