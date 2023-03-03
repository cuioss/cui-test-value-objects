package de.cuioss.test.valueobjects.generator.dynamic.impl;

import java.util.Optional;

import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.tools.logging.CuiLogger;
import javassist.util.proxy.ProxyFactory;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Creates proxies using javassist for any type given that is not an interface nor annotation nor
 * enum.
 *
 * @author Oliver Wolff
 * @param <T> the type of objects to be generated
 */
@ToString(of = "wrappedGenerator")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DynamicProxyGenerator<T> implements TypedGenerator<T> {

    private static final CuiLogger log = new CuiLogger(DynamicProxyGenerator.class);

    @NonNull
    private final Class<T> type;

    private final TypedGenerator<T> wrappedGenerator;

    @Override
    public T next() {
        return this.wrappedGenerator.next();
    }

    @Override
    public Class<T> getType() {
        return this.type;
    }

    /**
     * Factory method for creating an instance of {@link DynamicProxyGenerator}.
     * It only works for any type given that is not an interface nor annotation nor enum.
     *
     * @param type to be checked,
     * @return an {@link Optional} on the corresponding {@link TypedGenerator} if the given type is
     *         applicable, otherwise {@link Optional#empty()}
     */
    public static final <T> Optional<TypedGenerator<T>> getGeneratorForType(
            final Class<T> type) {
        if (null == type || type.isAnnotation() || type.isInterface() || type.isEnum()) {
            return Optional.empty();
        }
        final var proxyFactory = new ProxyFactory();
        proxyFactory.setSuperclass(type);
        proxyFactory.setFilter(m -> "equals".equals(m.getName()));

        Class<?> createClassType = proxyFactory.createClass();
        @SuppressWarnings("unchecked")
        final Optional<TypedGenerator<T>> constructorGenerator =
            ConstructorBasedGenerator.getGeneratorForType((Class<T>) createClassType);
        if (constructorGenerator.isPresent()) {
            return Optional.of(new DynamicProxyGenerator<>(type, constructorGenerator.get()));
        }
        log.warn("Unable to determine generator for type " + type);
        return Optional.empty();
    }

}
