package de.cuioss.test.valueobjects.util;

import static de.cuioss.tools.base.Preconditions.checkArgument;
import static de.cuioss.tools.collect.CollectionLiterals.mutableList;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;

import de.cuioss.test.valueobjects.api.VerifyMapperConfiguration;
import de.cuioss.test.valueobjects.api.contracts.VerifyBeanProperty;
import de.cuioss.test.valueobjects.api.contracts.VerifyBuilder;
import de.cuioss.test.valueobjects.api.contracts.VerifyConstructor;
import de.cuioss.test.valueobjects.api.contracts.VerifyConstructors;
import de.cuioss.test.valueobjects.api.contracts.VerifyFactoryMethod;
import de.cuioss.test.valueobjects.api.contracts.VerifyFactoryMethods;
import de.cuioss.test.valueobjects.api.property.PropertyReflectionConfig;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.BuilderMetadata;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import de.cuioss.test.valueobjects.property.util.AssertionStrategy;
import de.cuioss.test.valueobjects.property.util.PropertyAccessStrategy;
import de.cuioss.tools.collect.CollectionBuilder;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.property.PropertyMemberInfo;
import de.cuioss.tools.property.PropertyReadWrite;
import de.cuioss.tools.reflect.MoreReflection;
import lombok.experimental.UtilityClass;

/**
 * Simple helper class dealing with annotation on the test-base classes.
 *
 * @author Oliver Wolff
 */
@UtilityClass
public final class AnnotationHelper {

    private static final String NO_PROPERTIES_GIVEN_IS_THIS_INTENTIONAL = "No properties given: Is this intentional?";
    static final String UNABLE_TO_INSTANTIATE_GENERATOR =
        "Unable to instantiate generator, You must provide a no-arg public constructor: ";

    private static final CuiLogger log = new CuiLogger(AnnotationHelper.class);

    /**
     * Creates a {@link List} of {@link PropertyMetadata} according to the given parameter
     *
     * @param config identifying the concrete configuration
     * @param givenMetadata used fore deriving the concrete metadata. Must not be null
     * @return a {@link List} of {@link PropertyMetadata} according to the given parameter
     */
    public static List<PropertyMetadata> constructorConfigToPropertyMetadata(
            final VerifyConstructor config, final Collection<PropertyMetadata> givenMetadata) {

        requireNonNull(config);
        requireNonNull(givenMetadata);

        final Map<String, PropertyMetadata> map = new HashMap<>();
        final List<String> ofAsList = Arrays.asList(config.of());

        givenMetadata.stream().filter(p -> ofAsList.contains(p.getName()))
                .forEach(meta -> map.put(meta.getName(), meta));

        for (final String name : config.of()) {
            PropertyHelper.assertPropertyExists(name, map);
            modifyPropertyMetadata(map, config.defaultValued(), config.readOnly(),
                    config.required(), config.transientProperties(), config.writeOnly(),
                    config.assertUnorderedCollection());
        }

        if (config.allRequired()) {
            map.replaceAll(
                    (k, v) -> PropertyMetadataImpl.builder(v).required(true).build());
        }

        return orderPropertyMetadata(config.of(), handleWritableAttributes(map));
    }

    private static List<PropertyMetadata> handleWritableAttributes(
            final Map<String, PropertyMetadata> map) {
        final List<PropertyMetadata> result = new ArrayList<>();
        for (Entry<String, PropertyMetadata> entry : map.entrySet()) {
            result.add(
                    PropertyMetadataImpl.builder(entry.getValue())
                            .propertyReadWrite(determinePropertyReadWrite(entry.getValue())).build());
        }
        return result;
    }

    private static PropertyReadWrite determinePropertyReadWrite(final PropertyMetadata propertyMetadata) {
        if (PropertyReadWrite.WRITE_ONLY.equals(propertyMetadata.getPropertyReadWrite())) {
            return PropertyReadWrite.WRITE_ONLY;
        }
        return PropertyReadWrite.READ_WRITE;
    }

