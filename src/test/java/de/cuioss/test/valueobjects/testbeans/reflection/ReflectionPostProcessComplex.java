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
package de.cuioss.test.valueobjects.testbeans.reflection;

import static de.cuioss.test.valueobjects.testbeans.property.BeanWithReadWriteProperties.ATTRIBUTE_DEFAULT_VALUE;
import static de.cuioss.test.valueobjects.testbeans.property.BeanWithReadWriteProperties.ATTRIBUTE_READ_ONLY;
import static de.cuioss.test.valueobjects.testbeans.property.BeanWithReadWriteProperties.ATTRIBUTE_READ_WRITE;
import static de.cuioss.test.valueobjects.testbeans.property.BeanWithReadWriteProperties.ATTRIBUTE_TRANSIENT_VALUE;

import de.cuioss.test.valueobjects.api.property.PropertyReflectionConfig;

@PropertyReflectionConfig(required = ATTRIBUTE_READ_WRITE, defaultValued = ATTRIBUTE_READ_ONLY, exclude = ATTRIBUTE_DEFAULT_VALUE, transientProperties = ATTRIBUTE_TRANSIENT_VALUE)
public class ReflectionPostProcessComplex {

}
