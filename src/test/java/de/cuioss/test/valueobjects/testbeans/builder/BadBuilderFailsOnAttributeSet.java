package de.cuioss.test.valueobjects.testbeans.builder;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

import java.util.List;

import de.cuioss.test.valueobjects.generator.JavaTypesGenerator;
import de.cuioss.test.valueobjects.property.PropertyMetadata;

@SuppressWarnings("javadoc")
public class BadBuilderFailsOnAttributeSet {

    public static class BadBuilderFailsOnAttributeSetBuilder {

        public BadBuilderFailsOnAttributeSetBuilder attributeName(final String attribute) {
            throw new IllegalStateException("Bad boy");
        }

        public BadBuilderFailsOnAttributeSet build() {
            return new BadBuilderFailsOnAttributeSet();
        }
    }

    public static BadBuilderFailsOnAttributeSetBuilder builder() {
        return new BadBuilderFailsOnAttributeSetBuilder();
    }

    public static final List<PropertyMetadata> METADATA = immutableList(
            JavaTypesGenerator.STRINGS.metadata("attributeName"));

}
