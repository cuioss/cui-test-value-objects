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
package de.cuioss.test.valueobjects.junit5.testbeans;

import java.io.Serial;
import java.io.Serializable;


import de.cuioss.test.valueobjects.api.contracts.VerifyConstructor;
import de.cuioss.tools.property.PropertyMemberInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@VerifyConstructor(of = "name")
@VerifyConstructor(of = {"name", "propertyMemberInfo"})
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class BeanWithMultipleConstructorAnnotation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1140137211367940204L;

    @Getter
    private final String name;

    @Getter
    private PropertyMemberInfo propertyMemberInfo;

    public BeanWithMultipleConstructorAnnotation(final String name, final PropertyMemberInfo propertyMemberInfo) {
        this(name);
        this.propertyMemberInfo = propertyMemberInfo;
    }
}
