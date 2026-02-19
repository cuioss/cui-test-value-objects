/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.testbeans.testgenerator;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.valueobjects.property.PropertyMetadata;

import static de.cuioss.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator.*;

public class PropertyMetadataGenerator implements TypedGenerator<PropertyMetadata> {

    private static final TypedGenerator<PropertyMetadataTestDataGenerator> GENERATOR_GENERATOR = Generators.fixedValues(
        ATTRIBUTE_NAME, ATTRIBUTE_DEFAULT_VALUE, ATTRIBUTE_REQUIRED, ATTRIBUTE_ACCESS_STRATEGY,
        ATTRIBUTE_COLLECTION_WRAPPER, ATTRIBUTE_MEMBER_INFO);

    @Override
    public PropertyMetadata next() {
        return GENERATOR_GENERATOR.next().build();
    }

    @Override
    public Class<PropertyMetadata> getType() {
        return PropertyMetadata.class;
    }
}
