package de.cuioss.test.valueobjects.testbeans.constructor;

import java.io.Serializable;

import de.cuioss.test.valueobjects.api.contracts.VerifyConstructor;
import de.cuioss.tools.property.PropertyMemberInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@SuppressWarnings("javadoc")
@VerifyConstructor(of = "name")
@VerifyConstructor(of = { "name", "propertyMemberInfo" })
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class BeanWithMultipleConstructorAnnotation implements Serializable {

    private static final long serialVersionUID = 1140137211367940204L;

    @Getter
    private final String name;

    @Getter
    private PropertyMemberInfo propertyMemberInfo;

    public BeanWithMultipleConstructorAnnotation(final String name,
            final PropertyMemberInfo propertyMemberInfo) {
        this(name);
        this.propertyMemberInfo = propertyMemberInfo;
    }
}
