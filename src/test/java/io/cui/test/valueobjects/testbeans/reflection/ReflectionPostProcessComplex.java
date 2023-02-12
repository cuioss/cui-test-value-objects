package io.cui.test.valueobjects.testbeans.reflection;

import static io.cui.test.valueobjects.testbeans.property.BeanWithReadWriteProperties.ATTRIBUTE_DEFAULT_VALUE;
import static io.cui.test.valueobjects.testbeans.property.BeanWithReadWriteProperties.ATTRIBUTE_READ_ONLY;
import static io.cui.test.valueobjects.testbeans.property.BeanWithReadWriteProperties.ATTRIBUTE_READ_WRITE;
import static io.cui.test.valueobjects.testbeans.property.BeanWithReadWriteProperties.ATTRIBUTE_TRANSIENT_VALUE;

import io.cui.test.valueobjects.api.property.PropertyReflectionConfig;

@SuppressWarnings("javadoc")
@PropertyReflectionConfig(required = ATTRIBUTE_READ_WRITE, defaultValued = ATTRIBUTE_READ_ONLY,
        exclude = ATTRIBUTE_DEFAULT_VALUE, transientProperties = ATTRIBUTE_TRANSIENT_VALUE)
public class ReflectionPostProcessComplex {

}
