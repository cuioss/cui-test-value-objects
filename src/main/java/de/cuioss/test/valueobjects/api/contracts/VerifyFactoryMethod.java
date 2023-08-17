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
package de.cuioss.test.valueobjects.api.contracts;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

import de.cuioss.test.valueobjects.api.property.PropertyConfig;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.util.AssertionStrategy;
import de.cuioss.tools.property.PropertyMemberInfo;
import de.cuioss.tools.property.PropertyReadWrite;

/**
 * If used on ValueObjectTest this test checks / tests factory-methods.
 * <p>
 * In order to define the arguments in the correct order you need to pass them
 * using {@link #of()}
 * </p>
 * <p>
 * As default it assumes the individual arguments to be optional, saying null is
 * allowed. This can be controlled by {@link #required()}
 * </p>
 *
 * @author Oliver Wolff
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Repeatable(VerifyFactoryMethods.class)
public @interface VerifyFactoryMethod {

    /**
     * @return the name of the factory method to be called
     */
    String factoryMethodName();

    /**
     * @return the (optional) type where the factory method is declared. If it is
     *         not set it is assumed to be located at the actual type under test.
     *         The default {@link VerifyFactoryMethod} acts as null value.
     */
    Class<?> enclosingType() default VerifyFactoryMethod.class;

    /**
     * @return an array of properties, identified by their names that are to be
     *         considered for this test: Caution: The order of the names must
     *         exactly match the order of the attributes of corresponding
     *         constructor.
     */
    String[] of();

    /**
     * @return an array of properties, identified by their names that are to be
     *         treated as required properties, see
     *         {@link PropertyMetadata#isRequired()}. it is used
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
