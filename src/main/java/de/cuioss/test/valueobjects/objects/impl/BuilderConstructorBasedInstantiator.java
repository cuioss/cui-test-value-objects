package de.cuioss.test.valueobjects.objects.impl;

import static de.cuioss.test.valueobjects.objects.impl.ExceptionHelper.extractCauseMessageFromThrowable;
import static de.cuioss.tools.string.MoreStrings.emptyToNull;
import static java.util.Objects.requireNonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.cuioss.test.valueobjects.contract.BuilderContractImpl;
import de.cuioss.test.valueobjects.objects.BuilderInstantiator;
import de.cuioss.test.valueobjects.objects.ObjectInstantiator;
import lombok.Getter;
import lombok.ToString;

/**
 * Used for creating instances of a builder. This variant relies on the
 * builder-class having a parameter-free constructor.
 *
 * @author Oliver Wolff
 * @param <T> identifying the type of the {@link Object} created by the builder
 */
@ToString
public class BuilderConstructorBasedInstantiator<T> implements BuilderInstantiator<T> {

    private final Method builderMethod;

    @Getter
    private final Class<T> targetClass;

    @Getter
    private final Class<?> builderClass;

    private final ObjectInstantiator<?> builderInstantiator;

    /**
     * Constructor. Shortcut for calling
     * {@link #BuilderConstructorBasedInstantiator(Class, String)} with
     * {@link BuilderContractImpl#DEFAULT_BUILD_METHOD_NAME}
     *
     * @param builderType identifying the actual type of the builder. It is assumed
     *                    that it provides is parameter-free public constructor
     */
    public BuilderConstructorBasedInstantiator(final Class<?> builderType) {
        this(builderType, BuilderContractImpl.DEFAULT_BUILD_METHOD_NAME);
    }

    /**
     * Constructor.
     *
     * @param builderType     identifying the actual type of the builder. It is
     *                        assumed that it provides is parameter-free public
     *                        constructor
     * @param buildMethodName the actual name or the builder-method, must not be
     *                        null nor empty
     */
    @SuppressWarnings("unchecked")
    public BuilderConstructorBasedInstantiator(final Class<?> builderType, final String buildMethodName) {

        requireNonNull(builderType, "builderType must not be null");
        requireNonNull(emptyToNull(buildMethodName), "builderMethodName must not be null");

        builderClass = builderType;
        builderInstantiator = new DefaultInstantiator<>(builderType);

        try {
            builderMethod = builderInstantiator.getTargetClass().getDeclaredMethod(buildMethodName);
            targetClass = (Class<T>) builderMethod.getReturnType();
        } catch (NoSuchMethodException | SecurityException e) {
            throw new AssertionError("Unable to access method " + buildMethodName + " on type " + builderType.getName()
                    + ", due to " + extractCauseMessageFromThrowable(e), e);
        }

    }

    @Override
    public Object newBuilderInstance() {
        return builderInstantiator.newInstance();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T build(final Object builder) {
        try {
            return (T) builderMethod.invoke(builder);
        } catch (IllegalAccessException | InvocationTargetException | RuntimeException e) {
            throw new AssertionError("Unable to access method " + builderMethod.getName() + " on type "
                    + getBuilderClass().getName() + ", due to " + extractCauseMessageFromThrowable(e), e);
        }
    }
}
