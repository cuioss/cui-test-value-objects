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
package de.cuioss.test.valueobjects.objects.impl;

import de.cuioss.test.valueobjects.objects.ObjectInstantiator;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.lang.reflect.InvocationTargetException;

import static de.cuioss.test.valueobjects.objects.impl.ExceptionHelper.extractCauseMessageFromThrowable;

/**
 * Instantiator for any class that provide a public accessible default
 * constructor
 *
 * @author Oliver Wolff
 * @param <T> identifying the type of object to be instantiated
 */
@RequiredArgsConstructor
@ToString
public class DefaultInstantiator<T> implements ObjectInstantiator<T> {

    @NonNull
    @Getter
    private final Class<T> targetClass;

    /**
     * @return a newly created instance
     */
    @Override
    public T newInstance() {
        try {
            return targetClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
            | NoSuchMethodException | SecurityException e) {
            throw new AssertionError("Unable to instantiate class due to " + extractCauseMessageFromThrowable(e), e);
        }
    }

}
