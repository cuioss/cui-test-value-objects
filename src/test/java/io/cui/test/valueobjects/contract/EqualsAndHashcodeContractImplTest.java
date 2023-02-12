package io.cui.test.valueobjects.contract;

import static io.cui.tools.collect.CollectionLiterals.immutableSortedSet;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.api.object.ObjectTestConfig;
import io.cui.test.valueobjects.generator.TypedGeneratorRegistry;
import io.cui.test.valueobjects.objects.ParameterizedInstantiator;
import io.cui.test.valueobjects.objects.RuntimeProperties;
import io.cui.test.valueobjects.objects.impl.BeanInstantiator;
import io.cui.test.valueobjects.objects.impl.ConstructorBasedInstantiator;
import io.cui.test.valueobjects.objects.impl.DefaultInstantiator;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.testbeans.ComplexBean;
import io.cui.test.valueobjects.testbeans.objectcontract.BadObjectBean;
import io.cui.test.valueobjects.testbeans.objectcontract.BadObjectBeanWithInvalidEquals;
import io.cui.test.valueobjects.testbeans.objectcontract.BadObjectBeanWithInvalidHashCode;
import io.cui.test.valueobjects.testbeans.objectcontract.EqualsAndHashcodeTwoArgumentBean;
import io.cui.test.valueobjects.testbeans.objectcontract.EqualsAndHashcodeWithExlude;
import io.cui.test.valueobjects.util.ReflectionHelper;

class EqualsAndHashcodeContractImplTest {

    public static final DefaultInstantiator<ComplexBean> SIMPLE_BEAN_INSTANIATOR =
        new DefaultInstantiator<>(ComplexBean.class);

    public static final RuntimeProperties FULL_RUNTIME_INFORMATION =
        new RuntimeProperties(immutableSortedSet(ComplexBean.completeValidMetadata()));

    public static final RuntimeProperties EMPTY_RUNTIME_INFORMATION = new RuntimeProperties(immutableSortedSet());

    public static final BeanInstantiator<ComplexBean> FULL_BEAN_INSTANIATOR =
        new BeanInstantiator<>(SIMPLE_BEAN_INSTANIATOR, FULL_RUNTIME_INFORMATION);

    public static final BeanInstantiator<ComplexBean> EMPTY_BEAN_INSTANIATOR =
        new BeanInstantiator<>(SIMPLE_BEAN_INSTANIATOR, EMPTY_RUNTIME_INFORMATION);

    @Test
    void shouldHandleSimpleBeanWithAllAttributesCorrectly() {
        new EqualsAndHashcodeContractImpl().assertContract(FULL_BEAN_INSTANIATOR, null);
    }

    @Test
    void shouldHandleSimpleBeanWithoutAttributesCorrectly() {
        new EqualsAndHashcodeContractImpl().assertContract(EMPTY_BEAN_INSTANIATOR, null);
    }

    @Test
    void shouldFailOnBadObjectBean() {
        var instantiator =
            new BeanInstantiator<>(new DefaultInstantiator<>(BadObjectBean.class), EMPTY_RUNTIME_INFORMATION);
        var contract = new EqualsAndHashcodeContractImpl();
        assertThrows(AssertionError.class, () -> contract.assertContract(
                instantiator,
                null));
    }

    @Test
    void shouldFailOnBadObjectWithInvalidEquals() {
        var instantiator = new BeanInstantiator<>(
                new DefaultInstantiator<>(BadObjectBeanWithInvalidEquals.class), EMPTY_RUNTIME_INFORMATION);
        var contract = new EqualsAndHashcodeContractImpl();
        assertThrows(AssertionError.class, () -> contract.assertContract(
                instantiator,
                null));
    }

    @Test
    void shouldFailOnBadObjectWithInvalidHashCode() {
        var instantiator = new BeanInstantiator<>(
                new DefaultInstantiator<>(BadObjectBeanWithInvalidHashCode.class), EMPTY_RUNTIME_INFORMATION);
        var contract = new EqualsAndHashcodeContractImpl();
        assertThrows(AssertionError.class, () -> contract.assertContract(
                instantiator,
                null));
    }

    @Test
    void shouldHandleComplexBean() {
        TypedGeneratorRegistry.registerBasicTypes();
        final var instantiator =
            new BeanInstantiator<>(new DefaultInstantiator<>(ComplexBean.class),
                    new RuntimeProperties(ComplexBean.completeValidMetadata()));
        new EqualsAndHashcodeContractImpl().assertContract(instantiator, null);
        TypedGeneratorRegistry.clear();
    }

    @Test
    void shouldHandleTwoArgumentBean() {
        TypedGeneratorRegistry.registerBasicTypes();
        final List<PropertyMetadata> meta = ReflectionHelper
                .handlePropertyMetadata(EqualsAndHashcodeTwoArgumentBean.class, EqualsAndHashcodeTwoArgumentBean.class);
        final var instantiator =
            new ConstructorBasedInstantiator<>(EqualsAndHashcodeTwoArgumentBean.class, new RuntimeProperties(meta));
        new EqualsAndHashcodeContractImpl().assertContract(instantiator, null);
        TypedGeneratorRegistry.clear();
    }

    @Test
    void shouldHandleExclude() {
        TypedGeneratorRegistry.registerBasicTypes();
        final List<PropertyMetadata> meta = ReflectionHelper.handlePropertyMetadata(EqualsAndHashcodeWithExlude.class,
                EqualsAndHashcodeWithExlude.class);
        final ParameterizedInstantiator<EqualsAndHashcodeWithExlude> instantiator = new BeanInstantiator<>(
                new DefaultInstantiator<>(EqualsAndHashcodeWithExlude.class), new RuntimeProperties(meta));
        final var config = EqualsAndHashcodeWithExlude.class.getAnnotation(ObjectTestConfig.class);
        new EqualsAndHashcodeContractImpl().assertContract(instantiator, config);
        TypedGeneratorRegistry.clear();
    }
}
