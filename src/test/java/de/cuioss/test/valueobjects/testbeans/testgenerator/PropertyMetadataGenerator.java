package de.cuioss.test.valueobjects.testbeans.testgenerator;

import static de.cuioss.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator.ATTRIBUTE_ACCESS_STRATEGY;
import static de.cuioss.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator.ATTRIBUTE_COLLECTION_WRAPPER;
import static de.cuioss.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator.ATTRIBUTE_DEFAULT_VALUE;
import static de.cuioss.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator.ATTRIBUTE_MEMBER_INFO;
import static de.cuioss.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator.ATTRIBUTE_NAME;
import static de.cuioss.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator.ATTRIBUTE_REQUIRED;
import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.generator.internal.net.java.quickcheck.Generator;
import de.cuioss.test.generator.internal.net.java.quickcheck.generator.support.FixedValuesGenerator;
import de.cuioss.test.valueobjects.property.PropertyMetadata;

@SuppressWarnings("javadoc")
public class PropertyMetadataGenerator implements TypedGenerator<PropertyMetadata> {

    private static final Generator<PropertyMetadataTestDataGenerator> GENERATOR_GENERATOR = new FixedValuesGenerator<>(
            immutableList(ATTRIBUTE_NAME, ATTRIBUTE_DEFAULT_VALUE, ATTRIBUTE_REQUIRED, ATTRIBUTE_ACCESS_STRATEGY,
                    ATTRIBUTE_COLLECTION_WRAPPER, ATTRIBUTE_MEMBER_INFO));

    @Override
    public PropertyMetadata next() {
        return GENERATOR_GENERATOR.next().build();
    }

    @Override
    public Class<PropertyMetadata> getType() {
        return PropertyMetadata.class;
    }
}
