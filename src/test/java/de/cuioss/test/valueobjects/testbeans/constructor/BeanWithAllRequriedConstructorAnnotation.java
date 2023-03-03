package de.cuioss.test.valueobjects.testbeans.constructor;

import de.cuioss.test.valueobjects.api.contracts.VerifyConstructor;
import de.cuioss.test.valueobjects.property.util.PropertyAccessStrategy;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author Oliver Wolff
 */
@VerifyConstructor(of = { "name", "propertyAccessStrategy" }, allRequired = true)
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class BeanWithAllRequriedConstructorAnnotation {

    @Getter
    @NonNull
    private final String name;

    @Getter
    @NonNull
    private final PropertyAccessStrategy propertyAccessStrategy;
}
