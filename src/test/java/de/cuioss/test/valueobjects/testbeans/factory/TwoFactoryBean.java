package de.cuioss.test.valueobjects.testbeans.factory;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

import java.io.Serializable;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.valueobjects.api.contracts.VerifyFactoryMethod;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("javadoc")
@VerifyFactoryMethod(factoryMethodName = "create", of = "attribute")
@VerifyFactoryMethod(factoryMethodName = "create", of = {})
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class TwoFactoryBean implements Serializable {

    private static final long serialVersionUID = 4539639907656084561L;

    public static final PropertyMetadata ATTRIBUTE = PropertyMetadataImpl.builder().name("attribute")
            .propertyClass(String.class).generator(Generators.nonEmptyStrings()).build();

    public static final RuntimeProperties INFORMATION = new RuntimeProperties(immutableList(ATTRIBUTE));

    public static final String CREATE_METHOD_NAME = "create";

    @Getter
    private String attribute;

    public TwoFactoryBean(String attribute) {
        super();
        this.attribute = attribute;
    }

    public static TwoFactoryBean create() {
        return new TwoFactoryBean();
    }

    public static TwoFactoryBean create(String attribute) {
        return new TwoFactoryBean(attribute);
    }
}
