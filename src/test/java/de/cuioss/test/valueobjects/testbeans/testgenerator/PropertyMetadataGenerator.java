/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.testbeans.testgenerator;

import static de.cuioss.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator.ATTRIBUTE_ACCESS_STRATEGY;
import static de.cuioss.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator.ATTRIBUTE_COLLECTION_WRAPPER;
import static de.cuioss.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator.ATTRIBUTE_DEFAULT_VALUE;
import static de.cuioss.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator.ATTRIBUTE_MEMBER_INFO;
import static de.cuioss.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator.ATTRIBUTE_NAME;
import static de.cuioss.test.valueobjects.testbeans.testgenerator.PropertyMetadataTestDataGenerator.ATTRIBUTE_REQUIRED;
import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.generator.internal.net.java.quickcheck.Generator;
import de.cuioss.test.generator.internal.net.java.quickcheck.generator.support.FixedValuesGenerator;
import de.cuioss.test.valueobjects.property.PropertyMetadata;

public class PropertyMetadataGenerator implements TypedGenerator<PropertyMetadata> {

    private static final Generator<PropertyMetadataTestDataGenerator> GENERATOR_GENERATOR = new FixedValuesGenerator<>(
            immutableList(ATTRIBUTE_NAME, ATTRIBUTE_DEFAULT_VALUE, ATTRIBUTE_REQUIRED, ATTRIBUTE_ACCESS_STRATEGY,
                    ATTRIBUTE_COLLECTION_WRAPPER, ATTRIBUTE_MEMBER_INFO));

    @Override
    public PropertyMetadata next() {
        return GENERATOR_GENERATOR.next().build();
    }

    @Override
    public Class<PropertyMetadata> getType() {
        return PropertyMetadata.class;
    }
}
