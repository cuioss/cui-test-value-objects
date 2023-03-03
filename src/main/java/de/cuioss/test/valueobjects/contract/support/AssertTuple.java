package de.cuioss.test.valueobjects.contract.support;

import de.cuioss.test.valueobjects.property.PropertySupport;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Combines the {@link PropertySupport} for source and target with the associated
 * {@link MappingTuple}
 *
 * @author Oliver Wolff
 *
 */
@Value
@Builder
class AssertTuple {

    @NonNull
    private final PropertySupport sourceSupport;

    @NonNull
    private final PropertySupport targetSupport;

    @NonNull
    private final MappingTuple mappingTuple;

    /**
     * Asserts the contained contract
     *
     * @param sourceObject
     * @param targetObject
     */
    void assertContract(Object sourceObject, Object targetObject) {
        mappingTuple.getStrategy().assertMapping(sourceSupport, sourceObject, targetSupport, targetObject);
    }

    /**
     * Shortcut for querying a concrete {@link MappingTuple#getSource()}
     *
     * @param source to be check, must not be null.
     * @return boolean indicating whether the contained source
     */
    boolean isResponsibleForSource(String source) {
        return mappingTuple.getSource().equals(source);
    }
}
