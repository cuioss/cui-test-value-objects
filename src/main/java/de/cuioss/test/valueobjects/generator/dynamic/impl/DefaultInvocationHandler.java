package de.cuioss.test.valueobjects.generator.dynamic.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * Abstract implementation of {@link InvocationHandler} that handles
 * {@link Object#equals}, {@link Object#hashCode} and {@link Object#toString}.:
 *
 * @author <a href=
 *         "https://github.com/google/guava/blob/master/guava/src/com/google/common/reflect/DefaultInvocationHandler.java">...</a>
 */
class DefaultInvocationHandler implements InvocationHandler {

    private static final Object[] NO_ARGS = {};

    /**
     * {@inheritDoc}
     *
     * <ul>
     * <li>{@code proxy.hashCode()} delegates to
     * {@link DefaultInvocationHandler#hashCode}
     * <li>{@code proxy.toString()} delegates to
     * {@link DefaultInvocationHandler#toString}
     * <li>{@code proxy.equals(argument)} returns true if:
     * <ul>
     * <li>{@code proxy} and {@code argument} are of the same type
     * <li>and {@link DefaultInvocationHandler#equals} returns true for the
     * {@link InvocationHandler} of {@code argument}
     * </ul>
     * <li>other method calls return null.
     * </ul>
     */
    @Override
    public final Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (args == null) {
            args = NO_ARGS;
        }
        if (args.length == 0 && method.getName().equals("hashCode")) {
            return hashCode();
        }
        if (args.length == 1 && method.getName().equals("equals") && method.getParameterTypes()[0] == Object.class) {
            var arg = args[0];
            if (arg == null) {
                return false;
            }
            if (proxy == arg) {
                return true;
            }
            return isProxyOfSameInterfaces(arg, proxy.getClass()) && equals(Proxy.getInvocationHandler(arg));
        }
        if (args.length == 0 && method.getName().equals("toString")) {
            return toString();
        }
        return null;
    }

    /**
     * By default delegates to {@link Object#equals} so instances are only equal if
     * they are identical. {@code proxy.equals(argument)} returns true if:
     *
     * <ul>
     * <li>{@code proxy} and {@code argument} are of the same type
     * <li>and this method returns true for the {@link InvocationHandler} of
     * {@code argument}
     * </ul>
     *
     * <p>
     * Subclasses can override this method to provide custom equality.
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * By default delegates to {@link Object#hashCode}. The dynamic proxies'
     * {@code hashCode()} will delegate to this method. Subclasses can override this
     * method to provide custom equality.
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * By default delegates to {@link Object#toString}. The dynamic proxies'
     * {@code toString()} will delegate to this method. Subclasses can override this
     * method to provide custom string representation for the proxies.
     */
    @Override
    public String toString() {
        return super.toString();
    }

    private static boolean isProxyOfSameInterfaces(Object arg, Class<?> proxyClass) {
        return proxyClass.isInstance(arg)
                // Equal proxy instances should mostly be instance of proxyClass
                // Under some edge cases (such as the proxy of JDK types serialized and then
                // deserialized)
                // the proxy type may not be the same.
                // We first check isProxyClass() so that the common case of comparing with
                // non-proxy
                // objects
                // is efficient.
                || Proxy.isProxyClass(arg.getClass())
                        && Arrays.equals(arg.getClass().getInterfaces(), proxyClass.getInterfaces());
    }
}
