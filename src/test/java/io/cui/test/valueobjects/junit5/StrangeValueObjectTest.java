package io.cui.test.valueobjects.junit5;

import io.cui.test.valueobjects.api.contracts.VerifyBeanProperty;
import io.cui.test.valueobjects.api.contracts.VerifyCopyConstructor;
import io.cui.test.valueobjects.junit5.testbeans.StrangeInterface;
import io.cui.test.valueobjects.junit5.testbeans.StrangeObject;

@SuppressWarnings("javadoc")
@VerifyBeanProperty(exclude = "unknownObject")
@VerifyCopyConstructor(argumentType = StrangeInterface.class, exclude = "property", useObjectEquals = false)
class StrangeValueObjectTest extends ValueObjectTest<StrangeObject> {

}
