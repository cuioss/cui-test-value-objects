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
package de.cuioss.test.valueobjects.property.util;

import java.util.Collection;

import de.cuioss.test.valueobjects.property.PropertySupport;

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
