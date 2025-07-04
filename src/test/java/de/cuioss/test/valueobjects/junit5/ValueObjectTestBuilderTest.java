/**
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.junit5;

import de.cuioss.test.valueobjects.ValueObjectTest;
import de.cuioss.test.valueobjects.api.contracts.VerifyBuilder;
import de.cuioss.test.valueobjects.api.object.ObjectTestConfig;
import de.cuioss.test.valueobjects.api.object.VetoObjectTestContract;
import de.cuioss.test.valueobjects.api.property.PropertyReflectionConfig;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;

import static de.cuioss.test.valueobjects.api.object.ObjectTestContracts.SERIALIZABLE;

/**
 * @author Oliver Wolff
 */
@VetoObjectTestContract(SERIALIZABLE)
@VerifyBuilder
@PropertyReflectionConfig(required = {"name", "generator", "propertyClass"}, defaultValued = {"collectionType",
    "propertyMemberInfo", "propertyReadWrite", "propertyAccessStrategy", "assertionStrategy"})
@ObjectTestConfig(equalsAndHashCodeExclude = "generator")
class ValueObjectTestBuilderTest extends ValueObjectTest<PropertyMetadataImpl> {

}
