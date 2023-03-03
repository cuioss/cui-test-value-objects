package de.cuioss.test.valueobjects.testbeans.property;

import de.cuioss.test.generator.domain.EmailGenerator;
import de.cuioss.test.valueobjects.api.property.PropertyConfig;

/**
 * @author Oliver Wolff
 */
@PropertyConfig(name = "test", propertyClass = Integer.class, generator = EmailGenerator.class)
public class PropertyConfigPropertyClassAndGenerator {

}
