/**
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
package de.cuioss.test.valueobjects.testbeans.factory;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.valueobjects.api.contracts.VerifyFactoryMethod;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

@VerifyFactoryMethod(factoryMethodName = "create", of = "attribute", required = "attribute")
@RequiredArgsConstructor
public class OneFactoryBean {

    public static final PropertyMetadata ATTRIBUTE = PropertyMetadataImpl.builder().name("attribute")
        .propertyClass(String.class).required(true).generator(Generators.nonEmptyStrings()).build();

    public static final RuntimeProperties INFORMATION = new RuntimeProperties(immutableList(ATTRIBUTE));

    public static final String CREATE_METHOD_NAME = "create";

    @Getter
    private final String attribute;

    public static OneFactoryBean create(String attribute) {
        return new OneFactoryBean(attribute);
    }
}
