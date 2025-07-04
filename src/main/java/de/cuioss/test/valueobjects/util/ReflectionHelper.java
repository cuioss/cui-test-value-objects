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
package de.cuioss.test.valueobjects.util;

import de.cuioss.test.valueobjects.api.property.PropertyConfig;
import de.cuioss.test.valueobjects.api.property.PropertyReflectionConfig;
import de.cuioss.test.valueobjects.generator.dynamic.GeneratorResolver;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import de.cuioss.test.valueobjects.property.util.CollectionType;
import de.cuioss.tools.collect.CollectionBuilder;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.property.PropertyHolder;
import de.cuioss.tools.reflect.MoreReflection;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static de.cuioss.tools.collect.CollectionLiterals.immutableSet;
import static de.cuioss.tools.collect.CollectionLiterals.immutableSortedSet;
import static java.util.Objects.requireNonNull;

/**
 * @author Oliver Wolff
 */
@UtilityClass
public final class ReflectionHelper {

    /**
     * Identifies the properties to be ignored. Currently only consisting of 'class'
     */
    @SuppressWarnings("squid:S2386") // owolff: False positive -> is immutable
    public static final Set<String> PROPERTY_IGNORE_SET = immutableSet("class");

    private static final CuiLogger log = new CuiLogger(ReflectionHelper.class);

    /**
     * One stop method for the deriving of configured metadata
     *
     * @param annotated   must not be null
     * @param targetClass must not be null
     * @return an immutable {@link List} of {@link PropertyMetadata} containing the
     *         result of the actual scanning with
     *         {@link #scanBeanTypeForProperties(Class, PropertyReflectionConfig)}
     *         and {@link #handlePostProcess(Class, SortedSet)} and
     *         {@link PropertyHelper#handlePropertyConfigAnnotations(Class)} and
     *         {@link PropertyHelper#handlePrimitiveAsDefaults(Collection)}
     */
    public static <T> List<PropertyMetadata> handlePropertyMetadata(final Class<?> annotated,
        final Class<T> targetClass) {
        requireNonNull(annotated);
        requireNonNull(targetClass);

        final List<PropertyMetadata> builder = new ArrayList<>();
        if (shouldScanClass(annotated)) {
            final SortedSet<PropertyMetadata> scanned = new TreeSet<>(scanBeanTypeForProperties(targetClass,
                MoreReflection.extractAnnotation(annotated, PropertyReflectionConfig.class).orElse(null)));

            builder.addAll(handlePostProcess(annotated, scanned));
        }
        final var mapView = PropertyHelper.toMapView(builder);
        PropertyHelper.handlePropertyConfigAnnotations(annotated).forEach(p -> mapView.put(p.getName(), p));
        final var handled = PropertyHelper.handlePrimitiveAsDefaults(mapView.values());
        PropertyHelper.logMessageForPropertyMetadata(handled);
        return new ArrayList<>(handled);
    }

    /**
     * One stop method for the deriving of configured metadata
     *
     * @param propertyReflectionConfig extracted from the target class, may be null.
     * @param propertyConfig           extracted from the target class, may be empty
     *
     * @param targetClass              must not be null
     * @return an immutable {@link List} of {@link PropertyMetadata} containing the
     *         result of the actual scanning with
     *         {@link #scanBeanTypeForProperties(Class, PropertyReflectionConfig)}
     *         and {@link #handlePostProcess(Class, SortedSet)} and
     *         {@link PropertyHelper#handlePropertyConfigAnnotations(Class)} and
     *         {@link PropertyHelper#handlePrimitiveAsDefaults(Collection)}
     */
    public static <T> List<PropertyMetadata> handlePropertyMetadata(PropertyReflectionConfig propertyReflectionConfig,
        final List<PropertyConfig> propertyConfig, final Class<T> targetClass) {
        final List<PropertyMetadata> builder = new ArrayList<>();
        if (shouldScanClass(propertyReflectionConfig)) {
            final SortedSet<PropertyMetadata> scanned = new TreeSet<>(
                scanBeanTypeForProperties(targetClass, propertyReflectionConfig));

            builder.addAll(handlePostProcessConfig(propertyReflectionConfig, scanned));
        }
        final var mapView = PropertyHelper.toMapView(builder);
        PropertyHelper.handlePropertyConfigAnnotations(propertyConfig).forEach(p -> mapView.put(p.getName(), p));
        final var handled = PropertyHelper.handlePrimitiveAsDefaults(mapView.values());
        PropertyHelper.logMessageForPropertyMetadata(handled);
        return new ArrayList<>(handled);
    }