    /**
     * Creates a {@link List} of {@link PropertyMetadata} according to the given parameter
     *
     * @param config identifying the concrete configuration
     * @param givenMetadata used fore deriving the concrete metadata. Must not be null
     * @return a {@link List} of {@link PropertyMetadata} according to the given parameter
     */
    public static List<PropertyMetadata> factoryConfigToPropertyMetadata(
            final VerifyFactoryMethod config, final Collection<PropertyMetadata> givenMetadata) {

        requireNonNull(config);
        requireNonNull(givenMetadata);

        final Map<String, PropertyMetadata> map = new HashMap<>();
        final List<String> ofAsList = Arrays.asList(config.of());

        givenMetadata.stream().filter(p -> ofAsList.contains(p.getName()))
                .forEach(meta -> map.put(meta.getName(), meta));

        for (final String name : config.of()) {
            PropertyHelper.assertPropertyExists(name, map);
            modifyPropertyMetadata(map, config.defaultValued(), config.readOnly(),
                    config.required(), config.transientProperties(), config.writeOnly(),
                    config.assertUnorderedCollection());
        }

        return orderPropertyMetadata(config.of(), handleWritableAttributes(map));
    }

    /**
     * Checks the given type for the annotation {@link VerifyConstructor} and
     * {@link VerifyConstructors} and puts all found in the returned {@link Set}
     *
     * @param annotated the class that may or may not provide the annotations, must not be null
     * @return a {@link Set} of {@link VerifyConstructor} extracted from the annotations of the
     *         given type. May be empty but never null
     */
    public static Set<VerifyConstructor> extractConfiguredConstructorContracts(
            final Class<?> annotated) {
        requireNonNull(annotated);
        final var builder = new CollectionBuilder<VerifyConstructor>();

        MoreReflection.extractAllAnnotations(annotated, VerifyConstructors.class)
                .forEach(contract -> builder.add(Arrays.asList(contract.value())));
        MoreReflection.extractAllAnnotations(annotated, VerifyConstructor.class)
                .forEach(builder::add);

        return builder.toImmutableSet();
    }

    /**
     * Checks the given type for the annotation {@link VerifyFactoryMethod} and
     * {@link VerifyFactoryMethods} and puts all found in the returned list
     *
     * @param annotated the class that may or may not provide the annotations, must not be null
     * @return a Set of {@link VerifyFactoryMethod} extracted from the annotations of the given
     *         type. May be empty but never null
     */
    public static Set<VerifyFactoryMethod> extractConfiguredFactoryContracts(
            final Class<?> annotated) {
        requireNonNull(annotated);
        final var builder = new CollectionBuilder<VerifyFactoryMethod>();

        MoreReflection.extractAllAnnotations(annotated, VerifyFactoryMethods.class)
                .forEach(contract -> builder.add(Arrays.asList(contract.value())));
        MoreReflection.extractAllAnnotations(annotated, VerifyFactoryMethod.class)
                .forEach(builder::add);

        return builder.toImmutableSet();
    }

    /**
     * @param annotated must not be null and must be annotated with {@link VerifyBeanProperty}
     * @param givenMetadata must not be null
     * @return a {@link SortedSet} providing the property configuration derived by the given
     *         properties and the annotated {@link VerifyBeanProperty}
     */
    public static List<PropertyMetadata> handleMetadataForPropertyTest(
            final Class<?> annotated, final List<PropertyMetadata> givenMetadata) {
        requireNonNull(annotated);
        requireNonNull(givenMetadata);

        if (givenMetadata.isEmpty()) {
            log.warn(NO_PROPERTIES_GIVEN_IS_THIS_INTENTIONAL);
            return givenMetadata;
        }

        final Optional<VerifyBeanProperty> contractOption =
            MoreReflection.extractAnnotation(annotated, VerifyBeanProperty.class);

        final var contract = contractOption.orElseThrow(() -> new IllegalArgumentException(
                "Given type does not provide the expected annotation BeanPropertyTestContract, type="
                        + annotated));

        var map =
            PropertyHelper.handleWhiteAndBlacklist(contract.of(), contract.exclude(),
                    givenMetadata);

        modifyPropertyMetadata(map, contract.defaultValued(), contract.readOnly(),
                contract.required(), contract.transientProperties(), contract.writeOnly(),
                contract.assertUnorderedCollection());

        return orderPropertyMetadata(contract.of(), map.values());
    }

