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

import de.cuioss.test.valueobjects.objects.ObjectInstantiationException;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.tools.logging.CuiLogger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static de.cuioss.test.valueobjects.objects.impl.ExceptionHelper.extractCauseMessageFromThrowable;
import static de.cuioss.tools.base.Preconditions.checkArgument;
import static de.cuioss.tools.string.MoreStrings.isEmpty;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This {@link ParameterizedInstantiator} uses a factory method derived by the
 * given all properties from RuntimeProperties in order to instantiate
 * {@link Object}s
 *
 * @author Oliver Wolff
 * @param <T> identifying the type of objects to be created
 */
public class FactoryBasedInstantiator<T> extends AbstractOrderedArgsInstantiator<T> {

    private static final CuiLogger LOGGER = new CuiLogger(FactoryBasedInstantiator.class);

    private final Method factoryMethod;

    /**
     * Constructor.
     *
     * @param type              the type of the object to be created
     * @param runtimeProperties must not be null. defines the attributes in the
     *                          exact order to be used for the constructor:
     *                          all properties from RuntimeProperties
     * @param enclosingType     the type providing the factory method, usually the
     *                          target type to be created, must not be null
     * @param factoryMethodName the name of the factory method, must not be null,
     *                          nor empty.
     */
    public FactoryBasedInstantiator(final Class<T> type, final RuntimeProperties runtimeProperties,
        final Class<?> enclosingType, final String factoryMethodName) {

        super(runtimeProperties);
        requireNonNull(type, "type must not be null");
        requireNonNull(enclosingType, "enclosingType must not be null");
        requireNonNull(factoryMethodName);
        checkArgument(!isEmpty(factoryMethodName), "factoryMethodName must not be null nor empty");

        final List<Class<?>> parameter = new ArrayList<>();
        runtimeProperties.getAllProperties().forEach(meta -> parameter.add(meta.resolveActualClass()));
        try {
            factoryMethod = resolveFactoryMethod(enclosingType, factoryMethodName, toClassArray(parameter));
            assertTrue(type.isAssignableFrom(factoryMethod.getReturnType()),
                "Invalid type found on factory method: " + factoryMethod.getReturnType());
            checkArgument(Modifier.isStatic(factoryMethod.getModifiers()),
                "Factory method '" + factoryMethodName + "' on " + enclosingType.getName()
                    + " must be static in order to be usable as a factory method");
        } catch (NoSuchMethodException | SecurityException e) {
            final var message = "Unable to find factory method '" + factoryMethodName + "' on "
                + enclosingType.getName() + " with signature " + parameter;
            LOGGER.error(e, message);
            throw new AssertionError(message, e);
        }
    }

    /**
     * Resolves the factory method. It first tries the methods declared on the
     * given type and falls back to {@link Class#getMethod(String, Class...)} in
     * order to also find methods inherited from a (potentially abstract)
     * superclass.
     */
    private static Method resolveFactoryMethod(final Class<?> enclosingType, final String factoryMethodName,
        final Class<?>[] parameterTypes) throws NoSuchMethodException {
        try {
            return enclosingType.getDeclaredMethod(factoryMethodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            LOGGER.debug(e, "No declared method '%s' found, falling back to inherited public methods",
                factoryMethodName);
            return enclosingType.getMethod(factoryMethodName, parameterTypes);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected T doInstantiate(final Object... args) {
        try {
            return (T) factoryMethod.invoke(null, args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            final var message = "Unable to invoke factory method, due to " +
                extractCauseMessageFromThrowable(e);
            throw new ObjectInstantiationException(message, e);
        }
    }

    @Override
    public String toString() {
        return getClass().getName() + "\nFactory Method: " + factoryMethod +
            "\nProperty Configuration: " + getRuntimeProperties().toString();
    }
}
