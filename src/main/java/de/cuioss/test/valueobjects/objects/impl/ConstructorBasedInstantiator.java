/**
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.objects.impl;

import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.tools.logging.CuiLogger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static de.cuioss.test.valueobjects.objects.impl.ExceptionHelper.extractCauseMessageFromThrowable;
import static java.util.Objects.requireNonNull;

/**
 * This {@link ParameterizedInstantiator} uses a constructor derived by the
 * given all properties from RuntimeProperties in order to instantiate
 * {@link Object}s
 *
 * @param <T> identifying the type of objects to be created
 *
 * @author Oliver Wolff
 */
public class ConstructorBasedInstantiator<T> extends AbstractOrderedArgsInstantiator<T> {

    private final Constructor<T> constructor;

    private static final CuiLogger log = new CuiLogger(ConstructorBasedInstantiator.class);

    /**
     * Constructor.
     *
     * @param type              identifying the actual type to be instantiated, must
     *                          not be null
     * @param runtimeProperties must not be null. defines the attributes in the
     *                          exact order to be used for the constructor:
     *                          all properties from RuntimeProperties
     */
    public ConstructorBasedInstantiator(final Class<T> type, final RuntimeProperties runtimeProperties) {

        super(runtimeProperties);
        requireNonNull(type);

        final List<Class<?>> parameter = new ArrayList<>();
        runtimeProperties.getAllProperties().forEach(meta -> parameter.add(meta.resolveActualClass()));
        try {
            if (parameter.isEmpty()) {
                constructor = type.getConstructor();
            } else {
                constructor = type.getConstructor(toClassArray(parameter));
            }
            requireNonNull(constructor, "Unable to find a constructor with signature " + parameter);
        } catch (NoSuchMethodException | SecurityException e) {
            final var message = "Unable to find a constructor with signature " + parameter +
                ", for type " + type.getName();
            log.error(message, e);
            for (Constructor<?> tempConstructor : type.getConstructors()) {
                log.error("Found constructor: {}", tempConstructor.toGenericString());
            }
            if (0 == type.getConstructors().length) {
                log.error("No public constructor found!");
            }
            throw new AssertionError(message);
        }
    }

    @Override
    protected T doInstantiate(final Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                 | InvocationTargetException e) {
            final var message = "Unable to invoke constructor " + ", due to " +
                extractCauseMessageFromThrowable(e);
            throw new AssertionError(message, e);
        }
    }

    @Override
    public String toString() {
        return getClass().getName() + "\nConstructor: " + constructor +
            "\nProperty Configuration: " + getRuntimeProperties().toString();
    }
}
