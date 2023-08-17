package de.cuioss.test.valueobjects.junit5;

import static de.cuioss.test.valueobjects.api.object.ObjectTestContracts.SERIALIZABLE;

import de.cuioss.test.valueobjects.ValueObjectTest;
import de.cuioss.test.valueobjects.api.contracts.VerifyBuilder;
import de.cuioss.test.valueobjects.api.object.ObjectTestConfig;
import de.cuioss.test.valueobjects.api.object.VetoObjectTestContract;
import de.cuioss.test.valueobjects.api.property.PropertyReflectionConfig;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;

/**
 * @author Oliver Wolff
 */
@VetoObjectTestContract(SERIALIZABLE)
@VerifyBuilder
@PropertyReflectionConfig(required = { "name", "generator", "propertyClass" }, defaultValued = { "collectionType",
        "propertyMemberInfo", "propertyReadWrite", "propertyAccessStrategy", "assertionStrategy" })
@ObjectTestConfig(equalsAndHashCodeExclude = "generator")
class ValueObjectTestBuilderTest extends ValueObjectTest<PropertyMetadataImpl> {

}
