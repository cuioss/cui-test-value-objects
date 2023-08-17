package de.cuioss.test.valueobjects.junit5;

import de.cuioss.test.valueobjects.ValueObjectTest;
import de.cuioss.test.valueobjects.api.contracts.VerifyConstructor;
import de.cuioss.test.valueobjects.junit5.testbeans.BeanWithMultipleConstructorAnnotation;

@SuppressWarnings("javadoc")
@VerifyConstructor(of = "name")
@VerifyConstructor(of = { "name", "propertyMemberInfo" })
public class ValueObjectTestConstructorTest extends ValueObjectTest<BeanWithMultipleConstructorAnnotation> {

}
