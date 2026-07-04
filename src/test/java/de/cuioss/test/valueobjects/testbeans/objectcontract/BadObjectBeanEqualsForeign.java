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
package de.cuioss.test.valueobjects.testbeans.objectcontract;

/**
 * Bad bean whose {@link Object#equals(Object)} wrongly returns {@code true} for a
 * foreign-type argument. This reproduces the historic no-op in
 * {@code assertBasicContractOnEquals}, where {@code assertNotEquals(new Object(), underTest)}
 * only invoked {@code Object.equals} (identity) and never {@code underTest.equals(new Object())}:
 * prior to the fix this bean passed the contract, afterwards it must fail.
 *
 * @author Oliver Wolff
 */
public class BadObjectBeanEqualsForeign {

    @Override
    public boolean equals(final Object obj) {
        // Broken by design: equals(<foreign type>) must be false, this returns true for any
        // non-null argument.
        return null != obj;
    }

    @Override
    public int hashCode() {
        return 42;
    }
}
