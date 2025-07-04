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
package de.cuioss.test.valueobjects.testbeans.property;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import de.cuioss.tools.property.PropertyReadWrite;
import lombok.Getter;
import lombok.Setter;

public class BeanWithReadWriteProperties {

    public static final String ATTRIBUTE_READ_WRITE = "readWriteProperty";
    public static final String ATTRIBUTE_READ_ONLY = "readOnlyProperty";
    public static final String ATTRIBUTE_WRITE_ONLY = "writeOnlyProperty";
    public static final String ATTRIBUTE_NOT_ACCESSIBLE = "notAccesibleProperty";
    public static final String ATTRIBUTE_TRANSIENT_VALUE = "transientProperty";
    public static final String ATTRIBUTE_DEFAULT_VALUE = "defaultValueProperty";
    public static final String ATTRIBUTE_DEFAULT_VALUE_VALUE = "defaultValue";

    public static final PropertyMetadata METATDATA_WRITE_ONLY = PropertyMetadataImpl.builder()
        .name(ATTRIBUTE_WRITE_ONLY).propertyClass(String.class).generator(Generators.nonEmptyStrings())
        .propertyReadWrite(PropertyReadWrite.WRITE_ONLY).build();

    @Getter
    @Setter
    private Integer readWriteProperty;

    @Getter
    private String readOnlyProperty;

    @Setter
    private Boolean writeOnlyProperty;

    @SuppressWarnings("unused")
    private String notAccessibleProperty;

    @Getter
    @Setter
    private transient String transientProperty;

    @Getter
    @Setter
    private String defaultValueProperty = ATTRIBUTE_DEFAULT_VALUE_VALUE;
}
