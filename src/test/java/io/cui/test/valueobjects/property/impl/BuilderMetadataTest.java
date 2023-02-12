package io.cui.test.valueobjects.property.impl;

import static io.cui.test.valueobjects.property.impl.BuilderMetadata.prefixBuilderMethod;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import io.cui.test.generator.Generators;
import io.cui.test.valueobjects.AbstractValueObjectTest;
import io.cui.test.valueobjects.api.contracts.VerifyBuilder;
import io.cui.test.valueobjects.api.generator.PropertyGeneratorHint;
import io.cui.test.valueobjects.api.object.ObjectTestContracts;
import io.cui.test.valueobjects.api.object.VetoObjectTestContract;
import io.cui.test.valueobjects.api.property.PropertyConfig;
import io.cui.test.valueobjects.api.property.PropertyReflectionConfig;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.util.PropertyAccessStrategy;
import io.cui.tools.property.PropertyReadWrite;

@PropertyReflectionConfig(skip = true)
@PropertyGeneratorHint(declaredType = PropertyMetadata.class, implementationType = PropertyMetadataImpl.class)
@PropertyConfig(name = "delegateMetadata", propertyReadWrite = PropertyReadWrite.WRITE_ONLY,
        propertyClass = PropertyMetadata.class)
@VerifyBuilder(of = "delegateMetadata", required = "delegateMetadata")
@VetoObjectTestContract(ObjectTestContracts.SERIALIZABLE)
class BuilderMetadataTest extends AbstractValueObjectTest<BuilderMetadata> {

    private static final String PROPERTY_NAME = "propertyName";

    private static final PropertyMetadata STRING_METADATA = PropertyMetadataImpl.builder().name(PROPERTY_NAME)
            .generator(Generators.strings()).propertyAccessStrategy(PropertyAccessStrategy.BUILDER_DIRECT).build();

    private static final String PROPERTY_NAME_SHORT = "a";

    private static final String BUILDER_PREFIX = "with";

    private static final String TARGET_NAME_SHORT = "withA";

    private static final String TARGET_NAME_LONG = "withPropertyName";

    private static final String SINGLE_ADD_METHOD_NAME = "singleAddMethodName";

    private static final String BUILDER_METHOD_NAME = "builderMethodName";

    @Test
    void shouldPrefixBuilderMethod() {
        assertEquals(TARGET_NAME_SHORT, prefixBuilderMethod(PROPERTY_NAME_SHORT, BUILDER_PREFIX));
        assertEquals(PROPERTY_NAME_SHORT, prefixBuilderMethod(PROPERTY_NAME_SHORT, null));
        assertEquals(PROPERTY_NAME_SHORT, prefixBuilderMethod(PROPERTY_NAME_SHORT, ""));
        assertEquals(PROPERTY_NAME, prefixBuilderMethod(PROPERTY_NAME, null));
    }

    @Test
    void shouldFailToPrefixOnNullName() {
        assertThrows(NullPointerException.class, () -> prefixBuilderMethod(null, null));
    }

    @Test
    void shouldFailToPrefixOnEmptyName() {
        assertThrows(NullPointerException.class, () -> prefixBuilderMethod("", null));
    }

    @Test
    void shouldBuildMinimal() {
        final var metadata = BuilderMetadata.builder().delegateMetadata(STRING_METADATA).build();
        assertNotNull(metadata);
        assertEquals(PROPERTY_NAME, metadata.getName());
        assertEquals(PROPERTY_NAME, metadata.getBuilderAddMethodName());
        assertEquals(PROPERTY_NAME, metadata.getBuilderSingleAddMethodName());
        assertEquals(PropertyAccessStrategy.BUILDER_DIRECT, metadata.getPropertyAccessStrategy());
    }

    @Test
    void shouldPrefixMethod() {
        final var metadata =
            BuilderMetadata.builder().delegateMetadata(STRING_METADATA).builderMethodPrefix(BUILDER_PREFIX).build();
        assertNotNull(metadata);
        assertEquals(PROPERTY_NAME, metadata.getName());
        assertEquals(TARGET_NAME_LONG, metadata.getBuilderAddMethodName());
        assertEquals(TARGET_NAME_LONG, metadata.getBuilderSingleAddMethodName());
    }

    @Test
    void shouldUseFixedMethods() {
        final var metadata = BuilderMetadata.builder().delegateMetadata(STRING_METADATA)
                .builderMethodName(BUILDER_METHOD_NAME).builderSingleAddMethodName(SINGLE_ADD_METHOD_NAME).build();
        assertNotNull(metadata);
        assertEquals(BUILDER_METHOD_NAME, metadata.getBuilderAddMethodName());
        assertEquals(SINGLE_ADD_METHOD_NAME, metadata.getBuilderSingleAddMethodName());
    }
}
