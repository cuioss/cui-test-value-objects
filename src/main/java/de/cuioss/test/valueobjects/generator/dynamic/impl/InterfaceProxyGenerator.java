package de.cuioss.test.valueobjects.generator.dynamic.impl;

import java.lang.reflect.InvocationHandler;
import java.util.Optional;

import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.tools.reflect.MoreReflection;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Creates proxies for given interfaces, should only be used as last line of defense.
 *
 * @author Oliver Wolff
 * @param <T> the type of objects to be generated
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class InterfaceProxyGenerator<T> implements TypedGenerator<T> {

    private static final InvocationHandler DEFAULT_HANDLER = new DefaultInvocationHandler();

    private final Class<T> type;

    @Override
    public T next() {
        return MoreReflection.newProxy(this.type, DEFAULT_HANDLER);
    }

    @Override
    public Class<T> getType() {
        return this.type;
    }

    /**
     * Factory method for creating an instance of {@link InterfaceProxyGenerator}.
     * It only works with interfaces.
     *
     * @param type to be checked, should be an interface
     * @return an {@link Optional} on the corresponding {@link TypedGenerator} if the given type is
     *         an interfaces, otherwise {@link Optional#empty()}
     */
    public static final <T> Optional<TypedGenerator<T>> getGeneratorForType(
            final Class<T> type) {
        if (null == type || type.isAnnotation() || !type.isInterface()) {
            return Optional.empty();
        }
        return Optional.of(new InterfaceProxyGenerator<>(type));
    }
}
