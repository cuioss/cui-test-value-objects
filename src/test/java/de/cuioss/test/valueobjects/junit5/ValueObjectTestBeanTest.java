package de.cuioss.test.valueobjects.junit5;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.ValueObjectTest;
import de.cuioss.test.valueobjects.api.contracts.VerifyBeanProperty;
import de.cuioss.test.valueobjects.api.object.ObjectTestConfig;
import de.cuioss.test.valueobjects.junit5.testbeans.ComplexBean;

@VerifyBeanProperty(exclude = "badstring", defaultValued = "stringWithDefault")
@ObjectTestConfig(equalsAndHashCodeExclude = "noObjectIdentitiyString")
class ValueObjectTestBeanTest extends ValueObjectTest<ComplexBean> {

    @Test
    void shouldProvideAny() {
        assertNotNull(super.anyValueObject());
    }
}
