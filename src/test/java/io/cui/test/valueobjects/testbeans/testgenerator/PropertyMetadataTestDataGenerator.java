package io.cui.test.valueobjects.testbeans.testgenerator;

import static io.cui.test.valueobjects.property.util.PropertyAccessStrategy.BUILDER_DIRECT;
import static io.cui.tools.collect.CollectionLiterals.mutableList;

import java.util.List;

import io.cui.test.generator.Generators;
import io.cui.test.generator.TypedGenerator;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.impl.PropertyMetadataImpl;
import io.cui.test.valueobjects.property.impl.PropertyMetadataImpl.PropertyMetadataBuilder;
import io.cui.test.valueobjects.property.util.CollectionType;
import io.cui.test.valueobjects.property.util.PropertyAccessStrategy;
import io.cui.tools.property.PropertyMemberInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("javadoc")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertyMetadataTestDataGenerator {

    /**
     * Creates an instance of of {@link PropertyMetadata} for the field
     * {@link PropertyMetadata#getName()}.
     */
    public static final PropertyMetadataTestDataGenerator ATTRIBUTE_NAME =
        new PropertyMetadataTestDataGenerator("name",
                Generators.letterStrings(1, 12), false);

    /**
     * Creates an instance of of {@link PropertyMetadata} for the field
     * {@link PropertyMetadata#isDefaultValue()}.
     */
    public static final PropertyMetadataTestDataGenerator ATTRIBUTE_DEFAULT_VALUE =
        new PropertyMetadataTestDataGenerator("defaultValue",
                Generators.booleans(), true);

    /**
     * Creates an instance of of {@link PropertyMetadata} for the field
     * {@link PropertyMetadata#isRequired()}.
     */
    public static final PropertyMetadataTestDataGenerator ATTRIBUTE_REQUIRED =
        new PropertyMetadataTestDataGenerator("required",
                Generators.booleans(), true);

    /**
     * Creates an instance of of {@link PropertyMetadata} for the field
     * {@link PropertyMetadata#getPropertyCreateStrategy()}.
     */
    public static final PropertyMetadataTestDataGenerator ATTRIBUTE_ACCESS_STRATEGY =
        new PropertyMetadataTestDataGenerator("propertyAccessStrategy",
                Generators.enumValues(PropertyAccessStrategy.class),
                true);

    /**
     * Creates an instance of of {@link PropertyMetadata} for the field
     * {@link PropertyMetadata#getCollectionType()}.
     */
    public static final PropertyMetadataTestDataGenerator ATTRIBUTE_COLLECTION_WRAPPER =
        new PropertyMetadataTestDataGenerator("collectionType",
                Generators.enumValues(CollectionType.class),
                false);

    /**
     * Creates an instance of of {@link PropertyMetadata} for the field
     * {@link PropertyMetadata#getPropertyCreateStrategy()}.
     */
    public static final PropertyMetadataTestDataGenerator ATTRIBUTE_MEMBER_INFO =
        new PropertyMetadataTestDataGenerator("propertyMemberInfo",
                Generators.enumValues(PropertyMemberInfo.class),
                true);

    /**
     * Creates an instance of of {@link PropertyMetadata} for the field
     * {@link PropertyMetadata#getGenerator()} for string-generator.
     */
    public static final PropertyMetadataTestDataGenerator STRING_GENERATOR_GENERATOR =
        new PropertyMetadataTestDataGenerator("generator",
                Generators.fixedValues(TypedGenerator.class, Generators.booleanObjects(), Generators.serializables(),
                        Generators.strings()),
                false);

    /**
     * Creates an instance of of {@link PropertyMetadata} for the field
     * {@link PropertyMetadata#getPropertyClass()} for string.
     */
    public static final PropertyMetadataTestDataGenerator STRING_CLASS_GENERATOR =
        new PropertyMetadataTestDataGenerator("propertyClass",
                Generators.classTypes(),
                false);

    /**
     * A list of valid metadata for creating {@link PropertyMetadata} ->
     * {@link PropertyMetadataBuilder}
     */
    public static final List<PropertyMetadata> COMPLETE_VALID_ATTRIBUTES =
        mutableList(
                ATTRIBUTE_DEFAULT_VALUE.generatorBuilder()
                        .propertyAccessStrategy(BUILDER_DIRECT)
                        .build(),
                STRING_GENERATOR_GENERATOR.generatorBuilder()
                        .propertyAccessStrategy(BUILDER_DIRECT)
                        .required(true)
                        .build(),
                ATTRIBUTE_NAME.generatorBuilder()
                        .propertyAccessStrategy(BUILDER_DIRECT)
                        .required(true)
                        .build(),
                STRING_CLASS_GENERATOR.generatorBuilder()
                        .propertyAccessStrategy(BUILDER_DIRECT)
                        .required(false)
                        .build(),
                ATTRIBUTE_ACCESS_STRATEGY.generatorBuilder()
                        .propertyAccessStrategy(BUILDER_DIRECT)
                        .build(),
                ATTRIBUTE_MEMBER_INFO.generatorBuilder()
                        .propertyAccessStrategy(BUILDER_DIRECT)
                        .build(),
                ATTRIBUTE_COLLECTION_WRAPPER.generatorBuilder()
                        .propertyAccessStrategy(BUILDER_DIRECT).build(),
                ATTRIBUTE_REQUIRED.generatorBuilder()
                        .propertyAccessStrategy(BUILDER_DIRECT)
                        .build());

    /**
     * A list of metadata for creating {@link PropertyMetadata} ->
     * {@link PropertyMetadataBuilder} that mark the #ATTRIBUTE_DEFAULT_VALUE as required
     */
    public static final List<PropertyMetadata> INVALID_ATTRIBUTES_REGARDING_REQUIRED =
        mutableList(
                ATTRIBUTE_DEFAULT_VALUE.generatorBuilder().propertyAccessStrategy(BUILDER_DIRECT)
                        .build(),
                STRING_GENERATOR_GENERATOR.generatorBuilder().propertyAccessStrategy(BUILDER_DIRECT)
                        .required(true)
                        .build(),
                ATTRIBUTE_NAME.generatorBuilder().propertyAccessStrategy(BUILDER_DIRECT)
                        .required(true)
                        .build(),
                STRING_CLASS_GENERATOR.generatorBuilder().propertyAccessStrategy(BUILDER_DIRECT)
                        .required(true)
                        .build(),
                ATTRIBUTE_ACCESS_STRATEGY.generatorBuilder().propertyAccessStrategy(BUILDER_DIRECT)
                        .build(),
                ATTRIBUTE_MEMBER_INFO.generatorBuilder().propertyAccessStrategy(BUILDER_DIRECT)
                        .build(),
                ATTRIBUTE_COLLECTION_WRAPPER.generatorBuilder()
                        .propertyAccessStrategy(BUILDER_DIRECT).required(true).build(),
                ATTRIBUTE_REQUIRED.generatorBuilder().propertyAccessStrategy(BUILDER_DIRECT)
                        .build());

    private final String propertyName;

    /** The actual generator. */
    private final TypedGenerator<?> generator;

    /**
     * used for primitive types that have always a default, for all
     * other types this is <code>null</code>
     */
    private final boolean defaultValue;

    /**
     * Creates a configured instance of {@link PropertyMetadata}
     *
     * @param name of the property the {@link PropertyMetadata} is related to.
     * @return {@link PropertyMetadata} with the given name as
     *         {@link PropertyMetadata#getName()}, the corresponding
     *         {@link TypedGenerator} and {@link PropertyMetadata#getPropertyClass()}
     */
    public PropertyMetadata build() {
        return generatorBuilder().build();
    }

    /**
     * Creates pre-configured instances of {@link PropertyMetadataBuilder}
     *
     * @return {@link PropertyMetadataBuilder} with the given name as
     *         {@link PropertyMetadata#getName()}, the corresponding
     *         {@link TypedGenerator} and {@link PropertyMetadata#getPropertyClass()}
     */
    public PropertyMetadataBuilder generatorBuilder() {
        return PropertyMetadataImpl.builder().name(propertyName)
                .generator(generator).defaultValue(defaultValue);
    }

}
