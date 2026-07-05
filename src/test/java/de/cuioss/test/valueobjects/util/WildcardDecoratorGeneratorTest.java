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

import de.cuioss.test.generator.Generators;
import de.cuioss.test.generator.TypedGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WildcardDecoratorGeneratorTest {

    @Test
    void shouldExposeDeclaredTypeAndDelegateValueCreation() {
        final TypedGenerator<String> delegate = Generators.nonEmptyStrings();
        final var decorator = new WildcardDecoratorGenerator(Number.class, delegate);

        // getType() returns the declared (wildcard) type, not the delegate's type
        assertEquals(Number.class, decorator.getType());

        final var next = decorator.next();
        assertNotNull(next);
        // next() is delegated verbatim, so the actual value stems from the delegate
        assertInstanceOf(String.class, next);
    }
}
