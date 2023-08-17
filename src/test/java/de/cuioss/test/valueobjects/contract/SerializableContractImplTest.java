package de.cuioss.test.valueobjects.contract;

import static de.cuioss.test.valueobjects.contract.EqualsAndHashcodeContractImplTest.EMPTY_BEAN_INSTANIATOR;
import static de.cuioss.test.valueobjects.contract.EqualsAndHashcodeContractImplTest.EMPTY_RUNTIME_INFORMATION;
import static de.cuioss.test.valueobjects.contract.EqualsAndHashcodeContractImplTest.FULL_BEAN_INSTANIATOR;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import de.cuioss.test.valueobjects.testbeans.serializable.SerializationBasicOnlyFalseContract;
import de.cuioss.test.valueobjects.testbeans.serializable.SerializationBasicOnlyTrueContract;
import de.cuioss.test.valueobjects.testbeans.serializable.SerializationEqualsFalseContract;
import de.cuioss.test.valueobjects.testbeans.serializable.SerializationEqualsTrueContract;
import de.cuioss.test.valueobjects.testbeans.serializable.SerializationExclude;
import de.cuioss.test.valueobjects.testbeans.serializable.SerializationOf;
import de.cuioss.test.valueobjects.testbeans.serializable.SerializationReadFailure;
import de.cuioss.test.valueobjects.testbeans.serializable.SerializationWriteFailure;
import de.cuioss.tools.reflect.MoreReflection;

class SerializableContractImplTest {

    @Test
    void shouldHandleSimpleBeanWithAllAttributesCorrectly() {
        new SerializableContractImpl().assertContract(FULL_BEAN_INSTANIATOR, null);
    }

    @Test
    void shouldHandleSimpleBeanWithoutAttributesCorrectly() {
        new SerializableContractImpl().assertContract(EMPTY_BEAN_INSTANIATOR, null);
    }

    @Test
    void shouldFailWithNullAsArgument() {
        var contract = new SerializableContractImpl();
        assertThrows(NullPointerException.class, () -> {
            contract.assertContract(null, null);
        });
    }

    @Test
    void shouldFailOnBadObjectBean() {
        var instantiator = new BeanInstantiator<>(new DefaultInstantiator<>(BadObjectBean.class),
                EMPTY_RUNTIME_INFORMATION);
        var contract = new SerializableContractImpl();
        assertThrows(AssertionError.class, () -> contract.assertContract(instantiator, null));
    }

    @Test
    void shouldFailOnSerializableReadFailure() {
        var instantiator = new ConstructorBasedInstantiator<>(SerializationReadFailure.class,
                new RuntimeProperties(Collections.emptyList()));
        var contract = new SerializableContractImpl();
        assertThrows(AssertionError.class, () -> contract.assertContract(instantiator, null));
    }

    @Test
    void shouldFailOnSerializableWriteFailure() {
        var instantiator = new ConstructorBasedInstantiator<>(SerializationWriteFailure.class,
                new RuntimeProperties(Collections.emptyList()));
        var contract = new SerializableContractImpl();
        assertThrows(AssertionError.class, () -> contract.assertContract(instantiator, null));
    }

    @Test
    void shouldHandleBasicContract() {
        var objectTestConfigFalse = MoreReflection
                .extractAnnotation(SerializationBasicOnlyFalseContract.class, ObjectTestConfig.class).get();
        var objectTestConfigTrue = MoreReflection
                .extractAnnotation(SerializationBasicOnlyTrueContract.class, ObjectTestConfig.class).get();
        assertFalse(SerializableContractImpl.checkTestBasicOnly(null));
        assertFalse(SerializableContractImpl.checkTestBasicOnly(objectTestConfigFalse));
        assertTrue(SerializableContractImpl.checkTestBasicOnly(objectTestConfigTrue));
        new SerializableContractImpl()
                .assertContract(new ConstructorBasedInstantiator<>(SerializationBasicOnlyFalseContract.class,
                        new RuntimeProperties(Collections.emptyList())), objectTestConfigFalse);
        new SerializableContractImpl()
                .assertContract(new ConstructorBasedInstantiator<>(SerializationBasicOnlyTrueContract.class,
                        new RuntimeProperties(Collections.emptyList())), objectTestConfigTrue);
    }

    @Test
    void shouldHandleEqualsOptOutContract() {
        var objectTestConfigFalse = MoreReflection
                .extractAnnotation(SerializationEqualsFalseContract.class, ObjectTestConfig.class).get();
        var objectTestConfigTrue = MoreReflection
                .extractAnnotation(SerializationEqualsTrueContract.class, ObjectTestConfig.class).get();
        assertTrue(SerializableContractImpl.checkForEqualsComparison(null));
        assertFalse(SerializableContractImpl.checkForEqualsComparison(objectTestConfigFalse));
        assertTrue(SerializableContractImpl.checkForEqualsComparison(objectTestConfigTrue));

        new SerializableContractImpl()
                .assertContract(new ConstructorBasedInstantiator<>(SerializationEqualsTrueContract.class,
                        new RuntimeProperties(Collections.emptyList())), objectTestConfigTrue);
    }

    @Test
    void shouldHandleEqualsExcludeAndOfContract() {
        var objectTestConfigExclude = MoreReflection
                .extractAnnotation(SerializationExclude.class, ObjectTestConfig.class).get();
        var objectTestConfigOf = MoreReflection.extractAnnotation(SerializationOf.class, ObjectTestConfig.class).get();
        var properties = FULL_BEAN_INSTANIATOR.getRuntimeProperties().getAllProperties();
        assertEquals(properties, SerializableContractImpl.filterProperties(properties, null));
        assertEquals(properties.size() - 1,
                SerializableContractImpl.filterProperties(properties, objectTestConfigExclude).size());
        assertEquals(1, SerializableContractImpl.filterProperties(properties, objectTestConfigOf).size());
        new SerializableContractImpl().assertContract(new ConstructorBasedInstantiator<>(SerializationExclude.class,
                new RuntimeProperties(Collections.emptyList())), objectTestConfigExclude);
        new SerializableContractImpl().assertContract(new ConstructorBasedInstantiator<>(SerializationOf.class,
                new RuntimeProperties(Collections.emptyList())), objectTestConfigOf);
    }
}
