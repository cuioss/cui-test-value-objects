package de.cuioss.test.valueobjects.testbeans.copyconstructor;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

import java.io.Serializable;
import java.util.List;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.valueobjects.api.TestContract;
import de.cuioss.test.valueobjects.api.contracts.VerifyCopyConstructor;
import de.cuioss.test.valueobjects.contract.MockTestContract;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.impl.ConstructorBasedInstantiator;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
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

    public static final PropertyMetadata ATTRIBUTE = PropertyMetadataImpl.builder().name("attribute")
            .generator(Generators.nonEmptyStrings()).required(true).propertyClass(String.class).build();

    public static final List<PropertyMetadata> ATTRIBUTE_LIST = immutableList(ATTRIBUTE);

    public static final RuntimeProperties RUNTIME_PROPERTIES = new RuntimeProperties(ATTRIBUTE_LIST);

    public static final TestContract<OneRequiredFieldCopyConstructor> MOCK_INSTANTIATOR_CONTRACT = new MockTestContract<>(
            new ConstructorBasedInstantiator<>(OneRequiredFieldCopyConstructor.class, RUNTIME_PROPERTIES));

    @Getter
    @NonNull
    private final String attribute;

    public OneRequiredFieldCopyConstructor(final OneRequiredFieldCopyConstructor copyFrom) {
        this(copyFrom.attribute);
    }
}
