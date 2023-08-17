package de.cuioss.test.valueobjects.testbeans.reflection;

import static de.cuioss.test.valueobjects.testbeans.property.BeanWithReadWriteProperties.ATTRIBUTE_DEFAULT_VALUE;
import static de.cuioss.test.valueobjects.testbeans.property.BeanWithReadWriteProperties.ATTRIBUTE_READ_ONLY;
import static de.cuioss.test.valueobjects.testbeans.property.BeanWithReadWriteProperties.ATTRIBUTE_READ_WRITE;
import static de.cuioss.test.valueobjects.testbeans.property.BeanWithReadWriteProperties.ATTRIBUTE_TRANSIENT_VALUE;

import de.cuioss.test.valueobjects.api.property.PropertyReflectionConfig;

@SuppressWarnings("javadoc")
@PropertyReflectionConfig(required = ATTRIBUTE_READ_WRITE, defaultValued = ATTRIBUTE_READ_ONLY, exclude = ATTRIBUTE_DEFAULT_VALUE, transientProperties = ATTRIBUTE_TRANSIENT_VALUE)
public class ReflectionPostProcessComplex {

}
