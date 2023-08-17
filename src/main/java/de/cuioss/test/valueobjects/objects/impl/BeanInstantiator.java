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
package de.cuioss.test.valueobjects.objects.impl;

import static de.cuioss.test.valueobjects.objects.impl.AbstractInlineInstantiator.PROPERTIES_MUST_NOT_BE_NULL;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.stream.Collectors;

import de.cuioss.test.valueobjects.objects.ObjectInstantiator;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.PropertySupport;
import de.cuioss.tools.logging.CuiLogger;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @param <T> identifying the type of object to be instantiated
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public class BeanInstantiator<T> implements ParameterizedInstantiator<T> {

    private static final CuiLogger log = new CuiLogger(BeanInstantiator.class);

    @NonNull
    private final ObjectInstantiator<T> instantiator;

    @NonNull
    @Getter
    private final RuntimeProperties runtimeProperties;

    @Override
    public T newInstance(final List<PropertySupport> properties, final boolean generatePropertyValues) {
        assertNotNull(properties, PROPERTIES_MUST_NOT_BE_NULL);
        final var instance = instantiator.newInstance();
        if (generatePropertyValues) {
            properties.forEach(PropertySupport::generateTestValue);
        }
        for (final PropertySupport propertySupport : properties) {
            if (!propertySupport.getPropertyMetadata().getPropertyReadWrite().isWriteable()) {
                log.warn(
                        "Trying to apply a property '{}' which is not writable. Please check usage and configuration of @VerifyBeanProperty, maybe add the property to 'exclude' list.",
                        propertySupport.getName());
            }
            propertySupport.apply(instance);
        }
        return instance;
    }

    @Override
    public T newInstance(final List<PropertyMetadata> properties) {
        assertNotNull(properties, PROPERTIES_MUST_NOT_BE_NULL);
        return newInstance(properties.stream().map(PropertySupport::new).collect(Collectors.toList()), true);
    }

    @Override
    public T newInstanceMinimal() {
        return newInstance(runtimeProperties.getRequiredProperties());
    }

    @Override
    public T newInstanceFull() {
        return newInstance(runtimeProperties.getAdditionalProperties());
    }

    @Override
    public String toString() {
        final var builder = new StringBuilder(getClass().getName());
        builder.append("\nProperty Configuration: ").append(runtimeProperties.toString());
        return builder.toString();
    }
}
