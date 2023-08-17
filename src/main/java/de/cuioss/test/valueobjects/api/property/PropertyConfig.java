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
package de.cuioss.test.valueobjects.api.property;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.valueobjects.generator.dynamic.DynamicTypedGenerator;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.util.AssertionStrategy;
import de.cuioss.test.valueobjects.property.util.CollectionType;
import de.cuioss.test.valueobjects.property.util.PropertyAccessStrategy;
import de.cuioss.tools.property.PropertyMemberInfo;
import de.cuioss.tools.property.PropertyReadWrite;

/**
 * While the test classes are capable of auto-detecting JavaProperties you need
 * to adjust them from time to time. With this annotation you can do this.
 *
 * @author Oliver Wolff
 */
@Retention(RUNTIME)
@Target(TYPE)
@Repeatable(PropertyConfigs.class)
public @interface PropertyConfig {

    /**
     * Identifies the name of the property
     *
     * @return the actual name of the property, must never be null nor empty
     * @see {@link PropertyMetadata#getName()}.
     */
    String name();

    /**
     * @return the type of the property. This can either be the actual type, in case
     *         {@link PropertyMetadata#getCollectionType()} is
     *         {@link CollectionType#NO_ITERABLE}, the component-type in case of
     *         {@link CollectionType#ARRAY_MARKER} or the type argument for a
     *         collection for the other {@link CollectionType}s, see
     *         {@link PropertyMetadata#next()} and
     *         {@link PropertyMetadata#resolveActualClass()}
     * @see {@link PropertyMetadata#getPropertyClass()}.
     */
    Class<?> propertyClass();

    /**
     * @return the wrapped {@link TypedGenerator} to dynamically create properties.
     *         If it is not set {@link DynamicTypedGenerator} will be chosen
     * @see {@link PropertyMetadata#getGenerator()}.
     */
    @SuppressWarnings("rawtypes")
    Class<? extends TypedGenerator> generator() default DynamicTypedGenerator.class;

    /**
     * @return boolean indicating whether the property defines a default value,
     *         defaults to false
     * @see {@link PropertyMetadata#isDefaultValue()}.
     */
    boolean defaultValue() default false;

    /**
     * @return boolean indicating whether the given property is required, defaults
     *         to false
     * @see {@link PropertyMetadata#isRequired()}.
     */
    boolean required() default false;

    /**
     * @return The {@link PropertyMemberInfo}, defaults to
     *         {@link PropertyMemberInfo#DEFAULT}
     * @see {@link PropertyMetadata#getPropertyMemberInfo()}.
     */
    PropertyMemberInfo propertyMemberInfo() default PropertyMemberInfo.DEFAULT;

    /**
     * In case there is a collectionType defined the generated values will
     * implicitly wrapped in the corresponding collection class defined by that
     * wrapper, defaults to {@link CollectionType#NO_ITERABLE}.
     *
     * @return the {@link CollectionType}
     * @see {@link PropertyMetadata#getCollectionType()}.
     */
    CollectionType collectionType() default CollectionType.NO_ITERABLE;

    /**
     * @return whether the property can be read or written, default to
     *         {@link PropertyReadWrite#READ_WRITE}
     * @see {@link PropertyMetadata#getPropertyReadWrite()}.
     */
    PropertyReadWrite propertyReadWrite() default PropertyReadWrite.READ_WRITE;

    /**
     * Defines different ways for reading / writing properties.
     *
     * @return the {@link PropertyAccessStrategy}, defaults to
     *         {@link PropertyAccessStrategy#BEAN_PROPERTY}
     * @see {@link PropertyMetadata#getPropertyAccessStrategy()}.
     */
    PropertyAccessStrategy propertyAccessStrategy() default PropertyAccessStrategy.BEAN_PROPERTY;

    /**
     * Defines the the way how to deal with equality regarding
     * PropertySupport.assertValueSet(Object)
     *
     * @return the {@link AssertionStrategy}, defaults to
     *         {@link AssertionStrategy#DEFAULT}
     */
    AssertionStrategy assertionStrategy() default AssertionStrategy.DEFAULT;
}
