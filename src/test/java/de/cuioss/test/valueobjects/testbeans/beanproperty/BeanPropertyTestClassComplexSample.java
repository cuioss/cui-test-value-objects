package de.cuioss.test.valueobjects.testbeans.beanproperty;

import de.cuioss.test.valueobjects.api.contracts.VerifyBeanProperty;

/**
 * @author Oliver Wolff
 */
@VerifyBeanProperty(exclude = "name", defaultValued = "generator", required = "collectionType", readOnly = "generator", transientProperties = "propertyMemberInfo")
public class BeanPropertyTestClassComplexSample {

}
