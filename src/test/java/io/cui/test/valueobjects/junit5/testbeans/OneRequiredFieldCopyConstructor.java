package io.cui.test.valueobjects.junit5.testbeans;

import static io.cui.tools.collect.CollectionLiterals.immutableList;

import java.io.Serializable;
import java.util.List;

import io.cui.test.generator.Generators;
import io.cui.test.valueobjects.api.TestContract;
import io.cui.test.valueobjects.api.contracts.VerifyCopyConstructor;
import io.cui.test.valueobjects.objects.RuntimeProperties;
import io.cui.test.valueobjects.objects.impl.ConstructorBasedInstantiator;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.impl.PropertyMetadataImpl;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@SuppressWarnings("javadoc")
@RequiredArgsConstructor
@VerifyCopyConstructor
@EqualsAndHashCode
@ToString
public class OneRequiredFieldCopyConstructor implements Serializable {

    private static final long serialVersionUID = 741300814716513465L;

    public static final PropertyMetadata ATTRIBUTE =
        PropertyMetadataImpl.builder().name("attribute")
                .generator(Generators.nonEmptyStrings())
                .required(true).propertyClass(String.class)
                .build();

    public static final List<PropertyMetadata> ATTRIBUTE_LIST = immutableList(ATTRIBUTE);

    public static final RuntimeProperties RUNTIME_PROPERTIES =
        new RuntimeProperties(ATTRIBUTE_LIST);

    public static final TestContract<OneRequiredFieldCopyConstructor> MOCK_INSTANTIATOR_CONTRACT =
        new MockTestContract<>(new ConstructorBasedInstantiator<>(
                OneRequiredFieldCopyConstructor.class, RUNTIME_PROPERTIES));

    @Getter
    @NonNull
    private final String attribute;

    public OneRequiredFieldCopyConstructor(final OneRequiredFieldCopyConstructor copyFrom) {
        this(copyFrom.attribute);
    }
}
