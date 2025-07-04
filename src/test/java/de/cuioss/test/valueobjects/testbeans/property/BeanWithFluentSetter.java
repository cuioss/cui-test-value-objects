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
package de.cuioss.test.valueobjects.testbeans.property;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import de.cuioss.tools.property.PropertyReadWrite;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode
@ToString
public class BeanWithFluentSetter implements Serializable {

    @Serial
    private static final long serialVersionUID = 4441858070683023193L;

    public static final PropertyMetadata METATDATA = PropertyMetadataImpl.builder().name("field")
        .propertyClass(String.class).generator(Generators.nonEmptyStrings())
        .propertyReadWrite(PropertyReadWrite.READ_WRITE).build();

    @Getter
    private String field;

    public BeanWithFluentSetter setField(String field) {
        this.field = field;
        return this;
    }

}
