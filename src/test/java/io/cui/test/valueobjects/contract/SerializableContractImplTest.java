package io.cui.test.valueobjects.contract;

import static io.cui.test.valueobjects.contract.EqualsAndHashcodeContractImplTest.EMPTY_BEAN_INSTANIATOR;
import static io.cui.test.valueobjects.contract.EqualsAndHashcodeContractImplTest.EMPTY_RUNTIME_INFORMATION;
import static io.cui.test.valueobjects.contract.EqualsAndHashcodeContractImplTest.FULL_BEAN_INSTANIATOR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.api.object.ObjectTestConfig;
import io.cui.test.valueobjects.objects.RuntimeProperties;
import io.cui.test.valueobjects.objects.impl.BeanInstantiator;
import io.cui.test.valueobjects.objects.impl.ConstructorBasedInstantiator;
import io.cui.test.valueobjects.objects.impl.DefaultInstantiator;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.testbeans.objectcontract.BadObjectBean;
import io.cui.test.valueobjects.testbeans.serializable.SerializationBasicOnlyFalseContract;
import io.cui.test.valueobjects.testbeans.serializable.SerializationBasicOnlyTrueContract;
import io.cui.test.valueobjects.testbeans.serializable.SerializationEqualsFalseContract;
import io.cui.test.valueobjects.testbeans.serializable.SerializationEqualsTrueContract;
import io.cui.test.valueobjects.testbeans.serializable.SerializationExclude;
import io.cui.test.valueobjects.testbeans.serializable.SerializationOf;
import io.cui.test.valueobjects.testbeans.serializable.SerializationReadFailure;
import io.cui.test.valueobjects.testbeans.serializable.SerializationWriteFailure;
import io.cui.tools.reflect.MoreReflection;

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
        SerializableContractImpl contract = new SerializableContractImpl();
        assertThrows(NullPointerException.class, () -> {
            contract.assertContract(null, null);
        });
    }

    @Test
    void shouldFailOnBadObjectBean() {
        BeanInstantiator<BadObjectBean> instantiator =
            new BeanInstantiator<>(new DefaultInstantiator<>(BadObjectBean.class), EMPTY_RUNTIME_INFORMATION);
        SerializableContractImpl contract = new SerializableContractImpl();
        assertThrows(AssertionError.class, () -> contract.assertContract(
                instantiator,
                null));
    }

    @Test
    void shouldFailOnSerializableReadFailure() {
        ConstructorBasedInstantiator<SerializationReadFailure> instantiator =
            new ConstructorBasedInstantiator<>(SerializationReadFailure.class,
                    new RuntimeProperties(Collections.emptyList()));
        SerializableContractImpl contract = new SerializableContractImpl();
        assertThrows(AssertionError.class, () -> contract.assertContract(
                instantiator,
                null));
    }

    @Test
    void shouldFailOnSerializableWriteFailure() {
        ConstructorBasedInstantiator<SerializationWriteFailure> instantiator = new ConstructorBasedInstantiator<>(
                SerializationWriteFailure.class, new RuntimeProperties(Collections.emptyList()));
        SerializableContractImpl contract = new SerializableContractImpl();
        assertThrows(AssertionError.class, () -> contract.assertContract(
                instantiator,
                null));
    }

    @Test
    void shouldHandleBasicContract() {
        ObjectTestConfig objectTestConfigFalse =
            MoreReflection.extractAnnotation(SerializationBasicOnlyFalseContract.class, ObjectTestConfig.class).get();
        ObjectTestConfig objectTestConfigTrue =
            MoreReflection.extractAnnotation(SerializationBasicOnlyTrueContract.class, ObjectTestConfig.class).get();
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
        ObjectTestConfig objectTestConfigFalse =
            MoreReflection.extractAnnotation(SerializationEqualsFalseContract.class, ObjectTestConfig.class).get();
        ObjectTestConfig objectTestConfigTrue =
            MoreReflection.extractAnnotation(SerializationEqualsTrueContract.class, ObjectTestConfig.class).get();
        assertTrue(SerializableContractImpl.checkForEqualsComparison(null));
        assertFalse(SerializableContractImpl.checkForEqualsComparison(objectTestConfigFalse));
        assertTrue(SerializableContractImpl.checkForEqualsComparison(objectTestConfigTrue));

        new SerializableContractImpl()
                .assertContract(new ConstructorBasedInstantiator<>(SerializationEqualsTrueContract.class,
                        new RuntimeProperties(Collections.emptyList())), objectTestConfigTrue);
    }

    @Test
    void shouldHandleEqualsExcludeAndOfContract() {
        ObjectTestConfig objectTestConfigExclude =
            MoreReflection.extractAnnotation(SerializationExclude.class, ObjectTestConfig.class).get();
        ObjectTestConfig objectTestConfigOf =
            MoreReflection.extractAnnotation(SerializationOf.class, ObjectTestConfig.class).get();
        List<PropertyMetadata> properties = FULL_BEAN_INSTANIATOR.getRuntimeProperties().getAllProperties();
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
