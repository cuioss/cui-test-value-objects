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
package de.cuioss.test.valueobjects.junit5;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.cuioss.test.valueobjects.ValueObjectTest;
import de.cuioss.test.valueobjects.junit5.testbeans.ComplexBean;

class ValueObjectTestInlineInstantiatorTest extends ValueObjectTest<ComplexBean> {

    private ComplexBean any = null;

    @Override
    public void shouldImplementObjectContracts() {
        any = null;
        AssertionError ex = assertThrows(AssertionError.class, super::shouldImplementObjectContracts);
        assertEquals(
            "You need to configure either at least one de.icw.cui.test.valueobjects.api.contracts " +
                "or implement #anyValueObject()",
            ex.getMessage());

        any = new ComplexBean();
        assertDoesNotThrow(super::shouldImplementObjectContracts);
    }

    @Override
    protected ComplexBean anyValueObject() {
        return any;
    }

}
