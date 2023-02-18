package io.cui.test.valueobjects.objects.impl;

import static io.cui.test.valueobjects.objects.impl.ExceptionHelper.extractCauseMessageFromThrowable;
import static java.util.Objects.requireNonNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import io.cui.test.valueobjects.objects.ParameterizedInstantiator;
import io.cui.test.valueobjects.objects.RuntimeProperties;
import io.cui.tools.logging.CuiLogger;

/**
 * This {@link ParameterizedInstantiator} uses a constructor derived by the given
 * {@link RuntimeProperties#getAllProperties()} in order to instantiate {@link Object}s
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
     * @param type identifying the actual type to be instantiated, must not be null
     * @param runtimeProperties must not be null. defines the attributes in the exact order
     *            to be used for the constructor:
     *            {@link RuntimeProperties#getAllProperties()}
     */
    public ConstructorBasedInstantiator(final Class<T> type,
            final RuntimeProperties runtimeProperties) {

        super(runtimeProperties);
        requireNonNull(type);

        final List<Class<?>> parameter = new ArrayList<>();
        runtimeProperties.getAllProperties()
                .forEach(meta -> parameter.add(meta.resolveActualClass()));
        try {
            if (parameter.isEmpty()) {
                this.constructor = type.getConstructor();
            } else {
                this.constructor = type.getConstructor(toClassArray(parameter));
            }
            requireNonNull(this.constructor,
                    "Unable to find a constructor with signature " + parameter);
        } catch (NoSuchMethodException | SecurityException e) {
            final var message = new StringBuilder("Unable to find a constructor with signature ")
                    .append(parameter).append(", for type ").append(type.getName()).toString();
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
            return this.constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            final var message = new StringBuilder("Unable to invoke constructor ")
                    .append(", due to ").append(extractCauseMessageFromThrowable(e)).toString();
            throw new AssertionError(message, e);
        }
    }

    @Override
    public String toString() {
        final var builder = new StringBuilder(getClass().getName());
        builder.append("\nConstructor: ").append(this.constructor);
        builder.append("\nProperty Configuration: ")
                .append(getRuntimeProperties().toString());
        return builder.toString();
    }
}
