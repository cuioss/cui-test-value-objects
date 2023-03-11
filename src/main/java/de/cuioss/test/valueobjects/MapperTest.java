package de.cuioss.test.valueobjects;

import static de.cuioss.test.valueobjects.util.ReflectionHelper.handlePostProcessConfig;
import static de.cuioss.test.valueobjects.util.ReflectionHelper.handlePropertyMetadata;
import static de.cuioss.test.valueobjects.util.ReflectionHelper.scanBeanTypeForProperties;
import static de.cuioss.test.valueobjects.util.ReflectionHelper.shouldScanClass;
import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.valueobjects.api.VerifyMapperConfiguration;
import de.cuioss.test.valueobjects.api.property.PropertyConfig;
import de.cuioss.test.valueobjects.api.property.PropertyConfigs;
import de.cuioss.test.valueobjects.api.property.PropertyReflectionConfig;
import de.cuioss.test.valueobjects.contract.MapperContractImpl;
import de.cuioss.test.valueobjects.generator.dynamic.GeneratorResolver;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.TestObjectProvider;
import de.cuioss.test.valueobjects.objects.impl.BeanInstantiator;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.util.AnnotationHelper;
import de.cuioss.test.valueobjects.util.GeneratorRegistry;
import de.cuioss.test.valueobjects.util.PropertyHelper;
import de.cuioss.tools.base.Preconditions;
import de.cuioss.tools.reflect.MoreReflection;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Allows to test a mapper implementing a {@link Function} to map a (pseudo-)DTO object based on
 * whatever technology (FHIR, ...) to a DTO object. The actual test-method is
 * {@link #verifyMapper()}. The mapper-test-configuration is defined with
 * {@link VerifyMapperConfiguration}
 *
 * For simple uses-case, like well designed beans there is no special configuration needed
 * <ul>
 * <li>
 * In case the mapper needs to be configured in a special way (not using the default constructor)
 * you can overwrite {@link #getUnderTest()}
 * </li>
 * <li>The metadata / creation of the <em>source</em> objects can be adjusted in multiple ways:
 * <ul>
 * <li>{@link PropertyMetadata}: The annotations on class level affect the metadata for the
 * source-objects</li>
 * <li>{@link #resolveSourcePropertyMetadata()}: can be overwritten as an alternative for using
 * {@link PropertyMetadata} annotations
 * </li>
 * <li>{@link #getSourceInstantiator(RuntimeProperties)}: If not overwritten the default
 * implementation chooses the {@link BeanInstantiator} in order to generate source-objects.</li>
 * </ul>
 * </li>
 * <li>The metadata <em>target</em> objects is derived by reflection. <em>Caution</em>: For more
 * complex objects that can not be created by the generator framework you must provide either a
 * {@link TypedGenerator}, see {@link GeneratorRegistry} or overwrite
 * {@link #anyTargetObject()}</li>
 * </ul>
 *
 * @author Oliver Wolff
 * @param <M> Mapper: The type of the Mapper
 * @param <S> Source: The type of the source-Objects to be mapped from
 * @param <T> Target: The type of the source-Objects to be mapped to
 */
@SuppressWarnings("squid:S2187") // Base class for tests
public class MapperTest<M extends Function<S, T>, S, T> implements GeneratorRegistry, TestObjectProvider<M> {

    @Getter(AccessLevel.PROTECTED)
    private Class<M> mapperClass;

    @Getter(AccessLevel.PROTECTED)
    private Class<? extends S> sourceClass;

    @Getter(AccessLevel.PROTECTED)
    private Class<T> targetClass;

    /**
     * Reads the type information and fills the fields {@link #getMapperClass()},
     * {@link #getSourceClass()}, {@link #getTargetClass()}. It runs it checks only once
     */
    @SuppressWarnings({ "unchecked" })
    protected void intializeTypeInformation() {
        if (null == mapperClass) {
            var parameterized = MoreReflection.extractParameterizedType(getClass()).orElseThrow(
                    () -> new IllegalArgumentException("Given type defines no generic Type: " + getClass()));
            List<Type> types = immutableList(parameterized.getActualTypeArguments());
            Preconditions.checkArgument(3 == types.size(), "Super Class must provide 3 generic types in order to work");
            mapperClass = (Class<M>) MoreReflection.extractGenericTypeCovariantly(types.get(0))
                    .orElseThrow(() -> new AssertionError("Unable to determine mapperClass from type" + getClass()));
            assertNotNull(mapperClass, "Unable to determine mapperClass");
            assertFalse(mapperClass.isInterface(),
                    "This type only works with concrete implementations, but was the interface " + mapperClass);
            sourceClass = (Class<S>) MoreReflection.extractGenericTypeCovariantly(types.get(1))
                    .orElseThrow(() -> new AssertionError("Unable to determine sourceClass from type" + getClass()));
            assertNotNull(sourceClass, "Unable to determine sourceClass");
            targetClass = (Class<T>) MoreReflection.extractGenericTypeCovariantly(types.get(2))
                    .orElseThrow(() -> new AssertionError("Unable to determine targetClass from type" + getClass()));
            assertNotNull(targetClass, "Unable to determine targetClass");
        }
    }

    /**
     * Shorthand for calling {@link MapperTest#verifyMapper(PropertyReflectionConfig)} with
     * {@code null}
     */
    @Test
    public void verifyMapper() {
        verifyMapper(null);
    }

    /**
     * The actual test-method to be run
     *
     * @param targetConfig providing configuration, may be null
     */
    public void verifyMapper(PropertyReflectionConfig targetConfig) {
        intializeTypeInformation();
        Optional<VerifyMapperConfiguration> config =
            MoreReflection.extractAnnotation(getClass(), VerifyMapperConfiguration.class);

        assertTrue(config.isPresent(),
                "The mapper test must be annotated with " + VerifyMapperConfiguration.class.getName());

        @SuppressWarnings("squid:S3655") // owolff: false positive, checked above
        var processedSourceProperties =
            AnnotationHelper.handleMetadataForMapperTest(config.get(), resolveSourcePropertyMetadata());

        ParameterizedInstantiator<? extends S> sourceInstantiator =
            getSourceInstantiator(new RuntimeProperties(processedSourceProperties));

        var targetProperties = new RuntimeProperties(resolveTargetPropertyMetadata(targetConfig));

        new MapperContractImpl<>(config.get(), sourceInstantiator, targetProperties, getUnderTest()).assertContract();
    }

    @Override
    public M getUnderTest() {
        intializeTypeInformation();
        return new DefaultInstantiator<>(mapperClass).newInstance();
    }

    /**
     * Resolves the {@link PropertyMetadata} for the <em>source</em> objects by using reflection and
     * the
     * annotations {@link PropertyConfig} and / {@link PropertyConfigs} if provided.
     *
     * @return a {@link List} of {@link PropertyMetadata} defining the base line for the
     *         configured attributes
     */
    public List<PropertyMetadata> resolveSourcePropertyMetadata() {
        intializeTypeInformation();
        return handlePropertyMetadata(getClass(), getSourceClass());
    }

    /**
     * Resolves the {@link PropertyMetadata} for the <em>target</em> objects by using reflection
     *
     * @param config providing configuration, may be null
     *
     * @return a {@link List} of {@link PropertyMetadata} defining the base line for the
     *         configured attributes
     */
    public List<PropertyMetadata> resolveTargetPropertyMetadata(PropertyReflectionConfig config) {
        intializeTypeInformation();
        final List<PropertyMetadata> builder = new ArrayList<>();
        if (shouldScanClass(getClass())) {
            final SortedSet<PropertyMetadata> scanned = new TreeSet<>(
                    scanBeanTypeForProperties(anyTargetObject().getClass(), config));
            builder.addAll(handlePostProcessConfig(config, scanned));
        }
        final var handled =
            PropertyHelper.handlePrimitiveAsDefaults(PropertyHelper.toMapView(builder).values());
        PropertyHelper.logMessageForTargetPropertyMetadata(handled);
        return immutableList(handled);
    }

    /**
     * @return a target object to be used for reflection based resolving of
     *         {@link PropertyMetadata}. The default implementation uses the
     *         {@link GeneratorRegistry} in order to instantiate a corresponding object. For more
     *         complex objects you should add a corresponding {@link TypedGenerator}, see
     *         {@link GeneratorRegistry}
     */
    public T anyTargetObject() {
        intializeTypeInformation();
        return GeneratorResolver.resolveGenerator(targetClass).next();
    }

    /**
     * @param runtimeProperties to be used for creating the {@link ParameterizedInstantiator}
     *
     * @return the {@link ParameterizedInstantiator} to be used for instantiating source-object. If
     *         not overwritten it default to the beanInstantiator
     */
    @SuppressWarnings("java:S1452") // owolff: using wildcards here is the only way
    public ParameterizedInstantiator<? extends S> getSourceInstantiator(RuntimeProperties runtimeProperties) {
        intializeTypeInformation();
        return new BeanInstantiator<>(new DefaultInstantiator<>(getSourceClass()), runtimeProperties);
    }
}
