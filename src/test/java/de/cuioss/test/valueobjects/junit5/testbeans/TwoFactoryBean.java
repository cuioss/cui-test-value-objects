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
package de.cuioss.test.valueobjects.junit5.testbeans;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

import java.io.Serializable;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.valueobjects.api.contracts.VerifyFactoryMethod;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@VerifyFactoryMethod(factoryMethodName = "create", of = "attribute")
@VerifyFactoryMethod(factoryMethodName = "create", of = {})
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class TwoFactoryBean implements Serializable {

    private static final long serialVersionUID = 4539639907656084561L;

    public static final PropertyMetadata ATTRIBUTE = PropertyMetadataImpl.builder().name("attribute")
            .propertyClass(String.class).generator(Generators.nonEmptyStrings()).build();

    public static final RuntimeProperties INFORMATION = new RuntimeProperties(immutableList(ATTRIBUTE));

    public static final String CREATE_METHOD_NAME = "create";

    @Getter
    private String attribute;

    public TwoFactoryBean(String attribute) {
        this.attribute = attribute;
    }

    public static TwoFactoryBean create() {
        return new TwoFactoryBean();
    }

    public static TwoFactoryBean create(String attribute) {
        return new TwoFactoryBean(attribute);
    }
}