    private static List<PropertyMetadata> orderPropertyMetadata(final String[] of,
            final Collection<PropertyMetadata> givenMetadata) {
        final var builder = new CollectionBuilder<PropertyMetadata>();
        if (0 == of.length) {
            builder.add(givenMetadata);
        } else {
            final Map<String, PropertyMetadata> map = new HashMap<>();
            for (final PropertyMetadata metadata : givenMetadata) {
                map.put(metadata.getName(), metadata);
            }
            for (final String name : of) {
                builder.add(map.get(name));
            }
        }
        return builder.toImmutableList();

    }

    /**
     * @param annotated must not be null and must be annotated with {@link VerifyBeanProperty}
     * @param givenMetadata must not be null
     * @return a {@link SortedSet} providing the property configuration derived by the given
     *         properties and the annotated {@link VerifyBuilder}
     */
    public static List<PropertyMetadata> handleMetadataForBuilderTest(
            final Class<?> annotated, final List<PropertyMetadata> givenMetadata) {

        requireNonNull(annotated);
        requireNonNull(givenMetadata);

        if (givenMetadata.isEmpty()) {
            log.warn(NO_PROPERTIES_GIVEN_IS_THIS_INTENTIONAL);
            return givenMetadata;
        }

        final Optional<VerifyBuilder> contractOption =
            MoreReflection.extractAnnotation(annotated, VerifyBuilder.class);

        final var contract = contractOption.orElseThrow(() -> new IllegalArgumentException(
                "Given type does not provide the expected annotation BuilderTestContract, type="
                        + annotated));

        var map =
            PropertyHelper.handleWhiteAndBlacklist(contract.of(), contract.exclude(),
                    givenMetadata);

        modifyPropertyMetadata(map, contract.defaultValued(), contract.readOnly(),
                contract.required(), contract.transientProperties(), contract.writeOnly(),
                contract.assertUnorderedCollection());

        final Map<String, PropertyMetadata> builderPropertyMap = new HashMap<>();
        for (final PropertyMetadata metadata : BuilderPropertyHelper
                .handleBuilderPropertyConfigAnnotations(annotated,
                        mutableList(map.values()))) {
            checkArgument(map.containsKey(metadata.getName()),
                    "Invalid Configuration found: BuilderPropertyConfig and BuilderTestContract do not agree on configuration. offending property: "
                            + metadata);
            builderPropertyMap.put(metadata.getName(), metadata);
        }

        for (final Entry<String, PropertyMetadata> entry : map.entrySet()) {
            if (!builderPropertyMap.containsKey(entry.getKey())) {
                var delegate = entry.getValue();
                if (PropertyAccessStrategy.BEAN_PROPERTY
                        .equals(delegate.getPropertyAccessStrategy())) {
                    var propertyReadWrite = PropertyReadWrite.READ_WRITE;
                    if (PropertyReadWrite.WRITE_ONLY.equals(delegate.getPropertyReadWrite())) {
                        propertyReadWrite = PropertyReadWrite.WRITE_ONLY;
                    }

                    delegate = PropertyMetadataImpl.builder(delegate)
                            .propertyAccessStrategy(PropertyAccessStrategy.BUILDER_DIRECT)
                            .propertyReadWrite(propertyReadWrite).build();
                }
                builderPropertyMap.put(entry.getKey(),
                        BuilderMetadata.builder().delegateMetadata(delegate)
                                .builderMethodPrefix(contract.methodPrefix()).build());
            }
        }

        return orderPropertyMetadata(contract.of(), builderPropertyMap.values());
    }

