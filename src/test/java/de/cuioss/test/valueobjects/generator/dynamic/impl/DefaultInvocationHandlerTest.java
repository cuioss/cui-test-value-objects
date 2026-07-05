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
package de.cuioss.test.valueobjects.generator.dynamic.impl;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultInvocationHandlerTest {

    interface Sample {
        String greet();
    }

    private static Sample newProxy(final DefaultInvocationHandler handler) {
        return (Sample) Proxy.newProxyInstance(DefaultInvocationHandlerTest.class.getClassLoader(),
            new Class<?>[] { Sample.class }, handler);
    }

    @Test
    void shouldDelegateHashCodeAndToString() {
        final var handler = new DefaultInvocationHandler();
        final var proxy = newProxy(handler);
        assertEquals(handler.hashCode(), proxy.hashCode());
        assertNotNull(proxy.toString());
        assertEquals(handler.toString(), proxy.toString());
    }

    @Test
    void shouldReturnNullForRegularMethods() {
        final var proxy = newProxy(new DefaultInvocationHandler());
        assertNull(proxy.greet());
    }

    @Test
    void shouldImplementEqualsContract() {
        final var handler = new DefaultInvocationHandler();
        final var proxy = newProxy(handler);

        // reflexive
        assertTrue(proxy.equals(proxy));
        // null-safe
        assertFalse(proxy.equals(null));
        // two proxies backed by the same handler and interfaces are equal
        assertTrue(proxy.equals(newProxy(handler)));
        // proxies backed by different handlers are not equal
        assertFalse(proxy.equals(newProxy(new DefaultInvocationHandler())));
    }
}
