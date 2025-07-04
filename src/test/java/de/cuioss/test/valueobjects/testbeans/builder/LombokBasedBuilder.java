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
package de.cuioss.test.valueobjects.testbeans.builder;

import static de.cuioss.test.valueobjects.generator.JavaTypesGenerator.STRINGS_LETTER;
import static de.cuioss.tools.collect.CollectionLiterals.immutableSortedSet;

import java.io.Serial;
import java.io.Serializable;
import java.util.SortedSet;


import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.BuilderMetadata;
import de.cuioss.test.valueobjects.property.util.CollectionType;
import de.cuioss.test.valueobjects.property.util.PropertyAccessStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.ToString;

@EqualsAndHashCode
@AllArgsConstructor
@ToString
@Builder
public class LombokBasedBuilder implements Serializable {

    @Serial
    private static final long serialVersionUID = 5021276874059936648L;

    public static final SortedSet<PropertyMetadata> METADATA_COMPLETE = immutableSortedSet(BuilderMetadata.builder()
                    .delegateMetadata(STRINGS_LETTER.metadataBuilder("name").required(true)
                            .propertyAccessStrategy(PropertyAccessStrategy.BUILDER_DIRECT).build())
                    .build(),
            BuilderMetadata.builder()
                    .delegateMetadata(STRINGS_LETTER.metadataBuilder("elements").required(false)
                            .collectionType(CollectionType.SORTED_SET)
                            .propertyAccessStrategy(PropertyAccessStrategy.BUILDER_COLLECTION_AND_SINGLE_ELEMENT)
                            .build())
                    .builderSingleAddMethodName("element").build());
    @Getter
    @NonNull
    private final String name;

    @Singular
    @Getter
    private final SortedSet<String> elements;

    public LombokBasedBuilder(LombokBasedBuilder copy) {
        name = copy.getName();
        elements = copy.getElements();
    }

}
