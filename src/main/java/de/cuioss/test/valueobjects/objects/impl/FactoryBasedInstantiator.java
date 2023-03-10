package de.cuioss.test.valueobjects.objects.impl;

import static de.cuioss.test.valueobjects.objects.impl.ExceptionHelper.extractCauseMessageFromThrowable;
import static de.cuioss.tools.base.Preconditions.checkArgument;
import static de.cuioss.tools.string.MoreStrings.isEmpty;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.tools.logging.CuiLogger;

/**
 * This {@link ParameterizedInstantiator} uses a factory method derived by the given
 * {@link RuntimeProperties#getAllProperties()} in order to instantiate {@link Object}s
 *
 * @author Oliver Wolff
 * @param <T> identifying the type of objects to be created
 */
public class FactoryBasedInstantiator<T> extends AbstractOrderedArgsInstantiator<T> {

    private static final CuiLogger log = new CuiLogger(FactoryBasedInstantiator.class);

    private final Method factoryMethod;

    /**
     * Constructor.
     *
     * @param type the type of the object to be created
     * @param runtimeProperties must not be null. defines the attributes in the exact order
     *            to be used for the constructor:
     *            {@link RuntimeProperties#getAllProperties()}
     * @param enclosingType the type providing the factory method, usually the target type to be
     *            created, must not be null
     * @param factoryMethodName the name of the factory method, must not be null, nor empty.
     */
    public FactoryBasedInstantiator(
            final Class<T> type,
            final RuntimeProperties runtimeProperties,
            final Class<?> enclosingType,
            final String factoryMethodName) {

        super(runtimeProperties);
        requireNonNull(type, "type must not be null");
        requireNonNull(enclosingType, "enclosingType must not be null");
        requireNonNull(factoryMethodName);
        checkArgument(!isEmpty(factoryMethodName),
                "factoryMethodName msut not be null nor empty");

        final List<Class<?>> parameter = new ArrayList<>();
        runtimeProperties.getAllProperties()
                .forEach(meta -> parameter.add(meta.resolveActualClass()));
        try {
            if (parameter.isEmpty()) {
                this.factoryMethod = enclosingType.getDeclaredMethod(factoryMethodName);
            } else {
                this.factoryMethod =
                    enclosingType.getDeclaredMethod(factoryMethodName, toClassArray(parameter));
            }
            assertNotNull(

                    this.factoryMethod, "Unable to find a factory method with signature " + parameter + " and name "
                            + factoryMethodName);
            assertTrue(type.isAssignableFrom(this.factoryMethod.getReturnType()),
                    "Invalid type found on factory method: " + this.factoryMethod.getReturnType());
        } catch (NoSuchMethodException | SecurityException e) {
            final var message = new StringBuilder("Unable to find a constructor with signature ")
                    .append(parameter).toString();
            log.error(message, e);
            throw new AssertionError(message);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected T doInstantiate(final Object... args) {
        try {
            return (T) this.factoryMethod.invoke(null, args);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            final var message = new StringBuilder("Unable to invoke constructor ")
                    .append(", due to ").append(extractCauseMessageFromThrowable(e)).toString();
            throw new AssertionError(message, e);
        }
    }

    @Override
    public String toString() {
        final var builder = new StringBuilder(getClass().getName());
        builder.append("\nFactory Method: ").append(this.factoryMethod);
        builder.append("\nProperty Configuration: ")
                .append(getRuntimeProperties().toString());
        return builder.toString();
    }
}
