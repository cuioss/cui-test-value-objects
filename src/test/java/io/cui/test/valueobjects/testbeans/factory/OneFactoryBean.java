package io.cui.test.valueobjects.testbeans.factory;

import static io.cui.tools.collect.CollectionLiterals.immutableList;

import io.cui.test.generator.Generators;
import io.cui.test.valueobjects.api.contracts.VerifyFactoryMethod;
import io.cui.test.valueobjects.objects.RuntimeProperties;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.impl.PropertyMetadataImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("javadoc")
@VerifyFactoryMethod(factoryMethodName = "create",
        of = "attribute",
        required = "attribute")
@RequiredArgsConstructor
public class OneFactoryBean {

    public static final PropertyMetadata ATTRIBUTE =
        PropertyMetadataImpl.builder().name("attribute").propertyClass(String.class).required(true)
                .generator(Generators.nonEmptyStrings()).build();

    public static final RuntimeProperties INFORMATION =
        new RuntimeProperties(immutableList(ATTRIBUTE));

    public static final String CREATE_METHOD_NAME = "create";

    @Getter
    private final String attribute;

    public static OneFactoryBean create(String attribute) {
        return new OneFactoryBean(attribute);
    }
}
