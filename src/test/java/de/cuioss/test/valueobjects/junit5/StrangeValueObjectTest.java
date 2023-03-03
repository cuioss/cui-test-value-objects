package de.cuioss.test.valueobjects.junit5;

import de.cuioss.test.valueobjects.ValueObjectTest;
import de.cuioss.test.valueobjects.api.contracts.VerifyBeanProperty;
import de.cuioss.test.valueobjects.api.contracts.VerifyCopyConstructor;
import de.cuioss.test.valueobjects.junit5.testbeans.StrangeInterface;
import de.cuioss.test.valueobjects.junit5.testbeans.StrangeObject;

@VerifyBeanProperty(exclude = "unknownObject")
@VerifyCopyConstructor(argumentType = StrangeInterface.class, exclude = "property", useObjectEquals = false)
class StrangeValueObjectTest extends ValueObjectTest<StrangeObject> {

}
