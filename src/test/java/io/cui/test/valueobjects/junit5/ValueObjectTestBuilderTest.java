package io.cui.test.valueobjects.junit5;

import static io.cui.test.valueobjects.api.object.ObjectTestContracts.SERIALIZABLE;

import io.cui.test.valueobjects.api.contracts.VerifyBuilder;
import io.cui.test.valueobjects.api.object.ObjectTestConfig;
import io.cui.test.valueobjects.api.object.VetoObjectTestContract;
import io.cui.test.valueobjects.api.property.PropertyReflectionConfig;
import io.cui.test.valueobjects.property.impl.PropertyMetadataImpl;

/**
 * @author Oliver Wolff
 */
@VetoObjectTestContract(SERIALIZABLE)
@VerifyBuilder
@PropertyReflectionConfig(required = { "name", "generator", "propertyClass" },
        defaultValued = { "collectionType", "propertyMemberInfo", "propertyReadWrite",
            "propertyAccessStrategy", "assertionStrategy" })
@ObjectTestConfig(equalsAndHashCodeExclude = "generator")
class ValueObjectTestBuilderTest extends ValueObjectTest<PropertyMetadataImpl> {

}
