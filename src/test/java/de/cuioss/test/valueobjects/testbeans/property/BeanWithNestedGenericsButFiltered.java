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

import de.cuioss.test.valueobjects.api.property.PropertyReflectionConfig;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@PropertyReflectionConfig(exclude = "nested")
public class BeanWithNestedGenericsButFiltered implements Serializable {

    @Serial
    private static final long serialVersionUID = 7213569922964396147L;

    @Getter
    private List<String> list;

    @Getter
    private List<List<String>> nested;
}