    /**
     * Uses {@link MoreReflection} to scan the concrete bean and describe the
     * properties with fitting {@link PropertyMetadata}. Each property will contain
     * the derived data for the attributes:
     * <ul>
     * <li>{@link PropertyMetadata#getName()}</li>
     * <li>{@link PropertyMetadata#getGenerator()} with the generator being
     * dynamically resolved using
     * {@link GeneratorResolver#resolveGenerator(Class)}</li>
     * <li>{@link PropertyMetadata#getPropertyReadWrite()}</li>
     * <li>{@link PropertyMetadata#getCollectionType()}</li>
     * <li>{@link PropertyMetadata#getPropertyMemberInfo()}</li>
     * </ul>
     * Other attributes use there defaults:
     * <ul>
     * <li>{@link PropertyMetadata#isRequired()} defaults to false</li>
     * <li>{@link PropertyMetadata#isDefaultValue()} defaults to false</li>
     * </ul>
     *
     * @param beanType to be checked by reflection
     * @param config   optional instance of {@link PropertyReflectionConfig} used
     *                 for filtering the scanning
     * @return a {@link SortedSet} containing the result of the inspection
     */
    public static SortedSet<PropertyMetadata> scanBeanTypeForProperties(final Class<?> beanType,
        final PropertyReflectionConfig config) {
        final Set<String> filter = new HashSet<>(PROPERTY_IGNORE_SET);
        if (null != config) {
            filter.addAll(Arrays.asList(config.exclude()));
        }
        final var found = new CollectionBuilder<PropertyMetadata>();

        var builder = new CollectionBuilder<PropertyHolder>();
        for (Method method : MoreReflection.retrieveAccessMethods(beanType)) {
            var attributeName = MoreReflection.computePropertyNameFromMethodName(method.getName());
            if (filter.contains(attributeName)) {
                log.debug("Filtering attribute '%s' for type '%s' as configured", attributeName, beanType);
                continue;
            }
            var holder = PropertyHolder.from(beanType, attributeName);
            if (holder.isEmpty()) {
                log.info("Unable to extract metadata for type '%s' and method '%s'", beanType, method.getName());
            } else {
                builder.add(holder.get());
            }
        }

        for (PropertyHolder holder : builder) {
            found.add(createPropertyMetadata(beanType, holder));
        }
        return found.toImmutableNavigableSet();
    }

    /**
     * Creates a {@link PropertyMetadata} for a given field.
     *
     * @param beanType       providing the property, must not be null
     * @param propertyHolder identifying the property-metadata, must not be null
     * @return an instance of {@link PropertyMetadata} describing the field.
     * @throws IllegalArgumentException wrapping underlying reflection exceptions.
     */
    public static PropertyMetadata createPropertyMetadata(final Class<?> beanType, PropertyHolder propertyHolder) {
        requireNonNull(beanType);
        requireNonNull(propertyHolder);

        var collectionType = CollectionType.NO_ITERABLE;
        Class<?> propertyType = propertyHolder.getType();

        final var field = MoreReflection.accessField(beanType, propertyHolder.getName());

        if (field.isPresent()) {
            final var collectionTypeOption = CollectionType.findResponsibleCollectionType(field.get().getType());
            if (collectionTypeOption.isPresent()) {
                collectionType = collectionTypeOption.get();
                if (CollectionType.ARRAY_MARKER.equals(collectionType)) {
                    propertyType = field.get().getType().getComponentType();
                } else {
                    propertyType = extractParameterizedType(field.get(),
                        (ParameterizedType) field.get().getGenericType());
                }
            }
        }
        if (null == propertyType) {
            throw new IllegalArgumentException("Unable to extract property '%s' on type '%s'"
                .formatted(propertyHolder.getName(), beanType.getName()));
        }
        var defaultValued = propertyType.isPrimitive();
        if (defaultValued && CollectionType.ARRAY_MARKER.equals(collectionType)) {
            defaultValued = false;
        }
        return PropertyMetadataImpl.builder().name(propertyHolder.getName()).defaultValue(defaultValued)
            .collectionType(collectionType).propertyMemberInfo(propertyHolder.getMemberInfo())
            .propertyReadWrite(propertyHolder.getReadWrite())
            .generator(GeneratorResolver.resolveGenerator(propertyType)).build();
    }

