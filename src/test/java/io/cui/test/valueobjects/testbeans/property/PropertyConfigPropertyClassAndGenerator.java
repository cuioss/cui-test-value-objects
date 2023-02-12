package io.cui.test.valueobjects.testbeans.property;

import io.cui.test.generator.domain.EmailGenerator;
import io.cui.test.valueobjects.api.property.PropertyConfig;

/**
 * @author Oliver Wolff
 */
@PropertyConfig(name = "test", propertyClass = Integer.class, generator = EmailGenerator.class)
public class PropertyConfigPropertyClassAndGenerator {

}
