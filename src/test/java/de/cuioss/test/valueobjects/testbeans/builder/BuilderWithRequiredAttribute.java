package de.cuioss.test.valueobjects.testbeans.builder;

import static de.cuioss.test.valueobjects.generator.JavaTypesGenerator.STRINGS_LETTER;
import static de.cuioss.tools.collect.CollectionLiterals.immutableSortedSet;

import java.io.Serializable;
import java.util.SortedSet;

import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.BuilderMetadata;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@SuppressWarnings("javadoc")
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class BuilderWithRequiredAttribute implements Serializable {

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

    public static final SortedSet<PropertyMetadata> METADATA_COMPLETE =
        immutableSortedSet(
                BuilderMetadata.builder().delegateMetadata(
                        STRINGS_LETTER.metadataBuilder("requiredString").required(true)
                                .build())
                        .build());

    public static final SortedSet<PropertyMetadata> METADATA_INVALID =
        immutableSortedSet(
                BuilderMetadata.builder().delegateMetadata(
                        STRINGS_LETTER.metadata("requiredString"))
                        .build());

}
