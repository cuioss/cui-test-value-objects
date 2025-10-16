/*
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

import de.cuioss.test.generator.Generators;
import de.cuioss.test.valueobjects.api.TestContract;
import de.cuioss.test.valueobjects.api.contracts.VerifyCopyConstructor;
import de.cuioss.test.valueobjects.contract.MockTestContract;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.impl.BeanInstantiator;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import de.cuioss.tools.property.PropertyReadWrite;
import lombok.*;

import java.util.List;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static java.util.Objects.requireNonNull;

@VerifyCopyConstructor
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class MockWithReadOnly {

    public static final PropertyMetadata READWRITE = PropertyMetadataImpl.builder().name("readWrite")
        .generator(Generators.nonEmptyStrings()).required(true).propertyClass(String.class).build();

    public static final PropertyMetadata READONLY = PropertyMetadataImpl.builder().name("readOnly")
        .generator(Generators.nonEmptyStrings()).required(true).propertyClass(String.class)
        .propertyReadWrite(PropertyReadWrite.READ_ONLY).build();

    public static final List<PropertyMetadata> ATTRIBUTE_LIST = immutableList(READWRITE, READONLY);

    public static final RuntimeProperties RUNTIME_PROPERTIES = new RuntimeProperties(ATTRIBUTE_LIST);

    public static final TestContract<MockWithReadOnly> MOCK_INSTANTIATOR_CONTRACT = new MockTestContract<>(
        new BeanInstantiator<>(new DefaultInstantiator<>(MockWithReadOnly.class), RUNTIME_PROPERTIES));

    @Getter
    @Setter
    private String readWrite;

    @Getter
    private String readOnly;

    public MockWithReadOnly(final MockWithReadOnly copyFrom) {
        requireNonNull(copyFrom);
        readWrite = copyFrom.getReadWrite();
        readOnly = copyFrom.getReadOnly();
    }

}
