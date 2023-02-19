package io.cui.test.valueobjects.objects.impl;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.cui.test.valueobjects.objects.ParameterizedInstantiator;
import io.cui.test.valueobjects.objects.RuntimeProperties;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.PropertySupport;
import lombok.Getter;

/**
 * Base class for creating objects with a fixed set of parameter to be used. This can be constructor
 * and or factory based {@link ParameterizedInstantiator}.
 *
 * @author Oliver Wolff
 * @param <T>
 */
public abstract class AbstractOrderedArgsInstantiator<T> implements ParameterizedInstantiator<T> {

    @Getter
    private final RuntimeProperties runtimeProperties;

    /**
     * Constructor.
     *
     * @param runtimeProperties must not be null. defines the attributes in the exact order
     *            to be used for the constructor:
     *            {@link RuntimeProperties#getAllProperties()}
     */
    protected AbstractOrderedArgsInstantiator(
            final RuntimeProperties runtimeProperties) {
        requireNonNull(runtimeProperties);

        this.runtimeProperties = runtimeProperties;
    }

    /**
     * Wraps the given {@link Class} elements to a corresponding array
     *
     * @param parameter may be null
     * @return the array
     */
    static Class<?>[] toClassArray(final List<Class<?>> parameter) {
        if (null == parameter || parameter.isEmpty()) {
            return new Class<?>[0];
        }
        final var parameterArray = new Class<?>[parameter.size()];
        for (var i = 0; i < parameterArray.length; i++) {
            parameterArray[i] = parameter.get(i);
        }
        return parameterArray;
    }

    @Override
    public T newInstance(final List<PropertySupport> properties,
            final boolean generatePropertyValues) {
        final Map<String, PropertySupport> given = new HashMap<>();
        properties.forEach(p -> given.put(p.getName(), p));
        final List<Object> parameter = new ArrayList<>();
        for (final PropertySupport support : resolveFixedArgumentList()) {
            if (given.containsKey(support.getName())) {
                // Use given element
                final var givenSupport =
                    given.get(support.getName());
                if (generatePropertyValues) {
                    givenSupport.generateTestValue();
                }
                parameter.add(givenSupport.getGeneratedValue());
            } else {
                if (null != support.getGeneratedValue()) {
                    // A Test value is already set
                } else if (support.isRequired() || support.isPrimitive()) {
                    support.generateTestValue();
                }
                parameter.add(support.getGeneratedValue());
            }
        }
        return doInstantiate(parameter.toArray());
    }

    /**
     * The actual instantiation method for the {@link Object}s
     *
     * @param args Object[] of parameter to be passed to Constructor / factory ,
     *            method
     * @return the instantiated {@link Object}
     */
    protected abstract T doInstantiate(Object... args);

    @Override
    public T newInstance(final List<PropertyMetadata> properties) {
        return newInstance(RuntimeProperties.mapToPropertySupport(properties, false),
                true);
    }

    @Override
    public T newInstanceMinimal() {
        return newInstance(getRuntimeProperties().getRequiredAsPropertySupport(false),
                true);
    }

    @Override
    public T newInstanceFull() {
        return newInstance(resolveFixedArgumentList(), true);
    }

    protected List<PropertySupport> resolveFixedArgumentList() {
        return getRuntimeProperties().getAllAsPropertySupport(false);
    }

}
