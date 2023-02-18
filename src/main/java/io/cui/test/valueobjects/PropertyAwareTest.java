package io.cui.test.valueobjects;

import java.io.Serializable;
import java.util.List;
import java.util.SortedSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import io.cui.test.generator.TypedGenerator;
import io.cui.test.generator.junit.GeneratorControllerExtension;
import io.cui.test.valueobjects.api.property.PropertyConfig;
import io.cui.test.valueobjects.api.property.PropertyConfigs;
import io.cui.test.valueobjects.junit5.extension.GeneratorRegistryController;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.util.GeneratorRegistry;
import io.cui.test.valueobjects.util.ReflectionHelper;
import io.cui.tools.reflect.MoreReflection;
import lombok.Getter;
import lombok.Setter;

/**
 * Base class for dynamically testing properties. It provides the handling of
 * {@link PropertyMetadata} and {@link TypedGenerator}. In addition it computes the runtime class of
 * the Generic type T using Google Guava and exposes it accordingly, see
 * {@link #getTargetBeanClass()}
 * <h2>Configuration</h2>
 * The tests can be configured using certain annotations, depending on what you want to achieve:
 * <ul>
 * <li>Properties: The configuration of properties to be tested can be tweaked in multiple ways,
 * see {@link io.cui.test.valueobjects.api.property} for details</li>
 * <li>{@link TypedGenerator}: see {@link GeneratorRegistry} for documentation</li>
 * </ul>
 * Usage examples can be found at the package-documentation:
 * {@link io.cui.test.valueobjects.junit5}
 *
 * @param <T> identifying the type to be tested is usually but not necessarily at least
 *            {@link Serializable}.
 * @author Oliver Wolff
 */
@SuppressWarnings("squid:S2187") // owolff: this is a base class for concrete tests
@ExtendWith({ GeneratorControllerExtension.class, GeneratorRegistryController.class })
public class PropertyAwareTest<T> implements GeneratorRegistry {

    @Getter
    @Setter
    private Class<T> targetBeanClass;

    @Getter
    private List<PropertyMetadata> propertyMetadata;

    /**
     * Initializes all contracts, properties and generator
     */
    @BeforeEach
    public void initializePropertiesAndGenerators() {
        this.targetBeanClass = MoreReflection.extractFirstGenericTypeArgument(getClass());

        this.propertyMetadata = resolvePropertyMetadata();
    }

    /**
     * Resolves the {@link PropertyMetadata} by using reflections and the annotations
     * {@link PropertyConfig} and / {@link PropertyConfigs} if provided
     *
     * @return a {@link SortedSet} of {@link PropertyMetadata} defining the base line for the
     *         configured attributes
     */
    protected List<PropertyMetadata> resolvePropertyMetadata() {
        return ReflectionHelper.handlePropertyMetadata(getClass(), getTargetBeanClass());
    }
}
