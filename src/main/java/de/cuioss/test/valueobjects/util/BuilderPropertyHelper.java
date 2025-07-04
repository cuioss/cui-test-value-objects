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
package de.cuioss.test.valueobjects.util;

import de.cuioss.test.valueobjects.api.property.PropertyBuilderConfig;
import de.cuioss.test.valueobjects.api.property.PropertyBuilderConfigs;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.BuilderMetadata;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import de.cuioss.tools.collect.CollectionBuilder;
import de.cuioss.tools.reflect.MoreReflection;
import lombok.experimental.UtilityClass;

import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * Utility class for dealing with and the {@link PropertyBuilderConfig} and
 * {@link PropertyBuilderConfigs} annotations.
 *
 * @author Oliver Wolff
 */
@UtilityClass
public final class BuilderPropertyHelper {

    /**
     * Checks the given type for the annotation {@link PropertyBuilderConfig} and
     * {@link PropertyBuilderConfigs} and puts all found in the immutable
     * {@link List} to be returned
     *
     * @param annotated     the class that may or may not provide the annotations,
     *                      must not be null
     * @param givenMetadata must not be null
     * @return immutable {@link List} of found {@link PropertyMetadata} elements
     *         derived by the annotations.
     */
    public static List<PropertyMetadata> handleBuilderPropertyConfigAnnotations(final Class<?> annotated,
                                                                                final List<PropertyMetadata> givenMetadata) {
        requireNonNull(annotated);
        final var builder = new CollectionBuilder<PropertyMetadata>();

        extractConfiguredPropertyBuilderConfigs(annotated)
            .forEach(config -> builder.add(builderPropertyConfigToBuilderMetadata(config, givenMetadata)));

        return builder.toImmutableList();
    }

    /**
     * Checks the given type for the annotation {@link PropertyBuilderConfig} and
     * {@link PropertyBuilderConfigs} and puts all found in the immutable
     * {@link Set} to be returned
     *
     * @param annotated the class that may or may not provide the annotations, must
     *                  not be null
     * @return immutable {@link Set} of found {@link PropertyBuilderConfig}
     *         elements.
     */
    public static Set<PropertyBuilderConfig> extractConfiguredPropertyBuilderConfigs(final Class<?> annotated) {
        requireNonNull(annotated);
        final var builder = new CollectionBuilder<PropertyBuilderConfig>();

        MoreReflection.extractAllAnnotations(annotated, PropertyBuilderConfigs.class)
            .forEach(contract -> builder.add(Arrays.asList(contract.value())));
        MoreReflection.extractAllAnnotations(annotated, PropertyBuilderConfig.class).forEach(builder::add);

        return builder.toImmutableSet();
    }

    private static PropertyMetadata builderPropertyConfigToBuilderMetadata(final PropertyBuilderConfig config,
                                                                           final Collection<PropertyMetadata> givenMetadata) {

        final Map<String, PropertyMetadata> map = new HashMap<>();
        givenMetadata.forEach(meta -> map.put(meta.getName(), meta));
        PropertyHelper.assertPropertyExists(config.name(), map);

        final var metatada = PropertyMetadataImpl.builder(map.get(config.name()))
            .propertyAccessStrategy(config.propertyAccessStrategy()).build();

        return BuilderMetadata.builder().delegateMetadata(metatada).builderMethodName(config.builderMethodName())
            .builderMethodPrefix(config.methodPrefix())
            .builderSingleAddMethodName(config.builderSingleAddMethodName()).build();
    }

}
