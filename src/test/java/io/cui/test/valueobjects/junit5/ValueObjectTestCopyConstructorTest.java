package io.cui.test.valueobjects.junit5;

import io.cui.test.valueobjects.api.contracts.VerifyConstructor;
import io.cui.test.valueobjects.api.contracts.VerifyCopyConstructor;
import io.cui.test.valueobjects.junit5.testbeans.OneRequiredFieldCopyConstructor;

@SuppressWarnings("javadoc")
@VerifyConstructor(of = "attribute",
        required = "attribute")
@VerifyCopyConstructor
public class ValueObjectTestCopyConstructorTest
        extends ValueObjectTest<OneRequiredFieldCopyConstructor> {

}
