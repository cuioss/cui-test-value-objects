package de.cuioss.test.valueobjects.testbeans.beanproperty;

import de.cuioss.test.valueobjects.api.contracts.VerifyBeanProperty;

/**
 * @author Oliver Wolff
 */
@VerifyBeanProperty(exclude = { "name", "defaultValue" })
public class BeanPropertyTestClassExcludeNameAndDefaultValue {

}
