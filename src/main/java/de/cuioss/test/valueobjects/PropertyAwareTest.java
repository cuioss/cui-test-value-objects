/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects;

import java.io.Serializable;
import java.util.List;
import java.util.SortedSet;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.generator.junit.GeneratorControllerExtension;
import de.cuioss.test.valueobjects.api.property.PropertyConfig;
import de.cuioss.test.valueobjects.api.property.PropertyConfigs;
import de.cuioss.test.valueobjects.junit5.extension.GeneratorRegistryController;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.util.GeneratorRegistry;
import de.cuioss.test.valueobjects.util.ReflectionHelper;
import de.cuioss.tools.reflect.MoreReflection;
import lombok.Getter;
import lombok.Setter;

/**
 * Base class for dynamically testing properties. It provides the handling of
 * {@link PropertyMetadata} and {@link TypedGenerator}. In addition it computes
 * the runtime class of the Generic type T and exposes it accordingly, see
 * {@link #getTargetBeanClass()}
 * <h2>Configuration</h2> The tests can be configured using certain annotations,
 * depending on what you want to achieve:
 * <ul>
 * <li>Properties: The configuration of properties to be tested can be tweaked
 * in multiple ways, see {@link de.cuioss.test.valueobjects.api.property} for
 * details</li>
 * <li>{@link TypedGenerator}: see {@link GeneratorRegistry} for
 * documentation</li>
 * </ul>
 * Usage examples can be found at the package-documentation:
 * {@link de.cuioss.test.valueobjects.junit5}
 *
 * @param <T> identifying the type to be tested is usually but not necessarily
 *            at least {@link Serializable}.
 * @author Oliver Wolff
 */
@SuppressWarnings("squid:S2187") // owolff: this is a base class for concrete tests
@ExtendWith({GeneratorControllerExtension.class, GeneratorRegistryController.class})
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
    void initializePropertiesAndGenerators() {
        targetBeanClass = MoreReflection.extractFirstGenericTypeArgument(getClass());

        propertyMetadata = resolvePropertyMetadata();
    }

    /**
     * Resolves the {@link PropertyMetadata} by using reflections and the
     * annotations {@link PropertyConfig} and / {@link PropertyConfigs} if provided
     *
     * @return a {@link SortedSet} of {@link PropertyMetadata} defining the base
     *         line for the configured attributes
     */
    protected List<PropertyMetadata> resolvePropertyMetadata() {
        return ReflectionHelper.handlePropertyMetadata(getClass(), getTargetBeanClass());
    }
}
