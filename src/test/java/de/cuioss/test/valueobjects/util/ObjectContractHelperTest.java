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
package de.cuioss.test.valueobjects.util;

import de.cuioss.test.valueobjects.testbeans.veto.*;
import org.junit.jupiter.api.Test;

import static de.cuioss.test.valueobjects.util.ObjectContractHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObjectContractHelperTest {

    @Test
    void shouldExtractVetoesFromInheritedClass() {
        assertEquals(2, extractConfiguredVetoObjectContracts(InheritedVeto.class).size());
        assertEquals(3, extractConfiguredVetoObjectContracts(InheritedVetoWithAdditionalVeto.class).size());
    }

    @Test
    void shouldHandleVetoedAnnotations() {
        assertEquals(3, handleVetoedContracts(getClass()).size());
        assertEquals(2, handleVetoedContracts(ClassWithOneVeto.class).size());
        assertEquals(1, handleVetoedContracts(ClassWithTwoVetoes.class).size());
        assertEquals(0, handleVetoedContracts(ClassWithMixedVetoes.class).size());
    }

    @Test
    void shouldHandleOptInContracts() {
        assertTrue(handleOptedInContracts(getClass()).isEmpty());
        assertEquals(1, handleOptedInContracts(ClassWithOneOptIn.class).size());
        assertEquals(2, handleOptedInContracts(ClassWithTwoOptIns.class).size());
    }
}
