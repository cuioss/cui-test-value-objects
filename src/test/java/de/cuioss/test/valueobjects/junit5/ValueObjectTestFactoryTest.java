package de.cuioss.test.valueobjects.junit5;

import de.cuioss.test.valueobjects.ValueObjectTest;
import de.cuioss.test.valueobjects.api.contracts.VerifyFactoryMethod;
import de.cuioss.test.valueobjects.junit5.testbeans.TwoFactoryBean;

@SuppressWarnings("javadoc")
@VerifyFactoryMethod(factoryMethodName = "create", of = "attribute")
@VerifyFactoryMethod(factoryMethodName = "create", of = {})
public class ValueObjectTestFactoryTest extends ValueObjectTest<TwoFactoryBean> {

}
