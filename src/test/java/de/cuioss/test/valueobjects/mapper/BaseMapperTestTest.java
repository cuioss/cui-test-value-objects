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

import de.cuioss.test.valueobjects.MapperTest;
import de.cuioss.test.valueobjects.api.VerifyMapperConfiguration;
import de.cuioss.test.valueobjects.testbeans.mapper.SimpleMapper;
import de.cuioss.test.valueobjects.testbeans.mapper.SimpleSourceBean;
import de.cuioss.test.valueobjects.testbeans.mapper.SimpleTargetBean;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@VerifyMapperConfiguration(equals = {"firstname:nameFirst", "lastname:nameLast", "attributeList:listOfAttributes"})
class BaseMapperTestTest extends MapperTest<SimpleMapper, SimpleSourceBean, SimpleTargetBean> {

    @Test
    void shouldDetermineTypes() {
        super.intializeTypeInformation();
        assertEquals(SimpleMapper.class, getMapperClass());
        assertEquals(SimpleSourceBean.class, getSourceClass());
        assertEquals(SimpleTargetBean.class, getTargetClass());
    }

    @Test
    void shouldDetermineSourceMetadata() {
        assertNotNull(super.resolveSourcePropertyMetadata());
        assertEquals(3, super.resolveSourcePropertyMetadata().size());
    }

    @Test
    void shouldDetermineTargetMetadata() {
        assertNotNull(super.resolveTargetPropertyMetadata(null));
        assertEquals(3, super.resolveTargetPropertyMetadata(null).size());
    }

}
