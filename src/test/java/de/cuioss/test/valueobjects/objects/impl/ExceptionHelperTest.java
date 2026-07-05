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
package de.cuioss.test.valueobjects.objects.impl;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExceptionHelperTest {

    private static final String NO_MESSAGE = "No exception message could be extracted";

    @Test
    void shouldHandleNullThrowable() {
        assertEquals(NO_MESSAGE, ExceptionHelper.extractMessageFromThrowable(null));
        assertEquals(NO_MESSAGE, ExceptionHelper.extractCauseMessageFromThrowable(null));
    }

    @Test
    void shouldExtractMessageFromThrowable() {
        final var result = ExceptionHelper.extractMessageFromThrowable(new IllegalArgumentException("boom"));
        assertTrue(result.contains("IllegalArgumentException"), result);
        assertTrue(result.contains("boom"), result);
    }

    @Test
    void shouldExtractCauseMessageFromPlainThrowable() {
        final var result = ExceptionHelper.extractCauseMessageFromThrowable(new IllegalStateException("plain"));
        assertTrue(result.contains("IllegalStateException"), result);
        assertTrue(result.contains("plain"), result);
    }

    @Test
    void shouldUnwrapInvocationTargetException() {
        final var ite = new InvocationTargetException(new NullPointerException("target"));
        final var result = ExceptionHelper.extractCauseMessageFromThrowable(ite);
        // the wrapped target exception must be resolved, not the InvocationTargetException itself
        assertTrue(result.contains("NullPointerException"), result);
        assertTrue(result.contains("target"), result);
    }
}
