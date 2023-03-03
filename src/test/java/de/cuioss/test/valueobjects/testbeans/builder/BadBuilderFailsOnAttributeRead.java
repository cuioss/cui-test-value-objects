package de.cuioss.test.valueobjects.testbeans.builder;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

import java.util.List;

import de.cuioss.test.valueobjects.generator.JavaTypesGenerator;
import de.cuioss.test.valueobjects.property.PropertyMetadata;

@SuppressWarnings("javadoc")
public class BadBuilderFailsOnAttributeRead {

    public String getAttributeName() {
        throw new IllegalStateException("Bad boy");
    }

    public static class BadBuilderFailsOnAttributeReadBuilder {

        @SuppressWarnings("unused")
        public BadBuilderFailsOnAttributeReadBuilder attributeName(
                final String attribute) {
            return this;
        }

        public BadBuilderFailsOnAttributeRead build() {
            return new BadBuilderFailsOnAttributeRead();
        }
    }

    public static BadBuilderFailsOnAttributeReadBuilder builder() {
        return new BadBuilderFailsOnAttributeReadBuilder();
    }

    public static final List<PropertyMetadata> METADATA =
        immutableList(JavaTypesGenerator.STRINGS.metadata("attributeName"));

}
