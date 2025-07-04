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
package de.cuioss.test.valueobjects.property.impl;

import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.generator.impl.CollectionGenerator;
import de.cuioss.test.generator.impl.PrimitiveArrayGenerators;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.util.AssertionStrategy;
import de.cuioss.test.valueobjects.property.util.CollectionType;
import de.cuioss.test.valueobjects.property.util.PropertyAccessStrategy;
import de.cuioss.tools.property.PropertyMemberInfo;
import de.cuioss.tools.property.PropertyReadWrite;
import de.cuioss.tools.string.Joiner;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static de.cuioss.tools.string.MoreStrings.emptyToNull;
import static java.util.Objects.requireNonNull;

/**
 * Gathers all information needed for dynamically creating / asserting
 * properties.
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor(access = AccessLevel.MODULE)
@EqualsAndHashCode(exclude = "generator", doNotUseGetters = true)
public class PropertyMetadataImpl implements PropertyMetadata {

    @Getter
    private final String name;

    @Getter
    private final TypedGenerator<?> generator;

    @Getter
    private final boolean defaultValue;

    @Getter
    private final Class<?> propertyClass;

    private final Class<?> actualClass;

    @Getter
    private final boolean required;

    @Getter
    private final PropertyAccessStrategy propertyAccessStrategy;

    @Getter
    private final CollectionType collectionType;

    @Getter
    private final PropertyMemberInfo propertyMemberInfo;

    @Getter
    private final PropertyReadWrite propertyReadWrite;

    @Getter
    private final AssertionStrategy assertionStrategy;

    @Override
    public Object next() {
        switch (collectionType) {
            case NO_ITERABLE:
                return generator.next();
            case ARRAY_MARKER:
                if (!propertyClass.isPrimitive()) {
                    return resolveCollectionGenerator().list().toArray();
                }
                return PrimitiveArrayGenerators.resolveForType(getPropertyClass()).next();
            default:
                return collectionType.nextIterable(resolveCollectionGenerator());
        }
    }

    @Override
    public CollectionGenerator<?> resolveCollectionGenerator() {
        return new CollectionGenerator<>(generator);
    }

    @Override
    public Class<?> resolveActualClass() {
        return actualClass;
    }

    /**
     * @author Oliver Wolff
     */
    public static class PropertyMetadataBuilder {

        private TypedGenerator<?> tempGenerator;
        private String tempName;
        private boolean tempDefaultValue = false;
        private Class<?> tempPropertyClass;
        private boolean tempRequired = false;
        private PropertyMemberInfo tempPropertyMemberInfo = PropertyMemberInfo.DEFAULT;
        private CollectionType tempCollectionType = CollectionType.NO_ITERABLE;
        private PropertyAccessStrategy tempPropertyAccessStrategy = PropertyAccessStrategy.BEAN_PROPERTY;
        private PropertyReadWrite tempPropertyReadWrite = PropertyReadWrite.READ_WRITE;
        private AssertionStrategy tempAssertionStrategy = AssertionStrategy.DEFAULT;

        /**
         * @param propertyAccessStrategy to be set
         * @return the builder for {@link PropertyMetadataImpl}
         */
        public PropertyMetadataBuilder propertyAccessStrategy(final PropertyAccessStrategy propertyAccessStrategy) {
            tempPropertyAccessStrategy = propertyAccessStrategy;
            return this;
        }

        /**
         * @param propertyReadWrite to be set
         * @return the builder for {@link PropertyMetadataImpl}
         */
        public PropertyMetadataBuilder propertyReadWrite(final PropertyReadWrite propertyReadWrite) {
            tempPropertyReadWrite = propertyReadWrite;
            return this;
        }

        /**
         * @param assertionStrategy to be set
         * @return the builder for {@link PropertyMetadataImpl}
         */
        public PropertyMetadataBuilder assertionStrategy(final AssertionStrategy assertionStrategy) {
            tempAssertionStrategy = assertionStrategy;
            return this;
        }

        /**
         * In case you have a {@link TypedGenerator} it will be implicitly set as
         * {@link PropertyMetadataImpl#getGenerator()} and
         * {@link PropertyMetadataImpl#getPropertyClass()}
         *
         * @param typedGenerator to be set
         * @return the builder for {@link PropertyMetadataImpl}
         */
        public PropertyMetadataBuilder generator(final TypedGenerator<?> typedGenerator) {
            tempGenerator = typedGenerator;
            tempPropertyClass = typedGenerator.getType();
            return this;
        }

        /**
         * @see PropertyMetadata#getName()
         * @param name to be set. must not be null nor empty
         * @return the builder for {@link PropertyMetadataImpl}
         */
        public PropertyMetadataBuilder name(final String name) {
            tempName = emptyToNull(name);
            return this;
        }

        /**
         * @param defaultValue to be set
         * @return the builder for {@link PropertyMetadataImpl}
         */
        public PropertyMetadataBuilder defaultValue(final boolean defaultValue) {
            tempDefaultValue = defaultValue;
            return this;
        }

        /**
         * @see PropertyMetadata#getCollectionType()
         * @param collectionType
         * @return the builder for {@link PropertyMetadataImpl}
         */
        public PropertyMetadataBuilder collectionType(final CollectionType collectionType) {
            tempCollectionType = collectionType;
            return this;
        }

        /**
         * @param propertyClass to be set
         * @return the builder for {@link PropertyMetadataImpl}
         */
        public PropertyMetadataBuilder propertyClass(final Class<?> propertyClass) {
            tempPropertyClass = propertyClass;
            return this;
        }

        /**
         * @param required to be set
         * @return the builder for {@link PropertyMetadataImpl}
         */
        public PropertyMetadataBuilder required(final boolean required) {
            tempRequired = required;
            return this;
        }

        /**
         * @see PropertyMetadata#getPropertyMemberInfo()
         * @param propertyMemberInfo to be set. must not be null
         * @return the builder for {@link PropertyMetadataImpl}
         */
        public PropertyMetadataBuilder propertyMemberInfo(final PropertyMemberInfo propertyMemberInfo) {
            requireNonNull(propertyMemberInfo, "objectMemberInfo");
            tempPropertyMemberInfo = propertyMemberInfo;
            return this;
        }

        /**
         * @return the built {@link PropertyMetadataImpl}
         */
        public PropertyMetadataImpl build() {
            requireNonNull(tempName, "name");
            requireNonNull(tempGenerator, "generator");
            requireNonNull(tempPropertyClass, "propertyClass");

            Class<?> actualClass = tempPropertyClass;
            if (!CollectionType.NO_ITERABLE.equals(tempCollectionType)) {
                if (CollectionType.ARRAY_MARKER.equals(tempCollectionType)) {
                    actualClass = Array.newInstance(tempPropertyClass, 0).getClass();
                } else {
                    actualClass = tempCollectionType.getIterableType();
                }

            }

            return new PropertyMetadataImpl(tempName, tempGenerator, tempDefaultValue, tempPropertyClass, actualClass,
                tempRequired, tempPropertyAccessStrategy, tempCollectionType, tempPropertyMemberInfo,
                tempPropertyReadWrite, tempAssertionStrategy);
        }

    }

    /**
     * @return a new instance of {@link PropertyMetadataBuilder}
     */
    public static PropertyMetadataBuilder builder() {
        return new PropertyMetadataBuilder();
    }

    /**
     * @param copyFrom to be use as template, must not be null
     * @return a new instance of {@link PropertyMetadataBuilder} with the content of
     *         copyFrom being already copied into
     */
    public static PropertyMetadataBuilder builder(final PropertyMetadata copyFrom) {
        final var builder = builder();
        requireNonNull(copyFrom);
        builder.collectionType(copyFrom.getCollectionType()).defaultValue(copyFrom.isDefaultValue());
        builder.generator(copyFrom.getGenerator()).name(copyFrom.getName());
        builder.propertyAccessStrategy(copyFrom.getPropertyAccessStrategy());
        builder.propertyClass(copyFrom.getPropertyClass());
        builder.propertyMemberInfo(copyFrom.getPropertyMemberInfo());
        builder.propertyReadWrite(copyFrom.getPropertyReadWrite());
        builder.required(copyFrom.isRequired());
        builder.assertionStrategy(copyFrom.getAssertionStrategy());
        return builder;
    }

    @Override
    public int compareTo(final PropertyMetadata other) {
        return name.compareTo(other.getName());
    }

    @Override
    public String toString() {
        final List<Object> elements = new ArrayList<>();

        elements.add("'" + getName() + "' (" + getPropertyClass() + ")");

        if (isRequired()) {
            elements.add("required");
        }
        if (isDefaultValue()) {
            elements.add("defaultValued");
        }
        elements.add(getGenerator());
        if (!CollectionType.NO_ITERABLE.equals(getCollectionType())) {
            elements.add(getCollectionType());
            elements.add("actualClass: " + actualClass);
        }
        elements.add(getPropertyReadWrite());
        elements.add(getPropertyAccessStrategy());
        if (!AssertionStrategy.DEFAULT.equals(getAssertionStrategy())) {
            elements.add(getAssertionStrategy());
        }

        return Joiner.on(", ").join(elements);
    }

}
