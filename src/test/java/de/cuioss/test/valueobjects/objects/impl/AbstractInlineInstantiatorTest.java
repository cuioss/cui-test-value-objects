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
package de.cuioss.test.valueobjects.objects.impl;

import de.cuioss.test.valueobjects.testbeans.ComplexBean;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AbstractInlineInstantiatorTest extends AbstractInlineInstantiator<ComplexBean> {

    @Override
    protected ComplexBean any() {
        return new ComplexBean();
    }

    @Test
    void shouldBehave() {
        assertNotNull(getRuntimeProperties());
        assertNotNull(super.newInstance(null));
        assertNotNull(super.newInstanceMinimal());
        assertNotNull(super.newInstanceFull());
        assertNotNull(super.newInstance(null, true));
    }
}
