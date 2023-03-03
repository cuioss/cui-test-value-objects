package de.cuioss.test.valueobjects.testbeans.factory;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.valueobjects.api.contracts.VerifyFactoryMethod;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
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
