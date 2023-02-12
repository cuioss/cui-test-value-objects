package io.cui.test.valueobjects;

import java.io.Serializable;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import io.cui.test.generator.TypedGenerator;
import io.cui.test.valueobjects.api.property.PropertyConfig;
import io.cui.test.valueobjects.api.property.PropertyConfigs;
import io.cui.test.valueobjects.generator.TypedGeneratorRegistry;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.util.GeneratorAnnotationHelper;
import io.cui.test.valueobjects.util.GeneratorRegistry;
import io.cui.test.valueobjects.util.ReflectionHelper;
import io.cui.tools.reflect.MoreReflection;
import lombok.Getter;

/**
 * Base class for dynamically testing properties. It provides the handling of
 * {@link PropertyMetadata} and {@link TypedGenerator}. In addition it computes the runtime class of
 * the Generic type T using Google Guava and exposes it accordingly, see
 * {@link #getTargetBeanClass()}
 * <h2>Configuration</h2>
 * <p>
 * The tests can be configured using certain annotations, depending on what you want to achieve:
 * <ul>
 * <li>Properties: The configuration of properties to be tested can be tweaked in multiple ways,
 * see {@link io.cui.test.valueobjects.api.property} for details</li>
 * <li>{@link TypedGenerator}: see {@link GeneratorRegistry} for documentation</li>
 * </ul>
 * </p>
 * Usage examples can be found at the package-documentation: {@link io.cui.test.valueobjects}
 *
 * @param <T> identifying the type to be tested is usually but not necessarily at least
 *            {@link Serializable}.
 * @author Oliver Wolff
 */
// owolff: this is a base class for concrete tests
@SuppressWarnings("squid:S2187")
public class AbstractPropertyAwareTest<T> implements GeneratorRegistry {

    @Getter
    private Class<T> targetBeanClass;

    @Getter
    private List<PropertyMetadata> propertyMetadata;

    /**
     * Clears the {@link TypedGeneratorRegistry}
     */
    @AfterEach
    void tearDownGeneratorRegistry() {
        TypedGeneratorRegistry.clear();
    }

    /**
     * Initializes all contracts, properties and generator
     */
    @BeforeEach
    void initializePropertiesAndGenerators() {
        this.targetBeanClass = MoreReflection.extractFirstGenericTypeArgument(getClass());
        GeneratorAnnotationHelper.handleGeneratorsForTestClass(this, registerAdditionalGenerators());
        this.propertyMetadata = resolvePropertyMetadata();
    }

    /**
     * Resolves the {@link PropertyMetadata} by using reflections and the annotations
     * {@link PropertyConfig} and / {@link PropertyConfigs} if provided
     *
     * @return a {@link List} of {@link PropertyMetadata} defining the base line for the
     *         configured attributes
     */
    protected List<PropertyMetadata> resolvePropertyMetadata() {
        return ReflectionHelper.handlePropertyMetadata(getClass(), getTargetBeanClass());
    }
}
