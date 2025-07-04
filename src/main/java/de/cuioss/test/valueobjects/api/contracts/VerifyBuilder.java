/**
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.api.contracts;

import de.cuioss.test.valueobjects.api.property.PropertyBuilderConfig;
import de.cuioss.test.valueobjects.api.property.PropertyConfig;
import de.cuioss.test.valueobjects.contract.BuilderContractImpl;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl.PropertyMetadataBuilder;
import de.cuioss.test.valueobjects.property.util.AssertionStrategy;
import de.cuioss.tools.property.PropertyMemberInfo;
import de.cuioss.tools.property.PropertyReadWrite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

/**
 * If used on ValueObjectTest this test checks / tests builder of value objects.
 * <p>
 * As default the test assumes a static factory method with the name "builder",
 * e.g. {@link PropertyMetadataImpl#builder()} being present on the type to be
 * tested. This can be controlled using {@link #builderFactoryMethodName()},
 * {@link #builderFactoryProvidingClass()} and {@link #builderMethodName()}. In
 * case you have a builder that can be instantiated directly by using a no-args
 * public constructor directly you can use {@link #builderClass()} instead.
 * </p>
 * <p>
 * The builder methods for the individual properties are assumed to use the
 * property-name only as method-name, e.g.
 * {@link PropertyMetadataBuilder#propertyClass(Class)}. In case you prefer a
 * prefix you can either configure it using {@link #methodPrefix()} or can do
 * some more fine grained configuration with {@link PropertyBuilderConfig}
 * </p>
 *
 * @author Oliver Wolff
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface VerifyBuilder {

    /**
     * @return the name of the factory method for creating the builder instances. It
     *         is assumed that it is parameter-free static method. If not set it
     *         defaults to
     *         {@value BuilderContractImpl#DEFAULT_BUILDER_FACTORY_METHOD_NAME}
     */
    String builderFactoryMethodName() default BuilderContractImpl.DEFAULT_BUILDER_FACTORY_METHOD_NAME;

    /**
     * @return the name of the build method on the {@link #builderClass()}. It is
     *         assumed that it is parameter-free method on the builderType and
     *         returns instances of the type on unit class level of ValueObjectTest.
     *         Defaults to {@value BuilderContractImpl#DEFAULT_BUILD_METHOD_NAME}
     */
    String builderMethodName() default BuilderContractImpl.DEFAULT_BUILD_METHOD_NAME;

    /**
     * This attribute defines the actual type of the concrete builder. The default
     * value {@link VerifyBuilder} acts as {@code null} value. If it is set it will
     * be used instead of any factory. It is assumed to have a no args public
     * constructor.
     *
     * @return the builder-class
     */
    Class<?> builderClass() default VerifyBuilder.class;

    /**
     * Optional attribute. The default assumes that the factory method for creating
     * the concrete builder is located at the object under test. This method is only
     * necessary if it is located on another type, e.g the actual unit-test-class.
     * The default value {@link VerifyBuilder} acts as null value
     *
     * @return the type where the builderFactoryMethod is defined, the name of this
     *         method is defined within {@link #builderFactoryMethodName()}
     */
    Class<?> builderFactoryProvidingClass() default VerifyBuilder.class;

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

    /**
     * In case methodPrefix is not set the corresponding build method to be accessed
     * for setting the value is the name of the attribute: propertyName(), in case
     * it is a concrete value, e.g. 'with' it will taken into account:
     * withPropertName().
     *
     * @return the method prefix, defaults to empty string
     */
    String methodPrefix() default "";

}
