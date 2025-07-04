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
package de.cuioss.test.valueobjects.mapper;

import static org.junit.jupiter.api.Assertions.assertThrows;


import de.cuioss.test.valueobjects.MapperTest;
import de.cuioss.test.valueobjects.api.VerifyMapperConfiguration;
import de.cuioss.test.valueobjects.testbeans.mapper.SimpleErrorMapper;
import de.cuioss.test.valueobjects.testbeans.mapper.SimpleSourceBean;
import de.cuioss.test.valueobjects.testbeans.mapper.SimpleTargetBean;
import org.junit.jupiter.api.Test;

@VerifyMapperConfiguration(equals = {"not:there", "lastname:nameLast", "attributeList:listOfAttributes"})
class BaseMapperInvalidConfigTest extends MapperTest<SimpleErrorMapper, SimpleSourceBean, SimpleTargetBean> {

    @Test @Override protected void verifyMapper() {
        assertThrows(AssertionError.class, super::verifyMapper);
    }
}
