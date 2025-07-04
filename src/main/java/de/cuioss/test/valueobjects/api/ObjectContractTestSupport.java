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
package de.cuioss.test.valueobjects.api;

import de.cuioss.test.valueobjects.api.object.ObjectTestContracts;
import de.cuioss.test.valueobjects.api.object.VetoObjectTestContract;

import java.io.Serializable;

/**
 * Defines the method for testing the correctness of {@link Object} related
 * tests, see {@link #shouldImplementObjectContracts()}
 *
 * @author Oliver Wolff
 */
public interface ObjectContractTestSupport {

    /**
     * <p>
     * Base test for ValueObjects verifying the correctness of
     * {@link Object#equals(Object)} {@link Object#hashCode()},
     * {@link Object#toString()} and {@link Serializable} implementations.
     * </p>
     * <p>
     * If you want to disable one of these tests you can annotate the test class
     * with {@link VetoObjectTestContract} with the value
     * {@link ObjectTestContracts#EQUALS_AND_HASHCODE},
     * {@link ObjectTestContracts#SERIALIZABLE} or
     * {@link ObjectTestContracts#TO_STRING}
     * </p>
     */
    void shouldImplementObjectContracts();

}
