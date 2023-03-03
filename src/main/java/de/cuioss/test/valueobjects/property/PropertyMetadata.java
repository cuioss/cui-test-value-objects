package de.cuioss.test.valueobjects.property;

import java.io.Serializable;

import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.generator.impl.CollectionGenerator;
import de.cuioss.test.valueobjects.property.util.AssertionStrategy;
import de.cuioss.test.valueobjects.property.util.CollectionType;
import de.cuioss.test.valueobjects.property.util.PropertyAccessStrategy;
import de.cuioss.tools.property.PropertyMemberInfo;
import de.cuioss.tools.property.PropertyReadWrite;

/**
 * Represents runtime information for a certain property. This contains all metadata that may be
 * needed.
 *
 * @author Oliver Wolff
 */
public interface PropertyMetadata extends Comparable<PropertyMetadata> {

    /**
     * Identifies the name of the property
     *
     * @return the actual name of the property, must never be null nor empty
     */
    String getName();

    /**
     * @return the type of the property. This can either be the actual type, in case
     *         {@link #getCollectionType()} is {@link CollectionType#NO_ITERABLE}, the
     *         component-type in case of {@link CollectionType#ARRAY_MARKER} or the type argument
     *         for a collection for the other {@link CollectionType}s, see {@link #next()} and
     *         {@link #resolveActualClass()}
     */
    Class<?> getPropertyClass();

    /**
     * Generates a next random value
     * Similar to {@link TypedGenerator#next()} but in case there is a {@link #getCollectionType()}
     * that is not {@link CollectionType#NO_ITERABLE} it wraps the content of the contained
     * generator into a the corresponding collectionWrapper, see
     * {@link CollectionType#nextIterable(CollectionGenerator)}
     *
     * @return the next generated value
     */
    Object next();

    /**
     * @return The actual class of the property. PropertyMetadata can always be used directly or the
     *         data can be implicitly wrapped into a collection or an array. This method computes
     *         the actual class of the property combining both informations
     */
    Class<?> resolveActualClass();

    /**
     * @return a new instance of {@link CollectionGenerator} wrapping the contained generator;
     */
    @SuppressWarnings("squid:S1452") // owolff: No type information available at this level,
                                     // therefore the wildcard is needed
    CollectionGenerator<?> resolveCollectionGenerator();

    /**
     * @return the wrapped {@link TypedGenerator} to dynamically create properties.
     */
    @SuppressWarnings("squid:S1452") // owolff: No type information available at this level,
                                     // therefore the wildcard is needed
    TypedGenerator<?> getGenerator();

    /**
     * @return boolean indicating whether the attribute defines a default-value. Default is false.
     */
    boolean isDefaultValue();

    /**
     * Indicates that the attribute is required.
     *
     * @return boolean indicating whether the attribute is required, defaults to false.
     */
    boolean isRequired();

    /**
     * Defines the way properties are to be written / read
     *
     * @return the {@link PropertyAccessStrategy}, must never be null, defaults to
     *         {@link PropertyAccessStrategy#BEAN_PROPERTY}
     */
    PropertyAccessStrategy getPropertyAccessStrategy();

    /**
     * In case there is a collectionWrapper defined the generated values will implicitly wrapped
     * in the corresponding collection class defined by that wrapper.
     *
     * @return the {@link CollectionType} represented by this {@link PropertyMetadata}. Must never
     *         be null but may be {@link CollectionType#NO_ITERABLE}
     */
    CollectionType getCollectionType();

    /**
     * Indicates the way the corresponding property is subject to the contracts regarding the
     * canonical Object methods like {@link Object#equals(Object)}, {@link Object#hashCode()} and
     * {@link Object#toString()} and the {@link Serializable} contract. The default is
     * {@link PropertyMemberInfo#DEFAULT}
     *
     * @return the {@link PropertyMemberInfo}, must never be null
     */
    PropertyMemberInfo getPropertyMemberInfo();

    /**
     * @return {@link PropertyReadWrite} indicating whether a property can be read or written too,
     *         defaults to {@link PropertyReadWrite#READ_WRITE}
     */
    PropertyReadWrite getPropertyReadWrite();

    /**
     * Defines the the way how to deal with equality regarding
     * PropertySupport.assertValueSet(Object)
     *
     * @return the {@link AssertionStrategy}, defaults to {@link AssertionStrategy#DEFAULT}
     */
    AssertionStrategy getAssertionStrategy();

}
