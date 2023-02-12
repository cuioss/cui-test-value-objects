package io.cui.test.valueobjects.testbeans.beanproperty;

import io.cui.test.valueobjects.api.contracts.VerifyBeanProperty;

/**
 * @author Oliver Wolff
 */
@VerifyBeanProperty(exclude = "name", defaultValued = "generator",
        required = "collectionType", readOnly = "generator",
        transientProperties = "propertyMemberInfo")
public class BeanPropertyTestClassComplexSample {

}
