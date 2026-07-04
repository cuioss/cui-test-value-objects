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
package de.cuioss.test.valueobjects.objects;

import java.io.Serial;

/**
 * Dedicated {@link AssertionError} raised by the different
 * {@link ParameterizedInstantiator} / {@link BuilderInstantiator}
 * implementations when the actual reflective creation of an object (invoking a
 * constructor, factory- or builder-method) fails. Most prominently this is the
 * signal that a required property was missing and the object under test
 * correctly rejected the creation.
 * <p>
 * Having a dedicated type allows the contract implementations to distinguish
 * this <em>expected</em> rejection from unrelated {@link AssertionError}s (e.g.
 * a failing value assertion), which must always surface instead of being
 * silently swallowed.
 * <p>
 * It extends {@link AssertionError} in order to stay backwards compatible with
 * callers that only expect an {@link AssertionError} to be thrown on
 * instantiation failure.
 *
 * @author Oliver Wolff
 */
public class ObjectInstantiationException extends AssertionError {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * @param message describing the instantiation failure
     * @param cause   the underlying cause, may be null
     */
    public ObjectInstantiationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message describing the instantiation failure
     */
    public ObjectInstantiationException(final String message) {
        super(message);
    }
}
