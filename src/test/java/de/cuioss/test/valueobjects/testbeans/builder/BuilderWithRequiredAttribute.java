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

import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.BuilderMetadata;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.SortedSet;

import static de.cuioss.test.valueobjects.generator.JavaTypesGenerator.STRINGS_LETTER;
import static de.cuioss.tools.collect.CollectionLiterals.immutableSortedSet;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class BuilderWithRequiredAttribute implements Serializable {

    @Serial
    private static final long serialVersionUID = 475137338176919505L;

    @Getter
    private final String requiredString;

    public static class BuilderWithRequiredAttributeBuilder {

        private String requiredString;

        public BuilderWithRequiredAttribute build() {
            return new BuilderWithRequiredAttribute(requiredString);
        }
    }

    public static BuilderWithRequiredAttributeBuilder builder() {
        return new BuilderWithRequiredAttributeBuilder();
    }

    public static final SortedSet<PropertyMetadata> METADATA_COMPLETE = immutableSortedSet(BuilderMetadata.builder()
        .delegateMetadata(STRINGS_LETTER.metadataBuilder("requiredString").required(true).build()).build());

    public static final SortedSet<PropertyMetadata> METADATA_INVALID = immutableSortedSet(
        BuilderMetadata.builder().delegateMetadata(STRINGS_LETTER.metadata("requiredString")).build());

}
