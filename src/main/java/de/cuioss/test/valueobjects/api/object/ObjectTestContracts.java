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
package de.cuioss.test.valueobjects.api.object;

import de.cuioss.test.valueobjects.contract.EqualsAndHashcodeContractImpl;
import de.cuioss.test.valueobjects.contract.SerializableContractImpl;
import de.cuioss.test.valueobjects.contract.ToStringContractImpl;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Set;

import static de.cuioss.tools.collect.CollectionLiterals.immutableSet;

/**
 * Shorthand identifier / factory for the individual test contracts
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public enum ObjectTestContracts {

    /**
     * Tests the existence and correct implementation of the
     * {@link Object#equals(Object)} and {@link Object#hashCode()} at the level of
     * the concrete Object.
     */
    EQUALS_AND_HASHCODE(EqualsAndHashcodeContractImpl.class),

    /**
     * Tests the existence and correct implementation of {@link Object#toString()}
     * at the level of the concrete Object.
     */
    TO_STRING(ToStringContractImpl.class),

    /**
     * Tests whether the object under test is {@link Serializable} by first checking
     * whether the object implements {@link Serializable} and than actually
     * serializing and deserializing it.
     */
    SERIALIZABLE(SerializableContractImpl.class);

    @Getter
    private final Class<? extends ObjectTestContract> implementationClass;

    /**
     * @return a new instance of a {@link ObjectTestContract}.
     */
    public ObjectTestContract newObjectTestInstance() {
        return new DefaultInstantiator<>(implementationClass).newInstance();
    }

    /** Identifies the contract that are specific to Object contracts. */
    public static final Set<ObjectTestContracts> OBJECT_CONTRACTS = immutableSet(EQUALS_AND_HASHCODE, SERIALIZABLE,
        TO_STRING);
}
