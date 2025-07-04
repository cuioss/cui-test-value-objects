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
package de.cuioss.test.valueobjects.testbeans.copyconstructor;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


import de.cuioss.test.generator.Generators;
import de.cuioss.test.valueobjects.api.TestContract;
import de.cuioss.test.valueobjects.api.contracts.VerifyCopyConstructor;
import de.cuioss.test.valueobjects.contract.MockTestContract;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.impl.ConstructorBasedInstantiator;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@VerifyCopyConstructor(verifyDeepCopy = true)
@EqualsAndHashCode
@ToString
public class DeepCopyCopyConstructor implements Serializable {

    @Serial
    private static final long serialVersionUID = 741300814716513465L;

    public static final PropertyMetadata ATTRIBUTE = PropertyMetadataImpl.builder().name("attribute")
            .generator(Generators.nonEmptyStrings()).required(true).propertyClass(String.class).build();

    public static final List<PropertyMetadata> ATTRIBUTE_LIST = immutableList(ATTRIBUTE);

    public static final RuntimeProperties RUNTIME_PROPERTIES = new RuntimeProperties(ATTRIBUTE_LIST);

    public static final TestContract<DeepCopyCopyConstructor> MOCK_INSTANTIATOR_CONTRACT = new MockTestContract<>(
            new ConstructorBasedInstantiator<>(DeepCopyCopyConstructor.class, RUNTIME_PROPERTIES));

    @Getter
    @NonNull
    private final String attribute;

    public DeepCopyCopyConstructor(final DeepCopyCopyConstructor copyFrom) {
        this(copyFrom.attribute);
    }
}
