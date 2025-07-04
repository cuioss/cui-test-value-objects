/**
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
package de.cuioss.test.valueobjects.property.impl;

import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.util.PropertyAccessStrategy;
import lombok.*;
import lombok.experimental.Delegate;

import static de.cuioss.tools.string.MoreStrings.emptyToNull;
import static de.cuioss.tools.string.MoreStrings.isEmpty;
import static java.util.Objects.requireNonNull;

/**
 * Extensions of {@link PropertyMetadata} that deals with builder-specific
 * aspects
 *
 * @author Oliver Wolff
 */
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor(access = AccessLevel.MODULE)
public class BuilderMetadata implements PropertyMetadata {

    @Delegate
    private final PropertyMetadata delegate;

    /**
     * Used for builder testing / verifying: In case builderMethodPrefix is null the
     * corresponding build method to be accessed for setting the value is the name
     * attribute: {@link PropertyMetadata#getName()}, in case it is a concrete
     * value, e.g. 'with' it will take into account: withPropertyName().
     */
    @Getter
    private final String builderAddMethodName;

    /**
     * Similar to {@link BuilderMetadataBuilder#builderMethodName(String)} but for
     * special cases described within
     * {@link PropertyAccessStrategy#BUILDER_COLLECTION_AND_SINGLE_ELEMENT} There
     * are two different steps for deriving the concrete method-name:
     * <ul>
     * <li>Base-name: In case
     * {@link BuilderMetadataBuilder#builderSingleAddMethodName(String)} is not set
     * it used {@link PropertyMetadata#getName()} as base name -> Overloading</li>
     * <li>The base-name will prefixed if there is a builderPrefixed configured, see
     * {@link BuilderMetadataBuilder#builderMethodPrefix(String)}</li>
     * </ul>
     */
    @Getter
    private final String builderSingleAddMethodName;

    static String prefixBuilderMethod(final String nameToBePrefixed, final String builderMethodPrefix) {
        requireNonNull(emptyToNull(nameToBePrefixed), "nameToBePrefixed");
        if (isEmpty(builderMethodPrefix)) {
            return nameToBePrefixed;
        }
        return builderMethodPrefix + Character.toUpperCase(nameToBePrefixed.charAt(0)) +
            nameToBePrefixed.substring(1);
    }

    /**
     * Builder for instances of {@link BuilderMetadata}
     *
     * @author Oliver Wolff
     */
    public static class BuilderMetadataBuilder {

        private String tempBuilderMethodName;
        private String tempBuilderMethodPrefix;
        private String tempBuilderSingleAddMethodName;
        private PropertyMetadata tempDelegate;

        /**
         * In case this methodName is set it will be used directly for deriving the
         * write-method. {@link #builderMethodPrefix(String)} will then ignored
         *
         * @param builderMethodName to be set
         * @return the builder for {@link BuilderMetadata}
         */
        public BuilderMetadataBuilder builderMethodName(final String builderMethodName) {
            tempBuilderMethodName = builderMethodName;
            return this;
        }

        /**
         * Defines a delegate {@link PropertyMetadata} used for all attributes that are
         * not builder specific, required.
         *
         * @param delegate to be set
         * @return the builder for {@link BuilderMetadata}
         */
        public BuilderMetadataBuilder delegateMetadata(final PropertyMetadata delegate) {
            tempDelegate = delegate;
            return this;
        }

        /**
         * see {@link BuilderMetadata#builderAddMethodName} for details
         *
         * @param builderMethodPrefix to be set
         * @return the builder for {@link BuilderMetadata}
         */
        public BuilderMetadataBuilder builderMethodPrefix(final String builderMethodPrefix) {
            tempBuilderMethodPrefix = emptyToNull(builderMethodPrefix);
            return this;
        }

        /**
         * Only needed for builder that deal with {@link Iterable} and single elements,
         * see {@link PropertyAccessStrategy#BUILDER_COLLECTION_AND_SINGLE_ELEMENT} for
         * details
         *
         * @param builderSingleAddMethodName to be set
         * @return the builder for {@link BuilderMetadata}
         */
        public BuilderMetadataBuilder builderSingleAddMethodName(final String builderSingleAddMethodName) {
            tempBuilderSingleAddMethodName = emptyToNull(builderSingleAddMethodName);
            return this;
        }

        /**
         * @return the built {@link BuilderMetadata}
         */
        public BuilderMetadata build() {
            requireNonNull(tempDelegate, "delegate");

            final var tempPropertyName = tempDelegate.getName();

            var addMethodName = tempBuilderMethodName;
            if (isEmpty(addMethodName)) {
                addMethodName = prefixBuilderMethod(tempPropertyName, tempBuilderMethodPrefix);
            }

            var singleAddMethodName = tempBuilderSingleAddMethodName;
            if (isEmpty(singleAddMethodName)) {
                singleAddMethodName = prefixBuilderMethod(tempPropertyName, tempBuilderMethodPrefix);
            }

            return new BuilderMetadata(tempDelegate, addMethodName, singleAddMethodName);
        }
    }

    /**
     * @return a new instance of {@link BuilderMetadataBuilder}
     */
    public static BuilderMetadataBuilder builder() {
        return new BuilderMetadataBuilder();
    }

    /**
     * Simple converter for creating a {@link BuilderMetadata} from any given
     * {@link PropertyMetadata} with defaults for the methods.
     *
     * @param metadata must not be null
     * @return a {@link BuilderMetadata} computed from the given
     *         {@link PropertyMetadata} without further configuration
     */
    public static BuilderMetadata wrapFromMetadata(final PropertyMetadata metadata) {
        return builder().delegateMetadata(metadata).build();
    }
}
