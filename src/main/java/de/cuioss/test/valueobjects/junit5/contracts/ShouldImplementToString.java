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
package de.cuioss.test.valueobjects.junit5.contracts;

import de.cuioss.test.valueobjects.contract.ReflectionUtil;
import de.cuioss.test.valueobjects.objects.TestObjectProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Simple check whether the {@link TestObjectProvider#getUnderTest()} implements
 * {@link Object#equals(Object)} and {@link Object#hashCode()}
 *
 * @author Oliver Wolff
 * @param <T> identifying the type to be tested is usually but not necessarily
 *            at least Serializable.
 *
 */
public interface ShouldImplementToString<T> extends TestObjectProvider<T> {

    /**
     * Simple check whether the {@link TestObjectProvider#getUnderTest()} implements
     * {@link Object#equals(Object)} and {@link Object#hashCode()}
     */
    @Test
    default void shouldImplementToString() {
        var underTest = getUnderTest();
        ReflectionUtil.assertToStringMethodIsOverriden(underTest.getClass());
        assertNotNull(underTest.toString(), "toString must not return 'null'");
    }
}
