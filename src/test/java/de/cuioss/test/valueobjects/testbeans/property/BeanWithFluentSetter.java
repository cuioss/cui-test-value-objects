package de.cuioss.test.valueobjects.testbeans.property;

import java.io.Serializable;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import de.cuioss.tools.property.PropertyReadWrite;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@SuppressWarnings("javadoc")
@EqualsAndHashCode
@ToString
public class BeanWithFluentSetter implements Serializable {

    private static final long serialVersionUID = 4441858070683023193L;

    public static final PropertyMetadata METATDATA = PropertyMetadataImpl.builder().name("field")
            .propertyClass(String.class).generator(Generators.nonEmptyStrings())
            .propertyReadWrite(PropertyReadWrite.READ_WRITE).build();

    @Getter
    private String field;

    public BeanWithFluentSetter setField(String field) {
        this.field = field;
        return this;
    }

}
