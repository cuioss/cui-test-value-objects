package io.cui.test.valueobjects.testbeans.constructor;

import io.cui.test.valueobjects.api.contracts.VerifyConstructor;
import io.cui.test.valueobjects.property.util.PropertyAccessStrategy;
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
