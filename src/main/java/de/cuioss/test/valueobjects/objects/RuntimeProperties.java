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
package de.cuioss.test.valueobjects.objects;

import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.PropertySupport;
import de.cuioss.tools.collect.MapBuilder;
import de.cuioss.tools.string.Joiner;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.*;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static java.util.Objects.requireNonNull;

/**
 * Aggregates all information necessary to dynamically create Objects. In
 * addition it makes some sanity checks. It provides some convenience methods
 * for accessing certain views on the properties.
 *
 * @author Oliver Wolff
 */
@EqualsAndHashCode(of = "allProperties")
public class RuntimeProperties {

    /**
     * All {@link PropertyMetadata} contained by this {@link RuntimeProperties}. May
     * be empty
     */
    @Getter
    private final List<PropertyMetadata> allProperties;

    /**
     * All {@link PropertyMetadata} contained by this {@link RuntimeProperties} that
     * are required: {@link PropertyMetadata#isRequired()}. May be an empty list.
     */
    @Getter
    private final List<PropertyMetadata> requiredProperties;

    /**
     * All {@link PropertyMetadata} contained by this {@link RuntimeProperties} that
     * are <em>NOT</em> {@link PropertyMetadata#isRequired()}. May be an empty list.
     */
    @Getter
    private final List<PropertyMetadata> additionalProperties;

    /**
     * All {@link PropertyMetadata} contained by this {@link RuntimeProperties} that
     * provide a {@link PropertyMetadata#isDefaultValue()}. May be an empty list.
     */
    @Getter
    private final List<PropertyMetadata> defaultProperties;

    /**
     * All {@link PropertyMetadata} contained by this {@link RuntimeProperties}
     * where the properties can be written. May be an empty list.
     */
    @Getter
    private final List<PropertyMetadata> writableProperties;

    /**
     * Constructor.
     *
     * @param properties may be null
     */
    public RuntimeProperties(final List<? extends PropertyMetadata> properties) {
        if (null == properties) {
            allProperties = Collections.emptyList();
        } else {
            allProperties = immutableList(properties);
        }
        requiredProperties = allProperties.stream().filter(PropertyMetadata::isRequired).toList();

        additionalProperties = allProperties.stream().filter(metadata -> !metadata.isRequired()).toList();

        defaultProperties = allProperties.stream().filter(PropertyMetadata::isDefaultValue).toList();

        writableProperties = allProperties.stream().filter(metadata -> metadata.getPropertyReadWrite().isWriteable())
            .toList();
    }

    /**
     * @param properties
     */
    public RuntimeProperties(final SortedSet<PropertyMetadata> properties) {
        this(immutableList(properties));
    }

    /**
     * Creates a list of {@link PropertySupport} for each {@link PropertyMetadata}
     * of the given {@link Collection}
     *
     * @param propertyMetadata  if null or empty an empty list will be returned
     * @param generateTestValue boolean indicating whether to call
     *                          {@link PropertySupport#generateTestValue()} on each
     *                          created element
     * @return the newly created mutable {@link List}
     */
    public static List<PropertySupport> mapToPropertySupport(final Collection<PropertyMetadata> propertyMetadata,
        final boolean generateTestValue) {
        final List<PropertySupport> list = new ArrayList<>();
        if (null == propertyMetadata || propertyMetadata.isEmpty()) {
            return list;
        }
        propertyMetadata.forEach(p -> list.add(new PropertySupport(p)));
        if (generateTestValue) {
            list.forEach(PropertySupport::generateTestValue);
        }
        return list;
    }

    /**
     * Creates a list of {@link PropertySupport} for each {@link PropertyMetadata}
     * out of {@link #allProperties}
     *
     * @param generateTestValue boolean indicating whether to call
     *                          {@link PropertySupport#generateTestValue()} on each
     *                          created element
     * @return the newly created mutable {@link List}
     */
    public List<PropertySupport> getAllAsPropertySupport(final boolean generateTestValue) {
        return mapToPropertySupport(getAllProperties(), generateTestValue);
    }

    /**
     * Creates a list of {@link PropertySupport} for each {@link PropertyMetadata}
     * out of {@link #allProperties} but filtered according to the given names.
     *
     * @param generateTestValue boolean indicating whether to call
     *                          {@link PropertySupport#generateTestValue()} on each
     *                          created element
     * @param filter            containing the names to be filtered, must not be
     *                          null
     * @return the newly created mutable {@link List}
     */
    public List<PropertySupport> getAllAsPropertySupport(final boolean generateTestValue,
        final Collection<String> filter) {
        requireNonNull(filter);
        return getAllAsPropertySupport(generateTestValue).stream().filter(s -> filter.contains(s.getName())).toList();
    }

    /**
     * Creates a list of {@link PropertySupport} for each {@link PropertyMetadata}
     * out of {@link #requiredProperties}
     *
     * @param generateTestValue boolean indicating whether to call
     *                          {@link PropertySupport#generateTestValue()} on each
     *                          created element
     * @return the newly created mutable {@link List}
     */
    public List<PropertySupport> getRequiredAsPropertySupport(final boolean generateTestValue) {
        return mapToPropertySupport(getRequiredProperties(), generateTestValue);
    }

    /**
     * Creates a list of {@link PropertySupport} for each {@link PropertyMetadata}
     * out of {@link #requiredProperties} but filtered according to the given
     * names.
     *
     * @param generateTestValue boolean indicating whether to call
     *                          {@link PropertySupport#generateTestValue()} on each
     *                          created element
     * @param filter            containing the names to be filtered, must not be
     *                          null
     * @return the newly created mutable {@link List}
     */
    public List<PropertySupport> getRequiredAsPropertySupport(final boolean generateTestValue,
        final Collection<String> filter) {
        requireNonNull(filter);
        return getRequiredAsPropertySupport(generateTestValue).stream().filter(s -> filter.contains(s.getName()))
            .toList();
    }

