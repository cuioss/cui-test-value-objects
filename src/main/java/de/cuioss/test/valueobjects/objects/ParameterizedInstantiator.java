package de.cuioss.test.valueobjects.objects;

import java.util.List;

import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.PropertySupport;

/**
 * Abstraction of certain ways for creating and populating test-objects .
 *
 * @author Oliver Wolff
 * @param <T> identifying the type of object to be instantiated
 */
public interface ParameterizedInstantiator<T> {

    /**
     * Creates a new Object according to the given parameter, represented as
     * {@link List} of {@link PropertySupport}
     *
     * @param properties             to be applied to the newly created object.
     * @param generatePropertyValues indicating whether this method should
     *                               implicitly call
     *                               {@link PropertySupport#generateTestValue()} or
     *                               not. In case being <code>false</code> the
     *                               caller must ensure this.
     * @return a newly created object with the given properties being applied.
     * @throws AssertionError in case the object could not be created or the
     *                        properties could not been applied.
     */
    T newInstance(List<PropertySupport> properties, boolean generatePropertyValues);

    /**
     * Similar to {@link #newInstance(List, boolean)} but with the difference that
     * the caller has no control regarding the set / applied properties, that may be
     * need for some asserts. It is some kind of any()
     *
     * @param properties to be applied to the newly created object.
     * @return a newly created object with the given properties being applied.
     * @throws AssertionError in case the object could not be created or the
     *                        properties could not been applied.
     */
    T newInstance(List<PropertyMetadata> properties);

    /**
     * @return the runtime information associated with this
     *         {@link ParameterizedInstantiator}
     */
    RuntimeProperties getRuntimeProperties();

    /**
     * @return a new instance of the object with only required attributes set.
     */
    T newInstanceMinimal();

    /**
     * @return a new instance of the object with all attributes set.
     */
    T newInstanceFull();
}
