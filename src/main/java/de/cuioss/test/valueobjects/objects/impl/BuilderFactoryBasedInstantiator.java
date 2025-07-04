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

import de.cuioss.test.valueobjects.contract.BuilderContractImpl;
import de.cuioss.test.valueobjects.objects.BuilderInstantiator;
import de.cuioss.tools.logging.CuiLogger;
import lombok.Getter;
import lombok.ToString;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static de.cuioss.test.valueobjects.objects.impl.ExceptionHelper.extractCauseMessageFromThrowable;
import static java.util.Objects.requireNonNull;

/**
 * Used for creating instances of a builder. This variant relies on a factory
 * method on the target type usually with the name "builder". See
 * {@link BuilderFactoryBasedInstantiator#BuilderFactoryBasedInstantiator(Class, String, String)}
 * for details
 *
 * @author Oliver Wolff
 * @param <T> identifying the type of the {@link Object} created by the builder
 */
@ToString
public class BuilderFactoryBasedInstantiator<T> implements BuilderInstantiator<T> {

    private static final String UNABLE_TO_ACCESS_METHOD = "Unable to access method %s on type %s, due to %s";

    private static final CuiLogger log = new CuiLogger(BuilderFactoryBasedInstantiator.class);

    private final Method builderFactoryMethod;
    private final Method builderMethod;

    @Getter
    private final Class<T> targetClass;

    @Getter
    private final Class<?> builderClass;

    /**
     * Constructor. shortcut for calling
     * {@link #BuilderFactoryBasedInstantiator(Class, String, String)} with
     * {@link BuilderContractImpl#DEFAULT_BUILDER_FACTORY_METHOD_NAME} and
     * {@value BuilderContractImpl#DEFAULT_BUILD_METHOD_NAME}
     *
     * @param enclosingType identifying the type where the method is located in,
     *                      must not be null.
     */
    public BuilderFactoryBasedInstantiator(final Class<?> enclosingType) {
        this(enclosingType, BuilderContractImpl.DEFAULT_BUILDER_FACTORY_METHOD_NAME,
            BuilderContractImpl.DEFAULT_BUILD_METHOD_NAME);
    }

    /**
     * Constructor.
     *
     * @param enclosingType            identifying the type where the method is
     *                                 located in, must not be null.
     * @param builderFactoryMethodName The name of the factory method on the
     *                                 enclosing type. It is assumed that it is
     *                                 parameter-free static method
     * @param builderMethodName        the actual name or the builder-method
     */
    @SuppressWarnings("unchecked")
    public BuilderFactoryBasedInstantiator(final Class<?> enclosingType, final String builderFactoryMethodName,
                                           final String builderMethodName) {

        requireNonNull(enclosingType, "enclosingType must not be null");
        requireNonNull(builderMethodName, "builderMethodName must not be null");
        requireNonNull(builderFactoryMethodName, "builderFactoryMethodName must not be null");

        try {
            builderFactoryMethod = enclosingType.getDeclaredMethod(builderFactoryMethodName);
            builderClass = builderFactoryMethod.getReturnType();
        } catch (NoSuchMethodException | SecurityException e) {
            final var message = UNABLE_TO_ACCESS_METHOD.formatted(builderFactoryMethodName, enclosingType.getName(),
                extractCauseMessageFromThrowable(e));
            log.error(message, e);
            throw new AssertionError(message, e);
        }

        try {
            builderMethod = builderClass.getDeclaredMethod(builderMethodName);
            targetClass = (Class<T>) builderMethod.getReturnType();
        } catch (NoSuchMethodException | SecurityException e) {
            final var message = UNABLE_TO_ACCESS_METHOD.formatted(builderMethodName, builderClass,
                extractCauseMessageFromThrowable(e));
            log.error(message, e);
            throw new AssertionError(message, e);
        }

    }

    @Override
    public Object newBuilderInstance() {
        try {
            return builderFactoryMethod.invoke(null);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            final var message = UNABLE_TO_ACCESS_METHOD.formatted(builderFactoryMethod.getName(), targetClass,
                extractCauseMessageFromThrowable(e));
            log.error(message, e);
            throw new AssertionError(message, e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T build(final Object builder) {
        try {
            return (T) builderMethod.invoke(builder);
        } catch (IllegalAccessException | InvocationTargetException | RuntimeException e) {
            final var message = UNABLE_TO_ACCESS_METHOD.formatted(builderMethod.getName(), builderClass.getName(),
                extractCauseMessageFromThrowable(e));
            log.debug(message, e);
            throw new AssertionError(message, e);
        }
    }
}
