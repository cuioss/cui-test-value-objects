package io.cui.test.valueobjects.property.util;

import java.util.Collection;

import io.cui.test.valueobjects.property.PropertySupport;

/**
 * Types of this object defines the the way how to deal with equality regarding
 * {@link PropertySupport#assertValueSet(Object)}
 *
 * @author Oliver Wolff
 */
public enum AssertionStrategy {

    /**
     * The default behavior, saying the comparison will be done with
     * {@link Object#equals(Object)}
     */
    DEFAULT,

    /** Indicates that a {@link Collection} will be compared ignoring the order */
    COLLECTION_IGNORE_ORDER,

}
