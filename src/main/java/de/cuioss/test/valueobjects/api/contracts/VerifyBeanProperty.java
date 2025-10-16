/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.api.contracts;

import de.cuioss.test.valueobjects.api.property.PropertyConfig;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.util.AssertionStrategy;
import de.cuioss.tools.property.PropertyMemberInfo;
import de.cuioss.tools.property.PropertyReadWrite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

/**
 * If used on ValueObjectTest this test checks / tests all Bean-properties.
 * <p>
 * <em>Caution:</em> The implementation for this contract assumes a valid bean
 * regarding Java-Bean Spec, therefore a no-args public constructor is required.
 * </p>
 * <p>
 * In essence it checks the getters and setters. As default it assumes the
 * individual properties to not provide a default value. This can be controlled
 * using {@link #defaultValued()}
 * </p>
 *
 * @author Oliver Wolff
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface VerifyBeanProperty {

    /**
     * @return an array of properties, identified by their names that are not to be
     *         considered for this test: black-list
     */
    String[] exclude() default {};

    /**
     * @return an array of properties, identified by their names that are to be
     *         considered for this test: white-list
     */
    String[] of() default {};

    /**
     * @return an array of properties, identified by their names that are to be
     *         treated as required properties, see
     *         {@link PropertyMetadata#isRequired()}
     */
    String[] required() default {};

    /**
     * @return an array of properties, identified by their names that are to be
     *         treated as transient properties, see
     *         {@link PropertyMemberInfo#TRANSIENT}
     */
    String[] transientProperties() default {};

    /**
     * @return an array of properties, identified by their names that are to be
     *         treated as having a default values, see
     *         {@link PropertyMetadata#isDefaultValue()}
     */
    String[] defaultValued() default {};

    /**
     * @return an array of properties, identified by their names that are to be
     *         treated as being read-only, see {@link PropertyReadWrite#READ_ONLY},
     *         usually used in conjunction with {@link #defaultValued()}
     */
    String[] readOnly() default {};

    /**
     * @return an array of properties, identified by their names that are to be
     *         treated as being write-only, see
     *         {@link PropertyReadWrite#WRITE_ONLY}, usually used in cases where a
     *         property to be written will result in other properties but itself can
     *         not be accessed directly
     */
    String[] writeOnly() default {};

    /**
     * @return an array of properties, identified by their names representing at
     *         least a {@link Collection} that are to be asserted ignoring the
     *         concrete order, see {@link PropertyConfig#assertionStrategy()} and
     *         {@link AssertionStrategy#COLLECTION_IGNORE_ORDER}. The default
     *         implementation will always respect / assert the same order of
     *         elements.
     */
    String[] assertUnorderedCollection() default {};
}
