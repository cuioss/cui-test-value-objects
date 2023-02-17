package io.cui.test.valueobjects.junit5;

import static org.junit.jupiter.api.Assertions.fail;

import io.cui.test.valueobjects.ValueObjectTest;
import io.cui.test.valueobjects.junit5.testbeans.ComplexBean;

class ValueObjectTestInlineInstantiatorTest extends ValueObjectTest<ComplexBean> {

    private ComplexBean any = null;

    @Override
    public void shouldImplementObjectContracts() {
        // Should Fail Without any
        try {
            super.shouldImplementObjectContracts();
            fail("Should have thrown AssertionError");
        } catch (final AssertionError e) {
            // Expected
        }

        any = new ComplexBean();
        super.shouldImplementObjectContracts();
    }

    @Override
    protected ComplexBean anyValueObject() {
        return any;
    }

}
