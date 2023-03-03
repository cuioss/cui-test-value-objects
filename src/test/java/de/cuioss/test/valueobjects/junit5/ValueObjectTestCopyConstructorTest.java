package de.cuioss.test.valueobjects.junit5;

import de.cuioss.test.valueobjects.ValueObjectTest;
import de.cuioss.test.valueobjects.api.contracts.VerifyConstructor;
import de.cuioss.test.valueobjects.api.contracts.VerifyCopyConstructor;
import de.cuioss.test.valueobjects.junit5.testbeans.OneRequiredFieldCopyConstructor;

@SuppressWarnings("javadoc")
@VerifyConstructor(of = "attribute",
        required = "attribute")
@VerifyCopyConstructor
public class ValueObjectTestCopyConstructorTest
        extends ValueObjectTest<OneRequiredFieldCopyConstructor> {

}
