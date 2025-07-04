/**
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.contract;

import de.cuioss.test.valueobjects.api.TestContract;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Helper class handling the registration of {@link TestContract}s
 *
 * @author Oliver Wolff
 *
 */
@UtilityClass
public class ContractRegistry {

    /**
     * Resolves the concrete {@link TestContract}s to be tested. They are derived by
     * the corresponding annotations
     *
     * @param targetBeanClass  The actual class underTest
     * @param unitTestClass    The test-class
     * @param propertyMetadata
     * @return a list of configured {@link TestContract}s
     */
    public static <T> List<TestContract<T>> resolveTestContracts(Class<T> targetBeanClass, Class<?> unitTestClass,
        final List<PropertyMetadata> propertyMetadata) {

        final List<TestContract<T>> builder = new ArrayList<>();
        Optional<? extends TestContract<T>> contract = BeanPropertyContractImpl
            .createBeanPropertyTestContract(targetBeanClass, unitTestClass, propertyMetadata);
        contract.ifPresent(builder::add);
        contract = BuilderContractImpl.createBuilderTestContract(targetBeanClass, unitTestClass, propertyMetadata);
        contract.ifPresent(builder::add);
        builder.addAll(ObjectCreatorContractImpl.createTestContracts(targetBeanClass, unitTestClass, propertyMetadata));

        contract = CopyConstructorContractImpl.createTestContract(targetBeanClass, unitTestClass, propertyMetadata,
            builder);
        contract.ifPresent(builder::add);

        return builder;
    }
}
