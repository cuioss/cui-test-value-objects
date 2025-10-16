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
package de.cuioss.test.valueobjects.testbeans.builder;

import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.BuilderMetadata;
import de.cuioss.test.valueobjects.property.util.CollectionType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import static de.cuioss.test.valueobjects.generator.JavaTypesGenerator.STRINGS_LETTER;
import static de.cuioss.test.valueobjects.property.util.PropertyAccessStrategy.BUILDER_COLLECTION_AND_SINGLE_ELEMENT;
import static de.cuioss.test.valueobjects.property.util.PropertyAccessStrategy.BUILDER_DIRECT;
import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class BuilderWithCollections implements Serializable {

    @Serial
    private static final long serialVersionUID = 475137338176919505L;

    @Getter
    private final Set<String> stringSetElements;

    @Getter
    private final SortedSet<String> stringSortedSetElements;

    @Getter
    private final Collection<String> stringCollection;

    public static class BuilderWithCollectionsBuilder {

        private final Set<String> stringSetElements = new HashSet<>();

        private final SortedSet<String> stringSortedSetElements = new TreeSet<>();

        private Collection<String> stringCollection;

        public BuilderWithCollectionsBuilder stringSetElements(final Set<String> elementSet) {
            stringSetElements.addAll(elementSet);
            return this;
        }

        public BuilderWithCollectionsBuilder stringSetElement(final String element) {
            stringSetElements.add(element);
            return this;
        }

        public BuilderWithCollectionsBuilder stringSortedSetElements(final SortedSet<String> elementSortedSet) {
            stringSortedSetElements.addAll(elementSortedSet);
            return this;
        }

        public BuilderWithCollectionsBuilder stringSortedSetElement(final String element) {
            stringSortedSetElements.add(element);
            return this;
        }

        public BuilderWithCollectionsBuilder stringCollection(final Collection<String> elementCollection) {
            stringCollection = elementCollection;
            return this;
        }

        public BuilderWithCollections build() {
            return new BuilderWithCollections(stringSetElements, stringSortedSetElements, stringCollection);

        }
    }

    public static BuilderWithCollectionsBuilder builder() {
        return new BuilderWithCollectionsBuilder();
    }

    public static final List<PropertyMetadata> METADATA_COMPLETE = immutableList(BuilderMetadata.builder()
            .delegateMetadata(STRINGS_LETTER.metadataBuilder("stringSetElements").collectionType(CollectionType.SET)
                .propertyAccessStrategy(BUILDER_COLLECTION_AND_SINGLE_ELEMENT).build())
            .builderSingleAddMethodName("stringSetElement").build(),
        BuilderMetadata.builder()
            .delegateMetadata(STRINGS_LETTER.metadataBuilder("stringSortedSetElements")
                .collectionType(CollectionType.SORTED_SET)
                .propertyAccessStrategy(BUILDER_COLLECTION_AND_SINGLE_ELEMENT).build())
            .builderSingleAddMethodName("stringSortedSetElement").build(),
        BuilderMetadata.builder()
            .delegateMetadata(STRINGS_LETTER.metadataBuilder("stringCollection")
                .collectionType(CollectionType.COLLECTION).propertyAccessStrategy(BUILDER_DIRECT).build())
            .build()

    );

    public static final List<PropertyMetadata> METADATA_COLLECTION_ONLY = immutableList(
        BuilderMetadata.builder()
            .delegateMetadata(STRINGS_LETTER.metadataBuilder("stringSetElements")
                .collectionType(CollectionType.SET).propertyAccessStrategy(BUILDER_DIRECT).build())
            .build(),
        BuilderMetadata.builder()
            .delegateMetadata(STRINGS_LETTER.metadataBuilder("stringSortedSetElements")
                .collectionType(CollectionType.SORTED_SET).propertyAccessStrategy(BUILDER_DIRECT).build())
            .build(),
        BuilderMetadata.builder()
            .delegateMetadata(STRINGS_LETTER.metadataBuilder("stringCollection")
                .collectionType(CollectionType.COLLECTION).propertyAccessStrategy(BUILDER_DIRECT).build())
            .build()

    );

    public static final List<PropertyMetadata> METADATA_SORTED_SET_ONLY = immutableList(
        BuilderMetadata.builder()
            .delegateMetadata(STRINGS_LETTER.metadataBuilder("stringSortedSetElements")
                .collectionType(CollectionType.SORTED_SET).propertyAccessStrategy(BUILDER_DIRECT).build())
            .build()

    );

}
