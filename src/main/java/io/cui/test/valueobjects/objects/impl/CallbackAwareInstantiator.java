package io.cui.test.valueobjects.objects.impl;

import java.util.List;

import io.cui.test.valueobjects.objects.ConfigurationCallBackHandler;
import io.cui.test.valueobjects.objects.ParameterizedInstantiator;
import io.cui.test.valueobjects.objects.RuntimeProperties;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.PropertySupport;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Delegate to {@link ParameterizedInstantiator} that calls
 * {@link ConfigurationCallBackHandler#configure(Object)} after creation of the object.
 *
 * @author Oliver Wolff
 * @param <T> identifying the type of the objects to be instantiated
 */
@RequiredArgsConstructor
@ToString(of = "parameterizedInstantiator")
public class CallbackAwareInstantiator<T> implements ParameterizedInstantiator<T> {

    private final ParameterizedInstantiator<T> parameterizedInstantiator;
    private final ConfigurationCallBackHandler<T> callBackHandler;

    @Override
    public T newInstance(final List<PropertySupport> properties,
            final boolean generatePropertyValues) {
        final T instance = parameterizedInstantiator.newInstance(properties, generatePropertyValues);
        callBackHandler.configure(instance);
        return instance;
    }

    @Override
    public T newInstance(final List<PropertyMetadata> properties) {
        final T instance = parameterizedInstantiator.newInstance(properties);
        callBackHandler.configure(instance);
        return instance;
    }

    @Override
    public T newInstanceMinimal() {
        final T instance = parameterizedInstantiator.newInstanceMinimal();
        callBackHandler.configure(instance);
        return instance;
    }

    @Override
    public T newInstanceFull() {
        final T instance = parameterizedInstantiator.newInstanceFull();
        callBackHandler.configure(instance);
        return instance;
    }

    @Override
    public RuntimeProperties getRuntimeProperties() {
        return parameterizedInstantiator.getRuntimeProperties();
    }

}