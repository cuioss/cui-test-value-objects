package io.cui.test.valueobjects.objects.impl;

import static io.cui.test.valueobjects.objects.impl.ExceptionHelper.extractCauseMessageFromThrowable;
import static java.util.Objects.requireNonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.cui.test.valueobjects.contract.BuilderContractImpl;
import io.cui.test.valueobjects.objects.BuilderInstantiator;
import io.cui.tools.logging.CuiLogger;
import lombok.Getter;
import lombok.ToString;

/**
 * Used for creating instances of a builder. This variant relies on a factory method on the target
 * type usually with the name "builder". See
 * {@link BuilderFactoryBasedInstantiator#BuilderFactoryBasedInstantiator(Class, String, String)}
 * for details
 *
 * @author Oliver Wolff
 * @param <T> identifying the type of the {@link Object} created by the builder
 */
@ToString
public class BuilderFactoryBasedInstantiator<T> implements BuilderInstantiator<T> {

    private static final String UNABLE_TO_ACCESS_METHOD =
        "Unable to access method %s on type %s, due to %s";

    private static final CuiLogger log = new CuiLogger(BuilderFactoryBasedInstantiator.class);

    private final Method builderFactoryMethod;
    private final Method builderMethod;

    @Getter
    private final Class<T> targetClass;

    @Getter
    private final Class<?> builderClass;

    /**
     * Constructor.
     * shortcut for calling {@link #BuilderFactoryBasedInstantiator(Class, String, String)} with
     * {@link BuilderContractImpl#DEFAULT_BUILDER_FACTORY_METHOD_NAME} and
     * {@value BuilderContractImpl#DEFAULT_BUILD_METHOD_NAME}
     *
     * @param enclosingType identifying the type where the method is located in, must not be null.
     */
    public BuilderFactoryBasedInstantiator(final Class<?> enclosingType) {
        this(enclosingType, BuilderContractImpl.DEFAULT_BUILDER_FACTORY_METHOD_NAME,
                BuilderContractImpl.DEFAULT_BUILD_METHOD_NAME);
    }

    /**
     * Constructor.
     *
     * @param enclosingType identifying the type where the method is located in, must not be null.
     * @param builderFactoryMethodName The name of the factory method on the enclosing type. It is
     *            assumed that it is parameter-free static method
     * @param builderMethodName the actual name or the builder-method
     */
    @SuppressWarnings("unchecked")
    public BuilderFactoryBasedInstantiator(final Class<?> enclosingType,
            final String builderFactoryMethodName, final String builderMethodName) {

        requireNonNull(enclosingType, "enclosingType must not be null");
        requireNonNull(builderMethodName, "builderMethodName must not be null");
        requireNonNull(builderFactoryMethodName, "builderFactoryMethodName must not be null");

        try {
            this.builderFactoryMethod = enclosingType.getDeclaredMethod(builderFactoryMethodName);
            this.builderClass = this.builderFactoryMethod.getReturnType();
        } catch (NoSuchMethodException | SecurityException e) {
            final String message = String.format(UNABLE_TO_ACCESS_METHOD,
                    builderFactoryMethodName, enclosingType.getName(),
                    extractCauseMessageFromThrowable(e));
            log.error(message, e);
            throw new AssertionError(message, e);
        }

        try {
            this.builderMethod = this.builderClass.getDeclaredMethod(builderMethodName);
            this.targetClass = (Class<T>) this.builderMethod.getReturnType();
        } catch (NoSuchMethodException | SecurityException e) {
            final String message = String.format(UNABLE_TO_ACCESS_METHOD,
                    builderMethodName, this.builderClass, extractCauseMessageFromThrowable(e));
            log.error(message, e);
            throw new AssertionError(message, e);
        }

    }

    @Override
    public Object newBuilderInstance() {
        try {
            return this.builderFactoryMethod.invoke(null);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            final String message = String.format(UNABLE_TO_ACCESS_METHOD,
                    this.builderFactoryMethod.getName(), targetClass,
                    extractCauseMessageFromThrowable(e));
            log.error(message, e);
            throw new AssertionError(message, e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T build(final Object builder) {
        try {
            return (T) this.builderMethod.invoke(builder);
        } catch (IllegalAccessException | InvocationTargetException | RuntimeException e) {
            final String message = String.format(UNABLE_TO_ACCESS_METHOD,
                    this.builderMethod.getName(), this.builderClass.getName(),
                    extractCauseMessageFromThrowable(e));
            log.debug(message, e);
            throw new AssertionError(message, e);
        }
    }
}
