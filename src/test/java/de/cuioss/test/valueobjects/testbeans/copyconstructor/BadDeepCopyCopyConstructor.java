/**
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
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
import de.cuioss.test.valueobjects.objects.impl.ConstructorBasedInstantiator;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

@RequiredArgsConstructor
@VerifyCopyConstructor(verifyDeepCopy = true)
@EqualsAndHashCode
@ToString
public class BadDeepCopyCopyConstructor implements Serializable {

    @Serial
    private static final long serialVersionUID = 741300814716513465L;

    public static final PropertyMetadata ATTRIBUTE = PropertyMetadataImpl.builder().name("attribute")
        .generator(Generators.nonEmptyStrings()).required(true).propertyClass(String.class).build();

    public static final PropertyMetadata DATE = PropertyMetadataImpl.builder().name("date")
        .generator(Generators.localDateTimes()).required(true).propertyClass(LocalDateTime.class).build();

    public static final List<PropertyMetadata> ATTRIBUTE_LIST = immutableList(ATTRIBUTE, DATE);

    public static final RuntimeProperties RUNTIME_PROPERTIES = new RuntimeProperties(ATTRIBUTE_LIST);

    public static final TestContract<BadDeepCopyCopyConstructor> MOCK_INSTANTIATOR_CONTRACT = new MockTestContract<>(
        new ConstructorBasedInstantiator<>(BadDeepCopyCopyConstructor.class, RUNTIME_PROPERTIES));

    @Getter
    @NonNull
    private final String attribute;

    @Getter
    @NonNull
    private final LocalDateTime date;

    public BadDeepCopyCopyConstructor(final BadDeepCopyCopyConstructor copyFrom) {
        this(copyFrom.attribute, LocalDateTime.now());
    }
}
