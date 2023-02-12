package io.cui.test.valueobjects.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.cui.test.valueobjects.api.TestContract;
import io.cui.test.valueobjects.property.PropertyMetadata;
import lombok.experimental.UtilityClass;

/**
 * Helper class handling the registration of {@link TestContract}s
 *
 * @author Oliver Wolff
 *
 */
@UtilityClass
public class ContractRegistry {

    /**
     * Resolves the concrete {@link TestContract}s to be tested. They are derived by the
     * corresponding annotations
     *
     * @param targetBeanClass The actual class underTest
     * @param unitTestClass The test-class
     * @param propertyMetadata
     * @return a list of configured {@link TestContract}s
     */
    public static <T> List<TestContract<T>> resolveTestContracts(Class<T> targetBeanClass, Class<?> unitTestClass,
            final List<PropertyMetadata> propertyMetadata) {

        final List<TestContract<T>> builder = new ArrayList<>();
        Optional<? extends TestContract<T>> contract = BeanPropertyContractImpl
                .createBeanPropertyTestContract(targetBeanClass, unitTestClass,
                        propertyMetadata);
        contract.ifPresent(builder::add);
        contract = BuilderContractImpl.createBuilderTestContract(targetBeanClass, unitTestClass,
                propertyMetadata);
        contract.ifPresent(builder::add);
        builder.addAll(ObjectCreatorContractImpl.createTestContracts(targetBeanClass, unitTestClass,
                propertyMetadata));

        contract = CopyConstructorContractImpl.createTestContract(targetBeanClass, unitTestClass,
                propertyMetadata, builder);
        contract.ifPresent(builder::add);

        return builder;
    }
}
