/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.contract.support;

import de.cuioss.test.valueobjects.property.PropertySupport;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Combines the {@link PropertySupport} for source and target with the
 * associated {@link MappingTuple}
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
