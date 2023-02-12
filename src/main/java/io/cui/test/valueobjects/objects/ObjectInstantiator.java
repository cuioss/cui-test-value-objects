package io.cui.test.valueobjects.objects;

import io.cui.test.valueobjects.objects.impl.DefaultInstantiator;

/**
 * An {@link ObjectInstantiator} creates objects without further parameter, e.g. the
 * {@link DefaultInstantiator} uses the Default-Constructor
 *
 * @author Oliver Wolff
 * @param <T>
 */
public interface ObjectInstantiator<T> {

    /**
     * @return a newly created instance
     */
    T newInstance();

    /**
     * @return the type of object to be generated
     */
    Class<T> getTargetClass();

}
