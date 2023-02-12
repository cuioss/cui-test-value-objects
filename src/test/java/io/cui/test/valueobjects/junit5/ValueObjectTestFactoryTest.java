package io.cui.test.valueobjects.junit5;

import io.cui.test.valueobjects.api.contracts.VerifyFactoryMethod;
import io.cui.test.valueobjects.junit5.testbeans.TwoFactoryBean;

@SuppressWarnings("javadoc")
@VerifyFactoryMethod(factoryMethodName = "create",
        of = "attribute")
@VerifyFactoryMethod(factoryMethodName = "create",
        of = {})
public class ValueObjectTestFactoryTest
        extends ValueObjectTest<TwoFactoryBean> {

}
