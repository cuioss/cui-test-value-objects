package io.cui.test.valueobjects.junit5;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.api.contracts.VerifyBeanProperty;
import io.cui.test.valueobjects.api.object.ObjectTestConfig;
import io.cui.test.valueobjects.junit5.testbeans.ComplexBean;

@VerifyBeanProperty(exclude = "badstring", defaultValued = "stringWithDefault")
@ObjectTestConfig(equalsAndHashCodeExclude = "noObjectIdentitiyString")
class ValueObjectTestBeanTest extends ValueObjectTest<ComplexBean> {

    @Test
    void shouldProvideAny() {
        assertNotNull(super.anyValueObject());
    }
}
