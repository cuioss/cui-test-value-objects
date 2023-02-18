package io.cui.test.valueobjects.testbeans.builder;

import static io.cui.test.valueobjects.generator.JavaTypesGenerator.STRINGS_LETTER;
import static io.cui.test.valueobjects.property.util.PropertyAccessStrategy.BUILDER_COLLECTION_AND_SINGLE_ELEMENT;
import static io.cui.test.valueobjects.property.util.PropertyAccessStrategy.BUILDER_DIRECT;
import static io.cui.tools.collect.CollectionLiterals.immutableList;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.impl.BuilderMetadata;
import io.cui.test.valueobjects.property.util.CollectionType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@SuppressWarnings("javadoc")
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class BuilderWithCollections implements Serializable {

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

        public BuilderWithCollectionsBuilder stringSetElements(
                final Set<String> elementSet) {
            stringSetElements.addAll(elementSet);
            return this;
        }

        public BuilderWithCollectionsBuilder stringSetElement(
                final String element) {
            stringSetElements.add(element);
            return this;
        }

        public BuilderWithCollectionsBuilder stringSortedSetElements(
                final SortedSet<String> elementSortedSet) {
            stringSortedSetElements.addAll(elementSortedSet);
            return this;
        }

        public BuilderWithCollectionsBuilder stringSortedSetElement(
                final String element) {
            stringSortedSetElements.add(element);
            return this;
        }

        public BuilderWithCollectionsBuilder stringCollection(
                final Collection<String> elementCollection) {
            stringCollection = elementCollection;
            return this;
        }

        public BuilderWithCollections build() {
            return new BuilderWithCollections(stringSetElements, stringSortedSetElements,
                    stringCollection);

        }
    }

    public static BuilderWithCollectionsBuilder builder() {
        return new BuilderWithCollectionsBuilder();
    }

    public static final List<PropertyMetadata> METADATA_COMPLETE =
        immutableList(
                BuilderMetadata.builder().delegateMetadata(
                        STRINGS_LETTER.metadataBuilder("stringSetElements")
                                .collectionType(CollectionType.SET)
                                .propertyAccessStrategy(BUILDER_COLLECTION_AND_SINGLE_ELEMENT)
                                .build())
                        .builderSingleAddMethodName("stringSetElement").build(),
                BuilderMetadata.builder().delegateMetadata(
                        STRINGS_LETTER.metadataBuilder("stringSortedSetElements")
                                .collectionType(CollectionType.SORTED_SET)
                                .propertyAccessStrategy(BUILDER_COLLECTION_AND_SINGLE_ELEMENT)
                                .build())
                        .builderSingleAddMethodName("stringSortedSetElement").build(),
                BuilderMetadata.builder().delegateMetadata(
                        STRINGS_LETTER.metadataBuilder("stringCollection")
                                .collectionType(CollectionType.COLLECTION)
                                .propertyAccessStrategy(BUILDER_DIRECT)
                                .build())
                        .build()

        );

    public static final List<PropertyMetadata> METADATA_COLLECTION_ONLY =
        immutableList(
                BuilderMetadata.builder().delegateMetadata(
                        STRINGS_LETTER.metadataBuilder("stringSetElements")
                                .collectionType(CollectionType.SET)
                                .propertyAccessStrategy(BUILDER_DIRECT)
                                .build())
                        .build(),
                BuilderMetadata.builder().delegateMetadata(
                        STRINGS_LETTER.metadataBuilder("stringSortedSetElements")
                                .collectionType(CollectionType.SORTED_SET)
                                .propertyAccessStrategy(BUILDER_DIRECT)
                                .build())
                        .build(),
                BuilderMetadata.builder().delegateMetadata(
                        STRINGS_LETTER.metadataBuilder("stringCollection")
                                .collectionType(CollectionType.COLLECTION)
                                .propertyAccessStrategy(BUILDER_DIRECT)
                                .build())
                        .build()

        );

    public static final List<PropertyMetadata> METADATA_SORTED_SET_ONLY =
        immutableList(
                BuilderMetadata.builder().delegateMetadata(
                        STRINGS_LETTER.metadataBuilder("stringSortedSetElements")
                                .collectionType(CollectionType.SORTED_SET)
                                .propertyAccessStrategy(BUILDER_DIRECT)
                                .build())
                        .build()

        );

}