    private static Class<?> extractParameterizedType(final Field field, final ParameterizedType parameterizedType) {
        try {
            return (Class<?>) parameterizedType.getActualTypeArguments()[0];
        } catch (final ClassCastException e) {
            throw new IllegalStateException("""
                Unable to determine generic-type for %s, ususally this is the case with nested generics. \

                You need to provide a custom @PropertyConfig for this field and exclude it from scanning\
                , by using PropertyReflectionConfig#exclude.
                See package-javadoc of de.cuioss.test.valueobjects for samples.""".formatted(field.toString()), e);
        }
    }

    /**
     * Filters the given properties according to the annotated element given. It
     * checks for the annotation {@link PropertyReflectionConfig} and filters the
     * given {@link SortedSet}
     *
     * @param annotated must not be null
     * @param metatdata must not be null
     * @return the filtered {@link SortedSet}
     */
    public static SortedSet<PropertyMetadata> handlePostProcess(final Class<?> annotated,
        final SortedSet<PropertyMetadata> metatdata) {
        requireNonNull(annotated);

        final Optional<PropertyReflectionConfig> configOption = MoreReflection.extractAnnotation(annotated,
            PropertyReflectionConfig.class);

        return handlePostProcessConfig(configOption.orElse(null), metatdata);
    }

    /**
     * Filters the given properties according to the annotated element given. It
     * checks for the annotation {@link PropertyReflectionConfig} and filters the
     * given {@link SortedSet}
     *
     * @param config    must not be null
     * @param metatdata must not be null
     * @return the filtered {@link SortedSet}
     */
    public static SortedSet<PropertyMetadata> handlePostProcessConfig(final PropertyReflectionConfig config,
        final SortedSet<PropertyMetadata> metatdata) {
        requireNonNull(metatdata);

        if (metatdata.isEmpty() || null == config) {
            return metatdata;
        }

        var map = PropertyHelper.handleWhiteAndBlacklist(config.of(), config.exclude(), metatdata);

        map = AnnotationHelper.modifyPropertyMetadata(map, config.defaultValued(), config.readOnly(), config.required(),
            config.transientProperties(), config.writeOnly(), config.assertUnorderedCollection());

        return immutableSortedSet(map.values());
    }

    /**
     * Checks the given type for the annotation {@link PropertyReflectionConfig} if
     * it is there it checks on the value {@link PropertyReflectionConfig#skip()}
     *
     * @param annotated the class that may or may not provide the annotations, must
     *                  not be null
     * @return boolean indicating whether to skip property scanning.
     */
    public static boolean shouldScanClass(final Class<?> annotated) {
        requireNonNull(annotated);

        return shouldScanClass(
            MoreReflection.extractAnnotation(annotated, PropertyReflectionConfig.class).orElse(null));
    }

    /**
     * Checks the given type for the annotation {@link PropertyReflectionConfig} if
     * it is there it checks on the value {@link PropertyReflectionConfig#skip()}
     *
     * @param config the optional annotation, may be null
     * @return boolean indicating whether to skip property scanning.
     */
    public static boolean shouldScanClass(final PropertyReflectionConfig config) {
        if (null != config) {
            return !config.skip();
        }
        return true;
    }

    /**
     * Helper method that determines the actual type of a given {@link Iterable} by
     * peeking into it. <em>For testing only, should never be used in productive
     * code</em>
     *
     * @param iterable must not be null nor empty, the iterator must be reentrant.
     * @return The Class of the given {@link Iterable}.
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> determineSupertypeFromIterable(final Iterable<T> iterable) {
        requireNonNull(iterable, "iterable must not be null");
        final var iterator = iterable.iterator();
        if (iterator.hasNext()) {
            return (Class<T>) iterator.next().getClass();
        }
        throw new IllegalArgumentException("Must contain at least a single element");
    }
}
