/*
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
package de.cuioss.test.valueobjects.contract;

import de.cuioss.test.valueobjects.testbeans.ComplexBean;
import de.cuioss.test.valueobjects.testbeans.objectcontract.BadObjectBean;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ReflectionUtilTest {

    @Test
    void shouldDetectEqualsIsOverridden() {
        ReflectionUtil.assertEqualsMethodIsOverriden(ComplexBean.class);
    }

    @Test
    void shouldDetectEqualsIsNotOverridden() {
        assertThrows(AssertionError.class, () -> ReflectionUtil.assertEqualsMethodIsOverriden(BadObjectBean.class));
    }

    @Test
    void shouldDetectHashCodeIsOverridden() {
        ReflectionUtil.assertHashCodeMethodIsOverriden(ComplexBean.class);
    }

    @Test
    void shouldDetectHashCodeIsNotOverridden() {
        assertThrows(AssertionError.class, () -> ReflectionUtil.assertHashCodeMethodIsOverriden(BadObjectBean.class));
    }

    @Test
    void shouldDetectToStringIsOverridden() {
        ReflectionUtil.assertToStringMethodIsOverriden(ComplexBean.class);
    }

    @Test
    void shouldDetectToStringIsNotOverridden() {
        assertThrows(AssertionError.class, () -> ReflectionUtil.assertToStringMethodIsOverriden(BadObjectBean.class));
    }
}
