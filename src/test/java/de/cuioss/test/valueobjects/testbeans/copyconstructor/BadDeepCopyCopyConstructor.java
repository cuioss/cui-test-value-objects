package de.cuioss.test.valueobjects.testbeans.copyconstructor;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

import java.io.Serializable;
import java.time.LocalDateTime;
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
@VerifyCopyConstructor(verifyDeepCopy = true)
@EqualsAndHashCode
@ToString
public class BadDeepCopyCopyConstructor implements Serializable {

    private static final long serialVersionUID = 741300814716513465L;

    public static final PropertyMetadata ATTRIBUTE =
        PropertyMetadataImpl.builder().name("attribute")
                .generator(Generators.nonEmptyStrings())
                .required(true).propertyClass(String.class)
                .build();

    public static final PropertyMetadata DATE =
        PropertyMetadataImpl.builder().name("date")
                .generator(Generators.localDateTimes())
                .required(true).propertyClass(LocalDateTime.class)
                .build();

    public static final List<PropertyMetadata> ATTRIBUTE_LIST = immutableList(ATTRIBUTE, DATE);

    public static final RuntimeProperties RUNTIME_PROPERTIES =
        new RuntimeProperties(ATTRIBUTE_LIST);

    public static final TestContract<BadDeepCopyCopyConstructor> MOCK_INSTANTIATOR_CONTRACT =
        new MockTestContract<>(new ConstructorBasedInstantiator<>(
                BadDeepCopyCopyConstructor.class, RUNTIME_PROPERTIES));

    @Getter
    @NonNull
    private final String attribute;

    @Getter
    @NonNull
    private final LocalDateTime date;

    public BadDeepCopyCopyConstructor(final BadDeepCopyCopyConstructor copyFrom) {
        this(copyFrom.attribute, LocalDateTime.now());
    }
}
