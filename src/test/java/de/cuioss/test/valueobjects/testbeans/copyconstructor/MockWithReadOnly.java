package de.cuioss.test.valueobjects.testbeans.copyconstructor;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static java.util.Objects.requireNonNull;

import java.util.List;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.valueobjects.api.TestContract;
import de.cuioss.test.valueobjects.api.contracts.VerifyCopyConstructor;
import de.cuioss.test.valueobjects.contract.MockTestContract;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.impl.BeanInstantiator;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import de.cuioss.tools.property.PropertyReadWrite;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("javadoc")
@VerifyCopyConstructor
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class MockWithReadOnly {

    public static final PropertyMetadata READWRITE =
        PropertyMetadataImpl.builder().name("readWrite")
                .generator(Generators.nonEmptyStrings())
                .required(true).propertyClass(String.class)
                .build();

    public static final PropertyMetadata READONLY =
        PropertyMetadataImpl.builder().name("readOnly")
                .generator(Generators.nonEmptyStrings())
                .required(true).propertyClass(String.class)
                .propertyReadWrite(PropertyReadWrite.READ_ONLY)
                .build();

    public static final List<PropertyMetadata> ATTRIBUTE_LIST = immutableList(READWRITE, READONLY);

    public static final RuntimeProperties RUNTIME_PROPERTIES =
        new RuntimeProperties(ATTRIBUTE_LIST);

    public static final TestContract<MockWithReadOnly> MOCK_INSTANTIATOR_CONTRACT =
        new MockTestContract<>(new BeanInstantiator<>(new DefaultInstantiator<>(MockWithReadOnly.class),
                RUNTIME_PROPERTIES));

    @Getter
    @Setter
    private String readWrite;

    @Getter
    private String readOnly;

    public MockWithReadOnly(final MockWithReadOnly copyFrom) {
        requireNonNull(copyFrom);
        readWrite = copyFrom.getReadWrite();
        readOnly = copyFrom.getReadOnly();
    }

}