    /**
     * @param verifyMapper must not be null and must be annotated with
     *            {@link VerifyMapperConfiguration}
     * @param givenMetadata must not be null
     * @return a {@link SortedSet} providing the property configuration derived by the given
     *         properties and the annotated {@link VerifyMapperConfiguration}
     */
    public static List<PropertyMetadata> handleMetadataForMapperTest(
            final VerifyMapperConfiguration verifyMapper, final List<PropertyMetadata> givenMetadata) {

        requireNonNull(verifyMapper);
        requireNonNull(givenMetadata);

        if (givenMetadata.isEmpty()) {
            log.warn(NO_PROPERTIES_GIVEN_IS_THIS_INTENTIONAL);
            return givenMetadata;
        }

        var map =
            PropertyHelper.handleWhiteAndBlacklist(verifyMapper.of(), verifyMapper.exclude(),
                    givenMetadata);

        modifyPropertyMetadata(map, verifyMapper.defaultValued(), verifyMapper.readOnly(),
                verifyMapper.required(), new String[0], verifyMapper.writeOnly(),
                verifyMapper.assertUnorderedCollection());

        return orderPropertyMetadata(verifyMapper.of(), map.values());
    }

    /**
     * Checks the individual contracts and changes / modifies the corresponding
     * {@link PropertyMetadata} accordingly
     *
     * @param map must not be null
     * @param defaultValued must not be null
     * @param readOnly must not be null
     * @param required must not be null
     * @param transientProperties must not be null
     * @param writeOnly must not be null
     * @param unorderedCollection must not be null, see
     *            {@link PropertyReflectionConfig#assertUnorderedCollection()}
     * @return the filtered map
     */
    public static Map<String, PropertyMetadata> modifyPropertyMetadata(
            final Map<String, PropertyMetadata> map,
            final String[] defaultValued, final String[] readOnly, final String[] required,
            final String[] transientProperties, final String[] writeOnly,
            final String[] unorderedCollection) {

        for (final String name : defaultValued) {
            PropertyHelper.assertPropertyExists(name, map);
            map.put(name, PropertyMetadataImpl.builder(map.get(name)).defaultValue(true).build());
        }
        for (final String name : readOnly) {
            PropertyHelper.assertPropertyExists(name, map);
            map.put(name, PropertyMetadataImpl.builder(map.get(name))
                    .propertyReadWrite(PropertyReadWrite.READ_ONLY).build());
        }
        for (final String name : writeOnly) {
            PropertyHelper.assertPropertyExists(name, map);
            map.put(name, PropertyMetadataImpl.builder(map.get(name))
                    .propertyReadWrite(PropertyReadWrite.WRITE_ONLY).build());
        }
        for (final String name : required) {
            PropertyHelper.assertPropertyExists(name, map);
            map.put(name, PropertyMetadataImpl.builder(map.get(name))
                    .required(true).build());
        }
        for (final String name : transientProperties) {
            PropertyHelper.assertPropertyExists(name, map);
            map.put(name, PropertyMetadataImpl.builder(map.get(name))
                    .propertyMemberInfo(PropertyMemberInfo.TRANSIENT).build());
        }
        for (final String name : unorderedCollection) {
            PropertyHelper.assertPropertyExists(name, map);
            map.put(name, PropertyMetadataImpl.builder(map.get(name))
                    .assertionStrategy(AssertionStrategy.COLLECTION_IGNORE_ORDER).build());
        }
        return map;
    }

    /**
     * Checks the individual contracts and changes / modifies the corresponding
     * {@link PropertyMetadata} accordingly
     *
     * @param map must not be null
     * @param defaultValued must not be null
     * @param readOnly must not be null
     * @param required must not be null
     * @param transientProperties must not be null
     * @param writeOnly must not be null
     * @param unorderedCollection must not be null, see
     *            {@link PropertyReflectionConfig#assertUnorderedCollection()}
     * @return the filtered map
     */
    public static Map<String, PropertyMetadata> modifyPropertyMetadata(
            final Map<String, PropertyMetadata> map,
            final List<String> defaultValued, final List<String> readOnly,
            final List<String> required,
            final List<String> transientProperties, final List<String> writeOnly,
            final List<String> unorderedCollection) {

        return modifyPropertyMetadata(map, defaultValued.toArray(new String[defaultValued.size()]),
                readOnly.toArray(new String[readOnly.size()]),
                required.toArray(new String[required.size()]),
                transientProperties.toArray(new String[transientProperties.size()]),
                writeOnly.toArray(new String[writeOnly.size()]),
                unorderedCollection.toArray(new String[unorderedCollection.size()]));
    }

}