    /**
     * Creates a list of {@link PropertySupport} for each {@link PropertyMetadata}
     * out of {@link #defaultProperties}
     *
     * @param generateTestValue boolean indicating whether to call
     *                          {@link PropertySupport#generateTestValue()} on each
     *                          created element
     * @return the newly created mutable {@link List}
     */
    public List<PropertySupport> getDefaultAsPropertySupport(final boolean generateTestValue) {
        return mapToPropertySupport(getDefaultProperties(), generateTestValue);
    }

    /**
     * Creates a list of {@link PropertySupport} for each {@link PropertyMetadata}
     * out of {@link #defaultProperties} but filtered according to the given
     * names.
     *
     * @param generateTestValue boolean indicating whether to call
     *                          {@link PropertySupport#generateTestValue()} on each
     *                          created element
     * @param filter            containing the names to be filtered, must not be
     *                          null
     * @return the newly created mutable {@link List}
     */
    public List<PropertySupport> getDefaultAsPropertySupport(final boolean generateTestValue,
        final Collection<String> filter) {
        requireNonNull(filter);
        return getDefaultAsPropertySupport(generateTestValue).stream().filter(s -> filter.contains(s.getName()))
            .toList();
    }

    /**
     * Creates a list of {@link PropertySupport} for each {@link PropertyMetadata}
     * out of {@link #additionalProperties}
     *
     * @param generateTestValue boolean indicating whether to call
     *                          {@link PropertySupport#generateTestValue()} on each
     *                          created element
     * @return the newly created mutable {@link List}
     */
    public List<PropertySupport> getAdditionalAsPropertySupport(final boolean generateTestValue) {
        return mapToPropertySupport(getAdditionalProperties(), generateTestValue);
    }

    /**
     * Creates a list of {@link PropertySupport} for each {@link PropertyMetadata}
     * out of {@link #additionalProperties}
     *
     * @param generateTestValue boolean indicating whether to call
     *                          {@link PropertySupport#generateTestValue()} on each
     *                          created element
     * @param filter            containing the names to be filtered, must not be
     *                          null
     * @return the newly created mutable {@link List}
     */
    public List<PropertySupport> getAdditionalAsPropertySupport(final boolean generateTestValue,
        final Collection<String> filter) {
        requireNonNull(filter);
        return getAdditionalAsPropertySupport(generateTestValue).stream().filter(s -> filter.contains(s.getName()))
            .toList();
    }

    /**
     * Creates a list of {@link PropertySupport} for each {@link PropertyMetadata}
     * out of {@link #writableProperties}
     *
     * @param generateTestValue boolean indicating whether to call
     *                          {@link PropertySupport#generateTestValue()} on each
     *                          created element
     * @return the newly created mutable {@link List}
     */
    public List<PropertySupport> getWritableAsPropertySupport(final boolean generateTestValue) {
        return mapToPropertySupport(getWritableProperties(), generateTestValue);
    }

    /**
     * Creates a list of {@link PropertySupport} for each {@link PropertyMetadata}
     * out of {@link #writableProperties}
     *
     * @param generateTestValue boolean indicating whether to call
     *                          {@link PropertySupport#generateTestValue()} on each
     *                          created element
     * @param filter            containing the names to be filtered, must not be
     *                          null
     * @return the newly created mutable {@link List}
     */
    public List<PropertySupport> getWritableAsPropertySupport(final boolean generateTestValue,
        final Collection<String> filter) {
        requireNonNull(filter);
        return getWritableAsPropertySupport(generateTestValue).stream().filter(s -> filter.contains(s.getName()))
            .toList();
    }

    /**
     * @param generateTestValue boolean indicating whether to call
     *                          {@link PropertySupport#generateTestValue()} on each
     *                          created element
     * @return a map view on all {@link PropertyMetadata} as {@link PropertySupport}
     *         with the property names as key
     */
    public Map<String, PropertySupport> asMapView(final boolean generateTestValue) {
        var builder = new MapBuilder<String, PropertySupport>();
        getAllAsPropertySupport(generateTestValue).forEach(p -> builder.put(p.getName(), p));
        return builder.toImmutableMap();
    }

    /**
     * Extracts the names of a given {@link Collection} of {@link PropertyMetadata}
     *
     * @param metadata if it is null or empty an empty {@link Set} will be returned
     * @return a set of the extracted names.
     */
    public static final Set<String> extractNames(final Collection<PropertyMetadata> metadata) {
        if (null == metadata || metadata.isEmpty()) {
            return Collections.emptySet();
        }
        final Set<String> builder = new HashSet<>();
        metadata.forEach(m -> builder.add(m.getName()));
        return builder;
    }

    @Override
    public String toString() {
        return getClass().getName() + "\nRequired properties: " + getPropertyNames(requiredProperties) +
            "\nAdditional properties: " + getPropertyNames(additionalProperties) +
            "\nDefault valued properties: " + getPropertyNames(defaultProperties) +
            "\nWritable properties: " + getPropertyNames(writableProperties);
    }

    private static String getPropertyNames(final List<PropertyMetadata> properties) {
        if (null == properties || properties.isEmpty()) {
            return "-";
        }
        final List<String> names = new ArrayList<>();
        properties.forEach(p -> names.add(p.getName()));
        return Joiner.on(", ").join(names);
    }
}
