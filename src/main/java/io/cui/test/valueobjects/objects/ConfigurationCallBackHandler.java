package io.cui.test.valueobjects.objects;

/**
 * Defines a callback-method in case of further configuration of concrete
 * test-objects. After initialization and prior to testing the method {@link #configure(Object)}
 * will be called allowing the concrete test-class to do some specific configuration.
 *
 * @author Oliver Wolff
 * @param <T> identifying the type to be configured
 */
public interface ConfigurationCallBackHandler<T> {

    /**
     * Callback method for configuring a concrete created test-object. The default implementation is
     * NOOP
     *
     * @param toBeConfigured must never be null
     */
    default void configure(final T toBeConfigured) {
    }

}
