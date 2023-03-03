package de.cuioss.test.valueobjects.junit5;

import static org.junit.jupiter.api.Assertions.assertTrue;

import de.cuioss.test.valueobjects.ValueObjectTest;
import de.cuioss.test.valueobjects.junit5.testbeans.ComplexBean;

class ValueObjectTestInlineInstantiatorTest extends ValueObjectTest<ComplexBean> {

    private ComplexBean any = null;

    @Override
    public void shouldImplementObjectContracts() {
        // Should Fail Without any
        var assertIsCorrect = true;
        try {
            super.shouldImplementObjectContracts();
            assertIsCorrect = false;
        } catch (final AssertionError e) {
            // Expected
        }
        assertTrue(assertIsCorrect, "Should have thrown AssertionError");
        any = new ComplexBean();
        super.shouldImplementObjectContracts();
    }

    @Override
    protected ComplexBean anyValueObject() {
        return any;
    }

}
