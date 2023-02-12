package io.cui.test.valueobjects.testbeans.constructor;

import io.cui.test.valueobjects.api.contracts.VerifyConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author Oliver Wolff
 */
@VerifyConstructor(of = "name")
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class BeanWithOneConstructorAnnotation {

    @Getter
    private final String name;
}
