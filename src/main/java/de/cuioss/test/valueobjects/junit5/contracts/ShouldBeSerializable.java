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
package de.cuioss.test.valueobjects.junit5.contracts;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;


import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.contract.SerializableContractImpl;
import de.cuioss.test.valueobjects.objects.TestObjectProvider;

/**
 * Simple check whether the returned {@link TestObjectProvider#getUnderTest()}
 * implements {@link Serializable} correctly
 *
 * @author Oliver Wolff
 * @param <T> identifying the type to be tested is usually but not necessarily
 *            at least Serializable.
 *
 */
public interface ShouldBeSerializable<T> extends TestObjectProvider<T> {

    /**
     * Simple check whether the returned {@link TestObjectProvider#getUnderTest()}
     * implements {@link Serializable} correctly
     */
    @Test
    default void shouldImplementSerializable() {
        var underTest = getUnderTest();
        assertTrue(underTest instanceof Serializable,
                underTest.getClass().getName() + " does not implement java.io.Serializable");
        SerializableContractImpl.serializeAndDeserialize(underTest);
    }
}
