package io.cui.test.valueobjects.api.object;

import static io.cui.tools.collect.CollectionLiterals.immutableSet;

import java.io.Serializable;
import java.util.Set;

import io.cui.test.valueobjects.contract.EqualsAndHashcodeContractImpl;
import io.cui.test.valueobjects.contract.SerializableContractImpl;
import io.cui.test.valueobjects.contract.ToStringContractImpl;
import io.cui.test.valueobjects.objects.impl.DefaultInstantiator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Shorthand identifier / factory for the individual test contracts
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public enum ObjectTestContracts {

    /**
     * Tests the existence and correct implementation of the {@link Object#equals(Object)} and
     * {@link Object#hashCode()} at the level of the concrete Object.
     */
    EQUALS_AND_HASHCODE(EqualsAndHashcodeContractImpl.class),

    /**
     * Tests the existence and correct implementation of {@link Object#toString()} at the level of
     * the concrete Object.
     */
    TO_STRING(ToStringContractImpl.class),

    /**
     * Tests whether the object under test is {@link Serializable} by first checking whether the
     * object implements {@link Serializable} and than actually serializing and deserializing it.
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
    public static final Set<ObjectTestContracts> OBJECT_CONTRACTS =
        immutableSet(EQUALS_AND_HASHCODE, SERIALIZABLE, TO_STRING);
}
