package de.cuioss.test.valueobjects.objects.impl;

import java.util.Collections;
import java.util.List;

import de.cuioss.test.valueobjects.api.object.ObjectTestContract;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.PropertySupport;

/**
 * {@link ParameterizedInstantiator} for cases you actually are not able to use
 * the testing Infrastructure but want to benefit of the testing of the
 * {@link ObjectTestContract}s
 *
 * @author Oliver Wolff
 * @param <T> identifying the type of object to be instantiated
 */
public abstract class AbstractInlineInstantiator<T> implements ParameterizedInstantiator<T> {

    /** "Properties must not be null, but may be empty". */
    public static final String PROPERTIES_MUST_NOT_BE_NULL = "Properties must not be null, but may be empty";

    @Override
    public T newInstance(final List<PropertySupport> properties, final boolean generatePropertyValues) {
        return any();
    }

    @Override
    public T newInstance(final List<PropertyMetadata> properties) {
        return any();
    }

    @Override
    public RuntimeProperties getRuntimeProperties() {
        return new RuntimeProperties(Collections.emptyList());
    }

    @Override
    public T newInstanceMinimal() {
        return any();
    }

    @Override
    public T newInstanceFull() {
        return any();
    }

    protected abstract T any();
}
