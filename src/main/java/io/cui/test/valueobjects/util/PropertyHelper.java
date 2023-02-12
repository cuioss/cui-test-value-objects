package io.cui.test.valueobjects.util;

import static io.cui.test.valueobjects.util.AnnotationHelper.UNABLE_TO_INSTANTIATE_GENERATOR;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import io.cui.test.generator.TypedGenerator;
import io.cui.test.valueobjects.api.property.PropertyConfig;
import io.cui.test.valueobjects.api.property.PropertyConfigs;
import io.cui.test.valueobjects.generator.dynamic.DynamicTypedGenerator;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.impl.PropertyMetadataImpl;
import io.cui.test.valueobjects.property.util.CollectionType;
import io.cui.tools.collect.CollectionBuilder;
import io.cui.tools.logging.CuiLogger;
import io.cui.tools.reflect.MoreReflection;
import io.cui.tools.string.Joiner;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Provides utility methods for dealing with {@link PropertyMetadata}
 *
 * @author Oliver Wolff
 */
@SuppressWarnings("squid:S1118") // owolff: lombok generated
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertyHelper {

    private static final CuiLogger log = new CuiLogger(PropertyHelper.class);

    /**
     * Ensure that the result of property scanning is logged only once.
     */
    private static boolean propertyInformationLogged = false;

    /**
     * Ensure that the result of property scanning is logged only once.
     */
    private static boolean propertyTargetInformationLogged = false;

    /**
     * Simple helper method that creates a sensible information message for logging purpose. It uses
     * a static field in order to ensure that the logging will only be done once per Test-unit *
     *
     * @param handled to be logged
     */
    public static void logMessageForPropertyMetadata(
            final Collection<? extends PropertyMetadata> handled) {
        if (!propertyInformationLogged) {
            final StringBuilder messageBuilder =
                new StringBuilder(
                        "Properties detected by using reflection and PropertyConfig-annotation: ")
                                .append("\n");
            final List<String> elements = new ArrayList<>();
            handled.forEach(data -> elements.add("-" + data.toString()));
            Collections.sort(elements);
            messageBuilder.append(Joiner.on("\n").join(elements));

            log.info(messageBuilder.toString());
            synchronized (PropertyHelper.class) {
                propertyInformationLogged = true;
            }
        }
    }

    /**
     * Simple helper method that creates a sensible information message for logging purpose. It uses
     * a static field in order to ensure that the logging will only be done once per Test-unit *
     *
     * @param handled to be logged
     */
    public static void logMessageForTargetPropertyMetadata(
            final Collection<? extends PropertyMetadata> handled) {
        if (!propertyTargetInformationLogged) {
            final StringBuilder messageBuilder =
                new StringBuilder(
                        "Properties detected for targetType: ")
                                .append("\n");
            final List<String> elements = new ArrayList<>();
            handled.forEach(data -> elements.add("-" + data.toString()));
            Collections.sort(elements);
            messageBuilder.append(Joiner.on("\n").join(elements));

            log.info(messageBuilder.toString());
            synchronized (PropertyHelper.class) {
                propertyTargetInformationLogged = true;
            }
        }
    }

    /**
     * Sets all primitives of the given list to {@link PropertyMetadata#isDefaultValue()} being
     * {@code true} in case {@link PropertyMetadata#getCollectionType()} being
     * {@link CollectionType#NO_ITERABLE} .
     *
     * @param metadata must not be null
     * @return an immutable list with {@link PropertyMetadata}
     */
    public static final Collection<PropertyMetadata> handlePrimitiveAsDefaults(
            final Collection<PropertyMetadata> metadata) {
        requireNonNull(metadata);
        if (metadata.isEmpty()) {
            return metadata;
        }
        final CollectionBuilder<PropertyMetadata> builder = new CollectionBuilder<>();
        for (final PropertyMetadata propertyMetadata : metadata) {
            if (propertyMetadata.getPropertyClass().isPrimitive()
                    && CollectionType.NO_ITERABLE.equals(propertyMetadata.getCollectionType())) {
                builder.add(
                        PropertyMetadataImpl.builder(propertyMetadata).defaultValue(true).build());
            } else {
                builder.add(propertyMetadata);
            }
        }
        return builder.toImmutableList();
    }

    /**
     * Checks the given type for the annotation {@link PropertyConfig} and
     * {@link PropertyConfigs} and puts all found in the immutable set to be returned
     *
     * @param annotated the class that may or may not provide the annotations, must not be null
     * @return immutable set of found {@link PropertyMetadata} elements derived by the annotations.
     */
    public static final Set<PropertyMetadata> handlePropertyConfigAnnotations(
            final Class<?> annotated) {
        requireNonNull(annotated);
        return handlePropertyConfigAnnotations(extractConfiguredPropertyConfigs(annotated));
    }

    /**
     * Checks the given type for the annotation {@link PropertyConfig} and
     * {@link PropertyConfigs} and puts all found in the immutable set to be returned
     *
     * @param config the PropertyConfig-annotations, must not be null
     * @return immutable set of found {@link PropertyMetadata} elements derived by the annotations.
     */
    public static final Set<PropertyMetadata> handlePropertyConfigAnnotations(
            final Collection<PropertyConfig> config) {
        final CollectionBuilder<PropertyMetadata> builder =
            new CollectionBuilder<>();

        config.forEach(conf -> builder.add(propertyConfigToPropertyMetadata(conf)));

        return builder.toImmutableSet();
    }

    /**
     * Checks the given type for the annotation {@link PropertyConfig} and
     * {@link PropertyConfigs} and puts all found in the returned list
     *
     * @param annotated the class that may or may not provide the annotations, must not be null
     * @return a {@link Set} of {@link PropertyConfig} extract from the annotations of the given
     *         type. May be empty but never null
     */
    public static final Set<PropertyConfig> extractConfiguredPropertyConfigs(
            final Class<?> annotated) {
        requireNonNull(annotated);
        final CollectionBuilder<PropertyConfig> builder = new CollectionBuilder<>();

        MoreReflection.extractAllAnnotations(annotated, PropertyConfigs.class)
                .forEach(contract -> builder.add(contract.value()));
        MoreReflection.extractAllAnnotations(annotated, PropertyConfig.class)
                .forEach(builder::add);

        return builder.toImmutableSet();
    }

    private static PropertyMetadata propertyConfigToPropertyMetadata(final PropertyConfig config) {
        @SuppressWarnings("rawtypes")
        final Class<? extends TypedGenerator> generatorClass = config.generator();
        final TypedGenerator<?> generator;
        if (!DynamicTypedGenerator.class.equals(generatorClass)) {
            try {
                generator = generatorClass.newInstance();
            } catch (final InstantiationException | IllegalAccessException e) {
                throw new IllegalArgumentException(
                        UNABLE_TO_INSTANTIATE_GENERATOR + generatorClass,
                        e);
            }
        } else {
            generator = new DynamicTypedGenerator<>(config.propertyClass());
        }

        return PropertyMetadataImpl.builder().name(config.name())
                .defaultValue(config.defaultValue())
                .propertyAccessStrategy(config.propertyAccessStrategy())
                .generator(generator)
                .propertyClass(config.propertyClass())
                .propertyMemberInfo(config.propertyMemberInfo())
                .collectionType(config.collectionType())
                .assertionStrategy(config.assertionStrategy())
                .propertyReadWrite(config.propertyReadWrite()).required(config.required()).build();
    }

    /**
     * Simple helper, that create a map with with name as key for the given {@link PropertyMetadata}
     *
     * @param metadata if it is null or empty an empty {@link Map} will be returned.
     * @return a map with with name as key for the given {@link PropertyMetadata}
     */
    public static final Map<String, PropertyMetadata> toMapView(
            final Collection<PropertyMetadata> metadata) {
        final Map<String, PropertyMetadata> map = new HashMap<>();
        if (null == metadata) {
            return map;
        }
        metadata.forEach(m -> map.put(m.getName(), m));
        return map;
    }

    /**
     * Handles the white- / black-list for the given parameter {@link Collection}
     *
     * @param of must not be null
     * @param exclude must not be null
     * @param givenMetadata must not be null
     * @return the filtered property map
     */
    public static Map<String, PropertyMetadata> handleWhiteAndBlacklist(final String[] of,
            final String[] exclude, final Collection<PropertyMetadata> givenMetadata) {
        Map<String, PropertyMetadata> map = PropertyHelper.toMapView(givenMetadata);
        if (of.length != 0) {
            // Whitelist takes precedence
            final Map<String, PropertyMetadata> copyMap = new HashMap<>();
            for (final String name : of) {
                assertPropertyExists(name, map);
                copyMap.put(name, map.get(name));
            }
            map = copyMap;
        } else {
            // Remove all excluded properties
            for (final String name : exclude) {
                map.remove(name);
            }
        }
        return map;
    }

    /**
     * Handles the white- / black-list for the given parameter map. This variant returns a list with
     * the exact order of the given list
     *
     * @param of must not be null
     * @param exclude must not be null
     * @param givenMetadata is it is null or empty an empty list will be returned.
     * @return the filtered properties
     */
    public static List<PropertyMetadata> handleWhiteAndBlacklistAsList(final String[] of,
            final String[] exclude, final List<PropertyMetadata> givenMetadata) {
        if (null == givenMetadata || givenMetadata.isEmpty()) {
            return Collections.emptyList();
        }
        final Map<String, PropertyMetadata> map =
            handleWhiteAndBlacklist(of, exclude, givenMetadata);
        final CollectionBuilder<PropertyMetadata> builder = new CollectionBuilder<>();
        for (final PropertyMetadata meta : givenMetadata) {
            if (map.containsKey(meta.getName())) {
                builder.add(meta);
            }
        }
        return builder.toImmutableList();
    }

    /**
     * Simple assertions indicating that the property identified by the name exists in the given map
     *
     * @param name must no be null
     * @param map must no be null
     */
    public static void assertPropertyExists(final String name,
            final Map<String, PropertyMetadata> map) {
        if (!map.containsKey(name)) {
            throw new IllegalArgumentException("'" + name + "'"
                    + " is not a configured property within the given properties, check your configuration");
        }
    }

    /**
     * Simple assertions indicating that the property identified by the name exists in the given Set
     *
     * @param name must no be null
     * @param givenMetadata must no be null
     */
    public static void assertPropertyExists(final String name,
            final SortedSet<PropertyMetadata> givenMetadata) {
        final Map<String, PropertyMetadata> map = new HashMap<>();
        givenMetadata.forEach(meta -> map.put(meta.getName(), meta));
        assertPropertyExists(name, map);
    }

}
