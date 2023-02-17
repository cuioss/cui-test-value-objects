package io.cui.test.valueobjects.junit5;

import io.cui.test.valueobjects.ValueObjectTest;
import io.cui.test.valueobjects.api.contracts.VerifyConstructor;
import io.cui.test.valueobjects.junit5.testbeans.BeanWithMultipleConstructorAnnotation;

@SuppressWarnings("javadoc")
@VerifyConstructor(of = "name")
@VerifyConstructor(of = { "name", "propertyMemberInfo" })
public class ValueObjectTestConstructorTest
        extends ValueObjectTest<BeanWithMultipleConstructorAnnotation> {

}
