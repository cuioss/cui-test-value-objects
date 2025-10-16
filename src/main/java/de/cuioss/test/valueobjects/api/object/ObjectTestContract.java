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
package de.cuioss.test.valueobjects.api.object;

import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;

/**
 * An {@link ObjectTestContract} provides a method that again runs a number of
 * asserts regarding the canonical {@link Object} method.
 *
 * @author Oliver Wolff
 */
@FunctionalInterface
public interface ObjectTestContract {

    /**
     * Checks the concrete contract.
     *
     * @param instantiator     must not be null. The actual type of the
     *                         {@link Object} is not of interest, because we check
     *                         {@link Object} contracts
     * @param objectTestConfig optional configuration configuring the tests
     */
    void assertContract(ParameterizedInstantiator<?> instantiator, ObjectTestConfig objectTestConfig);
}
