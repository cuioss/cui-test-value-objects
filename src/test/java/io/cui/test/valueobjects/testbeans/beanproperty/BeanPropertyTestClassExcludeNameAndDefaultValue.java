package io.cui.test.valueobjects.testbeans.beanproperty;

import io.cui.test.valueobjects.api.contracts.VerifyBeanProperty;

/**
 * @author Oliver Wolff
 */
@VerifyBeanProperty(exclude = { "name", "defaultValue" })
public class BeanPropertyTestClassExcludeNameAndDefaultValue {

}
