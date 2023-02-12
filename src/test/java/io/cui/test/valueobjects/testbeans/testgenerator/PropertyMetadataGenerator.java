package io.cui.test.valueobjects.testbeans.testgenerator;

import static io.cui.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator.ATTRIBUTE_ACCESS_STRATEGY;
import static io.cui.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator.ATTRIBUTE_COLLECTION_WRAPPER;
import static io.cui.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator.ATTRIBUTE_DEFAULT_VALUE;
import static io.cui.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator.ATTRIBUTE_MEMBER_INFO;
import static io.cui.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator.ATTRIBUTE_NAME;
import static io.cui.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator.ATTRIBUTE_REQUIRED;
import static io.cui.tools.collect.CollectionLiterals.immutableList;

import io.cui.test.generator.TypedGenerator;
import io.cui.test.generator.internal.net.java.quickcheck.Generator;
import io.cui.test.generator.internal.net.java.quickcheck.generator.support.FixedValuesGenerator;
import io.cui.test.valueobjects.property.PropertyMetadata;

@SuppressWarnings("javadoc")
public class PropertyMetadataGenerator implements TypedGenerator<PropertyMetadata> {

    private static final Generator<PropertyMetadataTestDataGenerator> GENERATOR_GENERATOR =
        new FixedValuesGenerator<>(immutableList(ATTRIBUTE_NAME, ATTRIBUTE_DEFAULT_VALUE,
                ATTRIBUTE_REQUIRED, ATTRIBUTE_ACCESS_STRATEGY, ATTRIBUTE_COLLECTION_WRAPPER,
                ATTRIBUTE_MEMBER_INFO));

    @Override
    public PropertyMetadata next() {
        return GENERATOR_GENERATOR.next().build();
    }

    @Override
    public Class<PropertyMetadata> getType() {
        return PropertyMetadata.class;
    }
}
